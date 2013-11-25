package me.loki2302.dal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import me.loki2302.application.Repository;
import me.loki2302.application.Task;
import me.loki2302.application.TaskStatusIsQuery;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationService {
	@Inject
	private ApplicationState applicationState;
	
	@Inject
	private RetaskService retaskService;
	
	private volatile boolean isTaskRepositoryInitialized;
	private final Repository<Task> taskRepository = new Repository<Task>();
	private final Object isTaskRepositoryInitializingLock = new Object();
	private volatile boolean isTaskRepositoryInitializing;
	private final Queue<Runnable> taskRepositoryAwaitersQueue = new LinkedBlockingQueue<Runnable>();
	
	public Promise<String, Exception, Void> signIn(LongOperationListener longOperationListener, final String email, String password) {
		return retaskService.signIn(longOperationListener, email, password).then(new DonePipe<SessionDto, String, Exception, Void>() {
			@Override
			public Deferred<String, Exception, Void> pipeDone(SessionDto result) {
				String sessionToken = result.sessionToken;
				applicationState.setSessionToken(sessionToken);
				return new DeferredObject<String, Exception, Void>().resolve(sessionToken);
			}
		});		
	}
	
	public Promise<Object, Exception, Void> signUp(LongOperationListener longOperationListener, final String email, String password) {
		return retaskService.signUp(longOperationListener, email, password);		
	}
	
	public Promise<List<Task>, Exception, Void> getTasksByStatus(LongOperationListener longOperationListener, final TaskStatus status) {
		final Deferred<List<Task>, Exception, Void> deferred = new DeferredObject<List<Task>, Exception, Void>(); 
		
		Ln.i("getTasksByStatus(%s), isTaskRepositoryInitialized=%b", status, isTaskRepositoryInitialized);
		whenTaskRepositoryIsReady(longOperationListener, new Runnable() {
			@Override
			public void run() {
				deferred.resolve(taskRepository.getWhere(new TaskStatusIsQuery(status)));
			}			
		});
		
		return deferred.promise();
	}
	
	public Promise<Task, Exception, Void> getTask(LongOperationListener longOperationListener, final int taskId) {
		final Deferred<Task, Exception, Void> deferred = new DeferredObject<Task, Exception, Void>();
		
		whenTaskRepositoryIsReady(longOperationListener, new Runnable() {
			@Override
			public void run() {
				deferred.resolve(taskRepository.getOne(taskId));
			}			
		});
		
		return deferred.promise();
	}
	
	public Promise<Task, Exception, Void> createTask(LongOperationListener longOperationListener, String taskDescription) {
		TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
		taskDescriptionDto.taskDescription = taskDescription;
		return retaskService.createTask(longOperationListener, applicationState.getSessionToken(), taskDescriptionDto).then(new DonePipe<TaskDto, Task, Exception, Void>() {
			@Override
			public Deferred<Task, Exception, Void> pipeDone(TaskDto result) {
				Task task = taskFromTaskDto(result);
				taskRepository.add(task);
				return new DeferredObject<Task, Exception, Void>().resolve(task);
			}			
		});
	}	
	
	public Promise<Task, Exception, Void> updateTask(LongOperationListener longOperationListener, int taskId, String taskDescription) {
		TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
		taskDescriptionDto.taskDescription = taskDescription;
		return retaskService.updateTask(longOperationListener, applicationState.getSessionToken(), taskId, taskDescriptionDto).then(new DonePipe<TaskDto, Task, Exception, Void>() {
			@Override
			public Deferred<Task, Exception, Void> pipeDone(TaskDto result) {
				Task task = taskFromTaskDto(result);
				taskRepository.add(task);
				return new DeferredObject<Task, Exception, Void>().resolve(task);
			}			
		});
	}
	
	public Promise<Task, Exception, Void> progressTask(LongOperationListener longOperationListener, int taskId) {
		return retaskService.progressTask(longOperationListener, applicationState.getSessionToken(), taskId).then(new DonePipe<TaskDto, Task, Exception, Void>() {
			@Override
			public Deferred<Task, Exception, Void> pipeDone(TaskDto result) {
				Task task = taskFromTaskDto(result);
				taskRepository.add(task);
				return new DeferredObject<Task, Exception, Void>().resolve(task);
			}			
		});
	}
	
	public Promise<Task, Exception, Void> unprogressTask(LongOperationListener longOperationListener, int taskId) {
		return retaskService.unprogressTask(longOperationListener, applicationState.getSessionToken(), taskId).then(new DonePipe<TaskDto, Task, Exception, Void>() {
			@Override
			public Deferred<Task, Exception, Void> pipeDone(TaskDto result) {
				Task task = taskFromTaskDto(result);
				taskRepository.add(task);
				return new DeferredObject<Task, Exception, Void>().resolve(task);
			}			
		});
	}
	
	public Promise<Object, Exception, Void> deleteTask(LongOperationListener longOperationListener, final int taskId) {
		return retaskService.deleteTask(longOperationListener, applicationState.getSessionToken(), taskId).then(new DoneCallback<Object>() {
			@Override
			public void onDone(Object result) {
				taskRepository.remove(taskId);				
			}			
		});
	}
	
	private void whenTaskRepositoryIsReady(LongOperationListener longOperationListener, Runnable runnable) {
		if(isTaskRepositoryInitialized) {
			runnable.run();
		} else {
			synchronized(taskRepositoryAwaitersQueue) {
				taskRepositoryAwaitersQueue.add(runnable);
			}
			
			synchronized(isTaskRepositoryInitializingLock) {
				if(isTaskRepositoryInitializing) {
					return;
				}
				
				isTaskRepositoryInitializing = true;
			}
			
			retaskService.getWorkspace(longOperationListener, applicationState.getSessionToken()).done(new DoneCallback<WorkspaceDto>() {
				@Override
				public void onDone(WorkspaceDto result) {
					for(TaskDto taskDto : result.tasks) {
						Task task = taskFromTaskDto(taskDto);
						taskRepository.add(task);
					}
					
					isTaskRepositoryInitialized = true;
					
					synchronized(taskRepositoryAwaitersQueue) {
						while(!taskRepositoryAwaitersQueue.isEmpty()) {
							Runnable runnable = taskRepositoryAwaitersQueue.poll();
							runnable.run();
						}
					}					
				}				
			});
		}		
	}
					
	private static Task taskFromTaskDto(TaskDto taskDto) {
		Task task = new Task();
		task.id = taskDto.taskId;
		task.description = taskDto.taskDescription;
		task.status = taskDto.taskStatus;
		return task;
	}
}