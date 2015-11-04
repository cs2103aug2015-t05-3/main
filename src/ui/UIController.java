package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.command.Command;
import parser.CommandProcessor;
import parser.TimeProcessor;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import util.StringUtil;
import util.TimeUtil;

public class UIController implements Initializable {

	// Message string constants
	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello %s,";
	private static final String MSG_EMPTY = "";
	private static final String MSG_EMPTY_TABLE = "Nothing here";

	// Utility string constants
	private static final String EMPTY_STRING = "";
	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

	// FXML constants
	@FXML private Label pendingMsg;
	@FXML private Label timeDateMsg;
	@FXML private Label cmdMsg;
	@FXML private Label syntaxMsg;
	@FXML private Label tableFloatHeader;
	@FXML private Label tableTimedHeader;
	@FXML private Label overdueCount;
	@FXML private Label pendingCount;
	@FXML private Label doneCount;
	@FXML private TableColumn<UITask, String> idTimed;
	@FXML private TableColumn<UITask, String> taskTimed;
	@FXML private TableColumn<UITask, String> sDate;
	@FXML private TableColumn<UITask, String> idFloat;
	@FXML private TableColumn<UITask, String> taskFloat;
	@FXML private TableView<UITask> tableTimed;
	@FXML private TableView<UITask> tableFloat;
	@FXML private TextField input;

	private static UI uI;
	private static UIController uIController;
	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;
	private ArrayList<String> inputBuffer = new ArrayList<>();
	ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	ObservableList<UITask> dataFloat = FXCollections.observableArrayList();

	private Queue<String> masterQ;
	private Stack<String> upStack;
	private Stack<String> downStack;

