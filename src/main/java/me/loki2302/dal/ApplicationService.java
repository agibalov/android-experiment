package me.loki2302.dal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import me.loki2302.application.Repository;
import me.loki2302.application.Task;
import me.loki2302.application.TaskStatusIsQuery;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;
import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationService {
	private String sessionToken;
	
	@Inject
	private RetaskService retaskService;
		
	private volatile boolean isTaskRepositoryInitialized;
	private final Repository<Task> taskRepository = new Repository<Task>();
	private final Object isTaskRepositoryInitializingLock = new Object();
	private volatile boolean isTaskRepositoryInitializing;
	private final Queue<Runnable> taskRepositoryAwaitersQueue = new LinkedBlockingQueue<Runnable>();
	
	public void signIn(LongOperationListener longOperationListener, final String email, String password, final ApplicationServiceCallback<String> callback) {
		retaskService.signIn(longOperationListener, email, password, new ApiCallback<SessionDto>() {
			@Override
			public void onSuccess(SessionDto result) {
				ApplicationService.this.sessionToken = result.sessionToken;
				callback.onSuccess(result.sessionToken);
			}

			@Override
			public void onError(ServiceResultDto<SessionDto> response, Exception e) {				
			}			
		});		
	}
	
	public void getTasksByStatus(LongOperationListener longOperationListener, final TaskStatus status, final ApplicationServiceCallback<List<Task>> callback) {
		Ln.i("getTasksByStatus(%s), isTaskRepositoryInitialized=%b", status, isTaskRepositoryInitialized);
		whenTaskRepositoryIsReady(longOperationListener, new Runnable() {
			@Override
			public void run() {
				callback.onSuccess(taskRepository.getWhere(new TaskStatusIsQuery(status)));
			}			
		});
	}
	
	public void getTask(LongOperationListener longOperationListener, final int taskId, final ApplicationServiceCallback<Task> callback) {
		whenTaskRepositoryIsReady(longOperationListener, new Runnable() {
			@Override
			public void run() {
				callback.onSuccess(taskRepository.getOne(taskId));
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
			
			retaskService.getWorkspace(longOperationListener, sessionToken, new ApiCallback<WorkspaceDto>() {
				@Override
				public void onSuccess(WorkspaceDto result) {
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

				@Override
				public void onError(ServiceResultDto<WorkspaceDto> response, Exception e) {
					// callback.onError();
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