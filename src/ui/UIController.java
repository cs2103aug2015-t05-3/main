package ui;

import java.net.URL;
import java.util.ArrayList;
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
import taskCollections.Task;
import util.TimeUtil;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello Jim,";

	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";
	private static final String VAR_TABLE_STRING_EDATE = "eDate";
	private static final String VAR_TABLE_STRING_PRIORITY = "priority";

	@FXML private Text cmdMsg;
	@FXML private Text pendingMsg;
	@FXML private TableColumn<UITaskTimed, String> idTimed;
	@FXML private TableColumn<UITaskTimed, String> taskTimed;
	@FXML private TableColumn<UITaskTimed, String> sDate;
	@FXML private TableColumn<UITaskTimed, String> eDate;
	@FXML private TableColumn<UITaskFloat, String> idFloat;
	@FXML private TableColumn<UITaskFloat, String> taskFloat;
	@FXML private TableColumn<UITaskFloat, String> priority;
	@FXML private TableView<UITaskTimed> tableTimed;
	@FXML private TableView<UITaskFloat> tableFloat;
	@FXML private TextField input;

	//TODO remove this
	private int debugTestingIndex = 100;

	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;

	private static UI ui;

	//private static Semaphore lock;
	private ArrayList<String> inputBuffer = new ArrayList<>();

	final ObservableList<UITaskTimed> dataTimed = FXCollections.observableArrayList();
	final ObservableList<UITaskFloat> dataFloat = FXCollections.observableArrayList();

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Text fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		pendingMsg.setText(MSG_PENDING_HELLO);

		// Table
		idTimed.setCellValueFactory(  new PropertyValueFactory<UITaskTimed, String>(VAR_TABLE_STRING_ID));
		taskTimed.setCellValueFactory(new PropertyValueFactory<UITaskTimed, String>(VAR_TABLE_STRING_TASK));
		sDate.setCellValueFactory(    new PropertyValueFactory<UITaskTimed, String>(VAR_TABLE_STRING_SDATE));
		eDate.setCellValueFactory(    new PropertyValueFactory<UITaskTimed, String>(VAR_TABLE_STRING_EDATE));

		idFloat.setCellValueFactory(  new PropertyValueFactory<UITaskFloat, String>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(new PropertyValueFactory<UITaskFloat, String>(VAR_TABLE_STRING_TASK));
		priority.setCellValueFactory( new PropertyValueFactory<UITaskFloat, String>(VAR_TABLE_STRING_PRIORITY));

		idTimed.setSortType(TableColumn.SortType.ASCENDING);

		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);

		tableTimed.getSortOrder().add(idTimed);

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
		dataTimed.clear();
		dataFloat.clear();

		for (Task t : _nonFloatingTaskList) {
			UITaskTimed ui1 = new UITaskTimed(t.getId(), t.getName(), t.getStartTime(), t.getEndTime());
			dataTimed.add(ui1);
		}

		for (Task t : _floatingTaskList) {
			//TODO: RESOLVE PRIORITY
			UITaskFloat ui2 = new UITaskFloat(t.getId(), t.getName(), "N");
			dataFloat.add(ui2);
		}
	}

	void clearInput(){
		input.clear();
	}

	// To Implement Priority
	public static class UITaskFloat {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty priority;

		private UITaskFloat(int id, String task, String priority) {
			this.id = new SimpleIntegerProperty(id);
			this.task = new SimpleStringProperty(task);
			this.priority = new SimpleStringProperty(priority);
		}

		public int getId() {
			return id.get();
		}

		public String getTask() {
			return task.get();
		}

		public String getPriority() {
			return priority.get();
		}
	}

	public static class UITaskTimed {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty sDate;
		private final SimpleStringProperty eDate;
		private final SimpleLongProperty eDateLong;

		private UITaskTimed(int id, String task, long sDate, long eDate) {
			this.id = new SimpleIntegerProperty(id);
			this.task = new SimpleStringProperty(task);
			this.sDate = new SimpleStringProperty(TimeUtil.getUIFormattedDate(sDate));
			this.eDate = new SimpleStringProperty(TimeUtil.getUIFormattedDate(eDate));
			this.eDateLong = new SimpleLongProperty(eDate);
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

		public String getEDate() {
			return eDate.get();
		}

		public long getEDateLong() {
			return eDateLong.get();
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

	// Debugging code
	public void add() {
		UITaskTimed t = new UITaskTimed(debugTestingIndex--, "Buy Milk from Shop", 1442851200000L, 1443283140000L);
		dataTimed.add(t);

		UITaskFloat t1 = new UITaskFloat(debugTestingIndex--, "Go Die in a fire", "N");
		dataFloat.add(t1);
		tableTimed.getSortOrder().add(idTimed);

		clearInput();
	}

	public void delete(int id) {
		dataTimed.remove(id);
		clearInput();
	}
}
