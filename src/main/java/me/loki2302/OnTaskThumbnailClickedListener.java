package me.loki2302;

import me.loki2302.dal.dto.TaskDto;

public interface OnTaskThumbnailClickedListener {
	void onTaskThumbnailClicked(TaskDto model);
}