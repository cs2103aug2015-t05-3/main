package ui;

import parser.TimeProcessor;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;

public class UITask {
	private static final String EMPTY_STRING = "";

	private Task taskObj;

	public UITask(Task taskObj) {
		this.taskObj = taskObj;
	}

	private String getSDateString(Task taskObj) {
		TimeProcessor tp = TimeProcessor.getInstance();
		String dateStr;

		long startTimeLong = taskObj.getStartTime();
		long endTimeLong = taskObj.getEndTime();

		if (endTimeLong == Task.DATE_NULL) {
			dateStr = EMPTY_STRING;
		} else if (startTimeLong == Task.DATE_NULL) {
			dateStr = tp.getFormattedDate(endTimeLong);
		} else {
			dateStr = tp.getFormattedDate(startTimeLong, endTimeLong);
		}
		return dateStr;
	}

	public Integer getId() {
		return taskObj.getId();
	}

	public String getTask() {
		return taskObj.getName();
	}

	public String getSDate() {
		return getSDateString(taskObj);
	}

	public long getEndTime() {
		return taskObj.getEndTime();
	}

	public PRIORITY_TYPE getPriority() {
		return taskObj.getPriority();
	}

	public FLAG_TYPE getFlag() {
		return taskObj.getFlag();
	}

}
