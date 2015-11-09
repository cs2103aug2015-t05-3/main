//@@author A0076510M
package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import logic.command.Command;
import parser.CommandProcessor;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import util.StringUtil;
import util.TimeUtil;

//@@author A0126394B
public class UIController implements Initializable {

	// Message string constants
	private static final String MSG_CMD_WELCOME = "Welcome! Loading your tasks...";
	private static final String MSG_PENDING_HELLO = "Hello %s,";
	private static final String MSG_EMPTY = "";
	private static final String MSG_EMPTY_TABLE = "Nothing here";
	private static final String MSG_COUNT_OVERDUE = "Overdue [ %s ]";
	private static final String MSG_COUNT_PENDING = "Pending [ %s ]";
	private static final String MSG_COUNT_DONE = "Done [ %s ]";
	private static final String EMPTY_TIME_DATE = "Getting time...";

//@@author A0076510M
	// Utility string constants
	private static final String EMPTY_STRING = "";
	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

//@@author A0126394B
	// CSS node constants
	private static final String CSS_PRIORITY_HIGH = "highPriority";
	private static final String CSS_PRIORITY_LOW = "lowPriority";
	private static final String CSS_FLAG_DONE = "done";
	private static final String CSS_OVERDUE = "overdue";

	// FXML constants
	@FXML
	private Label pendingMsg;
	@FXML
	private Label timeDateMsg;
	@FXML
	private Label cmdMsg;
	@FXML
	private Label syntaxMsg;
	@FXML
	private Label tableFloatHeader;
	@FXML
	private Label tableTimedHeader;
	@FXML
	private Label overdueCount;
	@FXML
	private Label pendingCount;
	@FXML
	private Label doneCount;
//@@author A0076510M
	@FXML
	private TableColumn<UITask, Integer> idTimed;
	@FXML
	private TableColumn<UITask, String> taskTimed;
	@FXML
	private TableColumn<UITask, String> sDate;
	@FXML
	private TableColumn<UITask, Integer> idFloat;
	@FXML
	private TableColumn<UITask, String> taskFloat;
	@FXML
	private TableView<UITask> tableTimed;
	@FXML
	private TableView<UITask> tableFloat;
	@FXML
	private TextField input;
	@FXML
	private AnchorPane anchorPane;

	// UI Controller attributes
//@@author A0126394B
	private static UI uI;
	private static UIController uIController;
	private List<Task> floatingTaskList;
	private List<Task> nonFloatingTaskList;
	private ArrayList<String> inputBuffer = new ArrayList<>();
	ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	ObservableList<UITask> dataFloat = FXCollections.observableArrayList();

//@@author A0076510M
	private LinkedList<String> leftList;
	private LinkedList<String> rightList;
	private String cmdHistoryBuffer;

	public UIController() {
		cmdHistoryBuffer = EMPTY_STRING;
		leftList = new LinkedList<String>();
		rightList = new LinkedList<String>();
	}

//@@author A0126394B
	static UIController getUIController() {
		uIController = UI.getController();
		return uIController;
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Label fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");

		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));

		// Initialize and reset UI Label fields
		timeDateMsg.setText(EMPTY_TIME_DATE);
		overdueCount.setText(EMPTY_STRING);
		pendingCount.setText(EMPTY_STRING);
		doneCount.setText(EMPTY_STRING);

		// Table
//@@author A0076510M
		idTimed.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(VAR_TABLE_STRING_ID));
		taskTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		sDate.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_SDATE));
		tableTimed.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {
			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {

					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);
						manageTableRows(this);
					}
				};
				return row;
			}
		});

		idFloat.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		tableFloat.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {

			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {
					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);
						manageTableRows(this);
					}
				};
				return row;
			}
		});

//@@author A0126394B
		tableTimed.setPlaceholder(new Label(MSG_EMPTY_TABLE));
		tableFloat.setPlaceholder(new Label(MSG_EMPTY_TABLE));

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

