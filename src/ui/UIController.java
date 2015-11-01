package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.command.Command;
import parser.CommandProcessor;
import parser.TimeProcessor;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import util.StringUtil;
import util.TimeUtil;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello %s,";
	private static final String MSG_EMPTY = "";

	private static final String EMPTY_STRING = "";

	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

	//TODO added
	@FXML private Text pendingMsg;
	@FXML private Text tableFloatHeader;
	@FXML private Text tableTimedHeader;
	@FXML private Text timeDateMsg;
	@FXML private Text cmdMsg;
	@FXML private Text syntaxMsg;
	@FXML private TableColumn<UITask, String> idTimed;
	@FXML private TableColumn<UITask, String> taskTimed;
	@FXML private TableColumn<UITask, String> sDate;
	@FXML private TableColumn<UITask, String> idFloat;
	@FXML private TableColumn<UITask, String> taskFloat;
	@FXML private TableView<UITask> tableTimed;
	@FXML private TableView<UITask> tableFloat;
	@FXML private TextField input;

	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;

	private static UI ui;

	private ArrayList<String> inputBuffer = new ArrayList<>();

	ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	ObservableList<UITask> dataFloat = FXCollections.observableArrayList();

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Text fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");

		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));

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

		/*
		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);
		*/

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

	//TODO added
	private void processSyntaxMessage(String oldValue, String newValue) {
		String oldCommand = EMPTY_STRING;
		String newCommand = EMPTY_STRING;

		if (!oldValue.isEmpty() || oldValue != null) {
			oldCommand = StringUtil.getFirstWord(oldValue);
		}
		if (!newValue.isEmpty() || newValue != null) {
			newCommand = StringUtil.getFirstWord(newValue);
		}

		if (newCommand.equals(oldCommand)) {
			return;
		} else {
			if (isValidCmd(newCommand)) {
				CommandProcessor cp = parser.CommandProcessor.getInstance();
				Command commandType = cp.getCmdType(newCommand);
				String syntaxMessage = newCommand + " " + commandType.getHelpInfo();
				syntaxMsg(syntaxMessage);
			} else {
				syntaxMsg(MSG_EMPTY);
			}
		}
	}

	public UIController() {
	}

	private TableCell<UITask, String> updateColor() {
		TableCell<UITask, String> cell = new TableCell<UITask, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				String[] stringArr = splitStr(item);
				super.updateItem(item, empty);

				if (!isEmpty()) {
					if (stringArr[1].contains("M")) {
						this.setTextFill(Color.valueOf("#bdbdbd"));
					} else if (stringArr[1].contains("L")) {
						this.setTextFill(Color.LAWNGREEN);
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
		if (ui == null) {
			ui = new UI();

			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(ui.getClass());
				}
			}.start();

			while (!ui.isInitialised()) {
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

	void setOutputMsg(String a) {
		try {
			cmdMsg.setText(a);
		} catch (NullPointerException e) {
			return;
		}
	}

	void timeDateMsg(String str) {
		try {
			timeDateMsg.setText(str);
		} catch (NullPointerException e) {
			return;
		}
	}

	void syntaxMsg(String str) {
		try {
			syntaxMsg.setText(str);
		} catch (NullPointerException e) {
			return;
		}
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

	void clearInput() {
		input.clear();
	}

	// Event methods
	public void enterPressed() {

		synchronized (inputBuffer) {
			String in = input.getText().trim();

			inputBuffer.add(in);
			inputBuffer.notify();
		}
		clearInput();
	}

	/*
	 * // Debugging code public void add() { UITaskTimed t = new
	 * UITaskTimed(debugTestingIndex--, "Buy Milk from Shop", 1442851200000L,
	 * 1443283140000L); dataTimed.add(t);
	 *
	 * UITaskFloat t1 = new UITaskFloat(debugTestingIndex--, "Go Die in a fire",
	 * "N"); dataFloat.add(t1); tableTimed.getSortOrder().add(idTimed);
	 *
	 * clearInput(); }
	 */
	/*
	 * public void delete(int id) { dataTimed.remove(id); clearInput(); }
	 */
}
