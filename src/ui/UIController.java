package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import parser.TimeProcessor;
import taskCollections.Task;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello %s,";
	private static final String MSG_EMPTY = "";

	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

	@FXML
	private Text pendingMsg;
	@FXML
	private Text tableFloatHeader;
	@FXML
	private Text tableTimedHeader;
	@FXML
	private Text timeDateMsg;
	@FXML
	private Text cmdMsg;
	@FXML
	private Text syntaxMsg;
	@FXML
	private TableColumn<UITask, String> idTimed;
	@FXML
	private TableColumn<UITask, String> taskTimed;
	@FXML
	private TableColumn<UITask, String> sDate;
	@FXML
	private TableColumn<UITask, String> idFloat;
	@FXML
	private TableColumn<UITask, String> taskFloat;
	@FXML
	private TableView<UITask> tableTimed;
	@FXML
	private TableView<UITask> tableFloat;
	@FXML
	private TextField input;

	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;

	private static UI ui;

	private ArrayList<String> inputBuffer = new ArrayList<>();

	final ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	final ObservableList<UITask> dataFloat = FXCollections.observableArrayList();

	public UIController() {
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Text fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");
		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));
		syntaxMsg.setText(MSG_EMPTY);

		// Table
		idTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		sDate.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_SDATE));

		taskTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));

		tableTimed.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {
			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {
					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);

						try {
							String text = this.getItem().getTask();

							if (text != null) {
								// System.out.println(this.getItem().getTask());
								if (this.getItem().getIsDone()) {
									getStyleClass().add("marked");
								} else {
									switch (this.getItem().getPriority()) {
									case "HIGH":
										getStyleClass().add("highPriority");
										break;
									case "LOW":
										getStyleClass().add("lowPriority");
										break;
									}
									
									
								}
							}
						} catch (NullPointerException e) {
							System.err.println("");
						}
					}
				};
				return row;
			}
		});

		idFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));

		tableFloat.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {
			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {
					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);

						try {
							String text = this.getItem().getTask();

							if (text != null) {
								// System.out.println(this.getItem().getTask());
								if (this.getItem().getTask().contains("cs2010")) {
									getStyleClass().add("fancytext");
								} else {
									getStyleClass().add("fancytext1");
								}
							}
						} catch (NullPointerException e) {
							System.err.println("");
						}
					}
				};

				return row;
			}
		});

		// idTimed.setSortType(TableColumn.SortType.ASCENDING);

		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);

		// tableTimed.getSortOrder().add(idTimed);

		// Focus Settings
		tableTimed.setFocusTraversable(false);
		tableFloat.setFocusTraversable(false);
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

	void setOutputMsg(String str) {
		try {
			cmdMsg.setText(str);
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
			
			UITask ui1 = new UITask(t.getId(), t.getName(), generatedString, 
					String.valueOf(t.getPriority()),String.valueOf(t.getFlag()));
			dataTimed.add(ui1);
		}

		for (Task t : _floatingTaskList) {
			UITask ui2 = new UITask(t.getId(), t.getName(), 
					String.valueOf(t.getPriority()), String.valueOf(t.getFlag()));
			dataFloat.add(ui2);
		}
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

		// Other classes will do the job.
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
