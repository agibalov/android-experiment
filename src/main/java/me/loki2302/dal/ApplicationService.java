package me.loki2302.dal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;

import me.loki2302.ApplicationState;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;

import com.google.inject.Inject;

@ContextSingleton
public class ApplicationService {
	@Inject
	private ApplicationState applicationState;
	
	@Inject
	private RetaskService retaskService;
		
	private final Object isTaskRepositoryInitializingLock = new Object();
	private volatile boolean isTaskRepositoryInitializing;
	private final Queue<Runnable> taskRepositoryAwaitersQueue = new LinkedBlockingQueue<Runnable>();	
	
	public void getTasksByStatus(final TaskStatus status, final ApplicationServiceCallback<List<Task>> callback) {
		Ln.i("getTasksByStatus(%s), isTaskRepositoryInitialized=%b", status, applicationState.isTaskRepositoryInitialized());
		whenTaskRepositoryIsReady(new Runnable() {
			@Override
			public void run() {
				callback.onSuccess(applicationState.getTaskRepository().getWhere(new TaskStatusIsQuery(status)));
			}			
		});
	}
	
	public void getTask(final int taskId, final ApplicationServiceCallback<Task> callback) {
		whenTaskRepositoryIsReady(new Runnable() {
			@Override
			public void run() {
				callback.onSuccess(applicationState.getTaskRepository().getOne(taskId));
			}			
		});
	}
	
	private void whenTaskRepositoryIsReady(Runnable runnable) {
		if(applicationState.isTaskRepositoryInitialized()) {
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
			
			retaskService.getWorkspace(applicationState.getSessionToken(), new ApiCallback<WorkspaceDto>() {
				@Override
				public void onSuccess(WorkspaceDto result) {
					for(TaskDto taskDto : result.tasks) {
						Task task = taskFromTaskDto(taskDto);
						applicationState.getTaskRepository().add(task);
					}
					
					applicationState.setTaskRepositoryInitialized(true);
					
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