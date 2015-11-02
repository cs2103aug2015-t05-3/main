package ui;

import javafx.beans.property.SimpleStringProperty;

public class UITask {
	private final SimpleStringProperty id;
	private final SimpleStringProperty task;
	private final SimpleStringProperty sDate;

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

}
