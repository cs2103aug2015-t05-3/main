package ui;

import javafx.beans.property.SimpleStringProperty;
import parser.TimeProcessor;
import taskCollections.Task;

public class UITask {
	private static final String EMPTY_STRING = "";

	private final SimpleStringProperty id;
	private final SimpleStringProperty task;
	private final SimpleStringProperty sDate;
	private Task taskObj;

	public UITask(Task taskObj) {
		this.taskObj = taskObj;
		this.id = getSimpleString(taskObj.getId());
		this.task = getSimpleString(taskObj.getName());
		String sDate = getSDateString(taskObj);
		this.sDate = getSimpleString(sDate);
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

	public UITask(String id, String task, String dateString) {
		this.id = new SimpleStringProperty(id);
		this.task = new SimpleStringProperty(task);
		this.sDate = new SimpleStringProperty(dateString);
	}

	public UITask(String id, String task) {
		this.id = new SimpleStringProperty(id);
		this.task = new SimpleStringProperty(task);
		this.sDate = null;
	}

	public String getId() {
		return id.get();
	}

	public String getTask() {
		return task.get();
	}

	public String getSDate() {
		return sDate.get();
	}

	SimpleStringProperty getSimpleString(int integer) {
		return getSimpleString(String.valueOf(integer));
	}

	SimpleStringProperty getSimpleString(String str) {
		return new SimpleStringProperty(str);
	}
}