//@@author A0076510M
	private void manageTableRows(TableRow<UITask> tableRow) {
		updateRowsColor(tableRow);
	}

	private void updateRowsColor(TableRow<UITask> tableRow) {
		if (!tableRow.isEmpty()) {

			UITask uITask = tableRow.getItem();
			PRIORITY_TYPE priorityCheck = uITask.getPriority();
			FLAG_TYPE flagCheck = uITask.getFlag();

			// Clear all custom css element before adding new style to it
			removeAllCSSElement(tableRow);

			// Check flag
			if (flagCheck == FLAG_TYPE.DONE) {
				tableRow.getStyleClass().add(CSS_FLAG_DONE);
			} else {
				if (priorityCheck == PRIORITY_TYPE.HIGH) {
					tableRow.getStyleClass().add(CSS_PRIORITY_HIGH);
				} else if (priorityCheck == PRIORITY_TYPE.LOW) {
					tableRow.getStyleClass().add(CSS_PRIORITY_LOW);
				}
			}

			// Check overdue
			long endTime = uITask.getEndTime();
			if (isOverdue(endTime)) {
				tableRow.getStyleClass().add(CSS_OVERDUE);
			}
		} else {
			removeAllCSSElement(tableRow);
		}
	}

	private boolean isOverdue(long time) {
		boolean isFloating = time != Task.DATE_NULL;
		boolean isBeforeNow = TimeUtil.isBeforeNow(time);

		return isBeforeNow && isFloating;
	}

	private void removeAllCSSElement(TableRow<UITask> tableRow) {
		tableRow.getStyleClass().remove(CSS_PRIORITY_HIGH);
		tableRow.getStyleClass().remove(CSS_PRIORITY_LOW);
		tableRow.getStyleClass().remove(CSS_FLAG_DONE);
		tableRow.getStyleClass().remove(CSS_OVERDUE);
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

//@@author A0126394B
			while (!uI.isInitialised()) {
				try {
					Thread.sleep(0);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

//@@author A0076510M
	String getInput() {
		synchronized (inputBuffer) {
			// wait for input from field
			while (inputBuffer.isEmpty()) {
				try {
					inputBuffer.wait();
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
			return inputBuffer.remove(0);
		}
	}

//@@author A0126394B
	void setInput(String str) {
		input.setText(str);
		input.positionCaret(str.length());
	}

	void clearInput() {
		input.clear();
	}

	void setOutputMsg(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cmdMsg.setText(str);
			}
		});
	}

	void setTimeDateMsg(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timeDateMsg.setText(str);
			}
		});
	}

	void setSyntaxMsg(String str) {
		syntaxMsg.setText(str);
	}

	void setDoneCount(int count) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String str = String.format(MSG_COUNT_DONE, count);
				doneCount.setText(str);
			}
		});
	}

	void setPendingCount(int count) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String str = String.format(MSG_COUNT_PENDING, count);
				pendingCount.setText(str);
			}
		});
	}

	void setOverdueCount(int count) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String str = String.format(MSG_COUNT_OVERDUE, count);
				overdueCount.setText(str);
			}
		});
	}

	void showUIHelpOverlay() {
		uI.showUIHelpOverlayStage();
	}

	void hideUIHelpOverlay() {
		uI.hideUIHelpOverlayStage();
	}

//@@author A0076510M
	/**
	 * Receive a list of task and display them on the UI Tables. Floating and
	 * non-floating task will be separated and sorted in its respective table.
	 *
	 * @param taskList
	 *            to be displayed on the UI table
	 */
	void generateTablesOutput(List<Task> taskList) {
		separateTaskList(taskList);
		sortSeparatedList();
		displayTables();
	}

	private void separateTaskList(List<Task> taskList) {
		floatingTaskList = new ArrayList<Task>();
		nonFloatingTaskList = new ArrayList<Task>();

		for (Task task : taskList) {
			if (isFloating(task)) {
				floatingTaskList.add(task);
			} else {
				nonFloatingTaskList.add(task);
			}
		}
	}

	private boolean isFloating(Task task) {
		return task.getEndTime() == Task.DATE_NULL;
	}

	private void sortSeparatedList() {
		Collections.sort(floatingTaskList, new taskCollections.comparators.PriorityComparator());
		Collections.sort(nonFloatingTaskList, new taskCollections.comparators.TimeComparator());
	}

	private void displayTables() {
		displayTable(tableTimed, dataTimed, nonFloatingTaskList);
		displayTable(tableFloat, dataFloat, floatingTaskList);
	}

	private void displayTable(TableView<UITask> tableView, ObservableList<UITask> dataList, List<Task> taskList) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				dataList.clear();
				for (Task t : taskList) {
					dataList.add(new UITask(t));
				}
				tableView.setItems(dataList);
			}
		});
	}

	// Event methods
	public void enterPressed() {

		synchronized (inputBuffer) {
			String in = input.getText().trim();

			addToLists(in);

			inputBuffer.add(in);
			inputBuffer.notify();
		}
		clearInput();
	}

	private void addToLists(String in) {

		if (!in.isEmpty()) {
			if (!cmdHistoryBuffer.equals(EMPTY_STRING)) {
				leftList.offerLast(cmdHistoryBuffer);
				cmdHistoryBuffer = EMPTY_STRING;
			}

			while (!rightList.isEmpty()) {
				leftList.offerLast(rightList.removeFirst());
			}
			leftList.offerLast(in);
		}
	}

	public void showHistory(KeyEvent keyEvent) {

		switch (keyEvent.getCode()) {
		case UP:
			if (!cmdHistoryBuffer.equals(EMPTY_STRING) && !leftList.isEmpty()) {
				rightList.offerFirst(cmdHistoryBuffer);
				cmdHistoryBuffer = EMPTY_STRING;
			}

			if (!leftList.isEmpty()) {
				String history;
				history = leftList.pollLast();
				cmdHistoryBuffer = history;
				setInput(history);
			}
			keyEvent.consume();
			break;
		case DOWN:
			if (!cmdHistoryBuffer.equals(EMPTY_STRING)) {
				leftList.offerLast(cmdHistoryBuffer);
				cmdHistoryBuffer = EMPTY_STRING;
			}

			if (!rightList.isEmpty()) {
				String history;
				history = rightList.pollFirst();
				cmdHistoryBuffer = history;
				// _leftList.offerLast(history);
				setInput(history);
			} else {
				setInput(EMPTY_STRING);
			}
			keyEvent.consume();
			break;
		default:
			break;
		}
	}

//@@author A0126394B
	private void processSyntaxMessage(String oldValue, String newValue) {
		String oldCommand = EMPTY_STRING;
		String newCommand = EMPTY_STRING;

		if (!oldValue.isEmpty() && oldValue != null) {
			oldCommand = StringUtil.getFirstWord(oldValue);
		}
		if (!newValue.isEmpty() && newValue != null) {
			newCommand = StringUtil.getFirstWord(newValue);
		}

		if (newCommand == null) {
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

	private boolean isValidCmd(String input) {
		return CommandProcessor.getInstance().getEffectiveCmd(input) == null ? false : true;
	}

}