	public UIController() {
		masterQ = new LinkedList<String>();
		upStack = new Stack<String>();
		downStack = new Stack<String>();
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Label fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");

		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));

		//TODO unimplemented label field
		timeDateMsg.setText(EMPTY_STRING);
		overdueCount.setText(EMPTY_STRING);
		pendingCount.setText(EMPTY_STRING);
		doneCount.setText(EMPTY_STRING);

		// Table
		idTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		taskTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		sDate.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_SDATE));

		idTimed.setCellFactory((TableColumn<UITask, String> param) -> updateColor());
		taskTimed.setCellFactory((TableColumn<UITask, String> param) -> updateColor());
		sDate.setCellFactory((TableColumn<UITask, String> param) -> updateColor());

		idFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));

		idFloat.setCellFactory((TableColumn<UITask, String> param) -> updateColor());
		taskFloat.setCellFactory((TableColumn<UITask, String> param) -> updateColor());

		tableTimed.setPlaceholder(new Label(MSG_EMPTY_TABLE));
		tableFloat.setPlaceholder(new Label(MSG_EMPTY_TABLE));

		// Focus Settings
		tableTimed.setFocusTraversable(false);
		tableFloat.setFocusTraversable(false);

		// Command help tips listener
		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				processSyntaxMessage(oldValue, newValue);
			}
		});
	}

	private boolean isValidCmd(String input) {
		return CommandProcessor.getInstance().getEffectiveCmd(input) == null ? false : true;
	}

	private void processSyntaxMessage(String oldValue, String newValue) {
		String oldCommand = EMPTY_STRING;
		String newCommand = EMPTY_STRING;

		if (!oldValue.isEmpty() && oldValue != null) {
			oldCommand = StringUtil.getFirstWord(oldValue);
		}
		if (!newValue.isEmpty() && newValue != null) {
			newCommand = StringUtil.getFirstWord(newValue);
		}

		if (newCommand.equals(null)) {
			return;
		}

		if (newCommand.equals(oldCommand)) {
			return;
		} else {
			if (isValidCmd(newCommand)) {
				CommandProcessor cp = parser.CommandProcessor.getInstance();
				Command commandType = cp.getCmdType(newCommand);
				String syntaxMessage = newCommand + " " + commandType.getHelpInfo();
				setSyntaxMsg(syntaxMessage);
			} else {
				setSyntaxMsg(MSG_EMPTY);
			}
		}
	}

	private TableCell<UITask, String> updateColor() {
		TableCell<UITask, String> cell = new TableCell<UITask, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				String[] stringArr = splitStr(item);
				super.updateItem(item, empty);

				this.setAlignment(Pos.CENTER);
				//this.setFont(Font.font(13));


				if (!isEmpty()) {
					if (stringArr[1].contains("M")) {
						this.setTextFill(Color.valueOf("#bdbdbd"));
					} else if (stringArr[1].contains("L")) {
						this.setTextFill(Color.FORESTGREEN);
					} else if (stringArr[1].contains("H")) {
						this.setTextFill(Color.RED);
					} else if (stringArr[1].contains("N")) {
						this.setTextFill(Color.BLACK);
					}

					if (stringArr[1].contains("O") && !stringArr[1].contains("M")) {
						this.setStyle("-fx-font-weight: bold");
					} else {
						this.setStyle("-fx-font-weight: normal");
					}

					item = stringArr[0];
					setText(item);
				} else {
					setText(item);
					this.setStyle("-fx-font-weight: normal");
					this.setTextFill(Color.BLACK);
				}

			}
		};
		return cell;
	}

	/*
	 * Takes in a string value appended with Flags (see appendFlags(),
	 * return as String array.
	 *
	 * Sample Input: Buy Milk+PMO
	 *
	 * Returns: String Array with [Buy Milk] and [flags] as the data.
	 */
	private String[] splitStr(String item) {
		try {
			String[] toReturn = new String[2];
			int index = item.length() - 1; // points to last index of String

			while (item.charAt(index) != '+') { // locate where the '+' is at
				index--;
			}

			toReturn[0] = item.substring(0, index);
			toReturn[1] = item.substring(index + 1);

			return toReturn;
		} catch (NullPointerException e) {
			return null;
		}
	}

	// Create UI
	static void createUI() {
		if (uI == null) {
			uI = new UI();

			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(uI.getClass());
				}
			}.start();

			while (!uI.isInitialised()) {
				try {
					Thread.sleep(0);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	String getInput() {
		synchronized (inputBuffer) {
			// wait for input from field
			while (inputBuffer.isEmpty()) {
				try {
					inputBuffer.wait();
				} catch (InterruptedException e) {
				}
			}

			return inputBuffer.remove(0);
		}
	}

	void setInput(String str) {
		input.setText(str);
	}

	void clearInput() {
		input.clear();
	}

	void setOutputMsg(String a) {
		// TODO temporary fix for illegalState

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	try {
					cmdMsg.setText(a);
				} catch (NullPointerException e) {
					return;
				}
		    }
		});
	}

	void setTimeDateMsg(String str) {
		try {
			timeDateMsg.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void setSyntaxMsg(String str) {
		try {
			syntaxMsg.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void setDoneCount(String str) {
		try {
			doneCount.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void setPendingCount(String str) {
		try {
			pendingCount.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void setOverdueCount(String str) {
		try {
			overdueCount.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void showUIHelpOverlay() {
		uI.showUIHelpOverlayStage();
	}

	void hideUIHelpOverlay() {
		uI.hideUIHelpOverlayStage();
	}

	static UIController getUIController() {
		uIController = UI.getController();
		return uIController;
	}

	private void generateTable() {

		TimeProcessor tp = TimeProcessor.getInstance();

		dataTimed.clear();
		dataFloat.clear();


		for (Task t : _nonFloatingTaskList) {
			String id = String.valueOf(t.getId());
			String task = t.getName();
			String generatedString = null;

			if (t.getStartTime() == 0) {
				generatedString = tp.getFormattedDate(t.getEndTime());
			} else {
				generatedString = tp.getFormattedDate(t.getStartTime(), t.getEndTime());
			}
			id = appendFlags(id, t);
			task = appendFlags(task, t);
			generatedString = appendFlags(generatedString, t);

			UITask ui1 = new UITask(id, task, generatedString);
			dataTimed.add(ui1);
		}

		for (Task t : _floatingTaskList) {
			String id = String.valueOf(t.getId());
			String task = t.getName();

			id = appendFlags(id, t);
			task = appendFlags(task, t);

			UITask ui2 = new UITask(id, task);
			dataFloat.add(ui2);
		}

		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);
	}

	void seperateTaskList(List<Task> taskList) {

		_nonFloatingTaskList = new ArrayList<Task>();
		_floatingTaskList = new ArrayList<Task>();

		// Iterate through list and remove all floating tasks
		for (int i = 0; i < taskList.size();) {
			if (isFloating(taskList.get(i))) {
				_floatingTaskList.add(taskList.remove(i));
			} else {
				i++;
			}
		}
		// Remaining tasks are all non-floating
		_nonFloatingTaskList = taskList;

		Collections.sort(_nonFloatingTaskList, new taskCollections.comparators.EndTimeComparator());
		Collections.sort(_floatingTaskList, new taskCollections.comparators.PriorityComparator());
		// Collections.sort(_floatingTaskList);

		generateTable();
	}

	private boolean isFloating(Task task) {
		if (task.getEndTime() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Appends Priority: L for low, N for normal, H for high
	 * Appends Marked: M if task is marked.
	 * Appends Overdue: O if task is overdue.
	 */
	private String appendFlags(String toAppend, Task t) {
		char priority = t.getPriority().toString().charAt(0);
		String doneStr = "";

		if (t.getFlag() == FLAG_TYPE.DONE) {
			doneStr = "M"; //signify marked. to refactor
		}
		toAppend = toAppend + "+" + priority + doneStr;

		if (TimeUtil.isBeforeNow(t.getEndTime()) && t.getEndTime() != 0) {
			toAppend += "O"; //overdue
		}

		return toAppend;
	}

	// Event methods
	public void enterPressed() {

		synchronized (inputBuffer) {
			String in = input.getText().trim();
			masterQ.add(in);

			//can put the following into a method
			resetStacks();
			// end method


			inputBuffer.add(in);
			inputBuffer.notify();
		}
		clearInput();
	}

	private void resetStacks() {
		Queue<String> toStack = new LinkedList<String>(masterQ);

		upStack.clear();
		downStack.clear();

		while(!toStack.isEmpty()) {
			upStack.push(toStack.poll());
		}
	}

	public void showHistory(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.UP)) {
			if (!upStack.isEmpty()) {
				String history = upStack.pop();
				downStack.push(history);
				setInput(history);
			}
		} else if (ke.getCode().equals(KeyCode.DOWN)) {
			if (!downStack.isEmpty()) {
				String history = downStack.pop();
				upStack.push(history);
				setInput(history);
			} else {
				setInput("");
			}
		}
	}
}
