package ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UITask {
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty task;
	private final SimpleStringProperty sDate;
	private final SimpleStringProperty priority;
	private final SimpleBooleanProperty isDone;

	public UITask(int id, String task, String dateString, String priority, String flag) {
		this.id = new SimpleIntegerProperty(id);
		this.task = new SimpleStringProperty(task);
		this.sDate = new SimpleStringProperty(dateString);
		this.priority = new SimpleStringProperty(priority);
		
		if (flag.contains("DONE")) {
			this.isDone = new SimpleBooleanProperty(true);
		} else {
			this.isDone = new SimpleBooleanProperty(false);			
		}		
	}

	public UITask(int id, String task, String priority, String flag) {
		this.id = new SimpleIntegerProperty(id);
		this.task = new SimpleStringProperty(task);
		this.sDate = null;
		this.priority = new SimpleStringProperty(priority);
		
		if (flag.contains("DONE")) {
			this.isDone = new SimpleBooleanProperty(true);
		} else {
			this.isDone = new SimpleBooleanProperty(false);			
		}	
	}

	public int getId() {
		return id.get();
	}

	public String getTask() {
		return task.get();
	}

	public String getSDate() {
		return sDate.get();
	}
	
	public String getPriority() {
		return priority.get();
	}
	
	public boolean getIsDone() {
		return isDone.get();
	}
}
