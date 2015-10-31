package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import parser.TimeProcessor;
import taskCollections.Task;
import taskCollections.comparators.EndTimeComparator;
import util.TimeUtil;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello %s,";

	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

	@FXML private Text cmdMsg;
	@FXML private Text pendingMsg;
	@FXML private TableColumn<UITask, String> idTimed;
	@FXML private TableColumn<UITask, String> taskTimed;
	@FXML private TableColumn<UITask, String> sDate;
	@FXML private TableColumn<UITask, String> idFloat;
	@FXML private TableColumn<UITask, String> taskFloat;
	@FXML private TableView<UITask> tableTimed;
	@FXML private TableView<UITask> tableFloat;
	@FXML private TextField input;

	//TODO remove this
	private int debugTestingIndex = 100;

	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;

	private static UI ui;

	private ArrayList<String> inputBuffer = new ArrayList<>();

	final ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	final ObservableList<UITask> dataFloat = FXCollections.observableArrayList();
	

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Text fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");
		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));

		// Table
		idTimed.setCellValueFactory(
				new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		taskTimed.setCellValueFactory(
				new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		sDate.setCellValueFactory(
				new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_SDATE));

		idFloat.setCellValueFactory(
				new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(
				new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));

		//idTimed.setSortType(TableColumn.SortType.ASCENDING);

		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);

		//tableTimed.getSortOrder().add(idTimed);

		// Focus Settings
		tableTimed.setFocusTraversable(false);
		tableFloat.setFocusTraversable(false);
	}

	public UIController() {}

	// Create UI
	static void createUI() {
		if (ui == null) {
			ui = new UI();

			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(ui.getClass());
				}
			}.start();

			while(!ui.isInitialised()) {
				try {
				    Thread.sleep(0);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}
	}

	String getInput() {
		synchronized (inputBuffer) {
            // wait for input from field
            while (inputBuffer.isEmpty()) {
            	try{
            		inputBuffer.wait();
            	} catch(InterruptedException e){ }
            }

            return inputBuffer.remove(0);
        }
	}

	void setOutputMsg(String a){
		try {
			cmdMsg.setText(a);
		} catch (NullPointerException e) {
			return;
		}
	}

	void seperateTaskList(List<Task> taskList){
		
		_nonFloatingTaskList = new ArrayList<Task>();
		_floatingTaskList = new ArrayList<Task>();

		//Iterate through list and remove all floating tasks
		for(int i=0; i < taskList.size();){
			if(isFloating(taskList.get(i))){
				_floatingTaskList.add(taskList.remove(i));
			} else {
				i++;
			}
		}
		//Remaining tasks are all non-floating
		_nonFloatingTaskList = taskList;

		Collections.sort(_nonFloatingTaskList, 
				new taskCollections.comparators.EndTimeComparator());
		//Collections.sort(_floatingTaskList);
		
		generateTable();
	}

	private boolean isFloating(Task task){
		if(task.getEndTime() == 0){
			return true;
		}else{
			return false;
		}
	}

	private void generateTable() {
		
		TimeProcessor tp = TimeProcessor.getInstance();
		String generatedString = null;
		
		dataTimed.clear();
		dataFloat.clear();

		for (Task t : _nonFloatingTaskList) {
			if (t.getStartTime() == 0) {
				generatedString = tp.getFormattedDate(t.getEndTime());
			} else {
				generatedString = tp.getFormattedDate(t.getStartTime(), t.getEndTime());
			}
			System.out.println(generatedString);
			UITask ui1 = new UITask(t.getId(), t.getName(), generatedString);
			dataTimed.add(ui1);
		}

		for (Task t : _floatingTaskList) {
			UITask ui2 = new UITask(t.getId(), t.getName());
			dataFloat.add(ui2);
		}
	}

	void clearInput(){
		input.clear();
	}

	public static class UITask {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty sDate;

		private UITask(int id, String task, String dateString) {
			this.id = new SimpleIntegerProperty(id);
			this.task = new SimpleStringProperty(task);
			this.sDate = new SimpleStringProperty(dateString);
		}
		
		private UITask(int id, String task) {
			this.id = new SimpleIntegerProperty(id);
			this.task = new SimpleStringProperty(task);
			this.sDate = null;
		}

		public int getId() {
			return id.get();
		}

		public String getTask() {
			return task.get();
		}
		
		public String getsDate() {
			return sDate.get();
		}
	}

	// Event methods
	public void enterPressed() {

		synchronized (inputBuffer) {
            String in = input.getText().trim();

			inputBuffer.add(in);
            inputBuffer.notify();
        }

		// Other classes will do the job.
		clearInput();
	}

	/*
	// Debugging code
	public void add() {
		UITaskTimed t = new UITaskTimed(debugTestingIndex--, "Buy Milk from Shop", 1442851200000L, 1443283140000L);
		dataTimed.add(t);

		UITaskFloat t1 = new UITaskFloat(debugTestingIndex--, "Go Die in a fire", "N");
		dataFloat.add(t1);
		tableTimed.getSortOrder().add(idTimed);

		clearInput();
	}
	*/
	/*
	public void delete(int id) {
		dataTimed.remove(id);
		clearInput();
	}
	*/
}
