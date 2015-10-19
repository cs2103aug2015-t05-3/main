package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	@FXML
	private Text cmdMsg;

	@FXML
	private TableColumn<Task, String> id;
	@FXML
	private TableColumn<Task, String> task;
	@FXML
	private TableColumn<Task, String> sDate;
	@FXML
	private TableColumn<Task, String> eDate;
	@FXML
	private TableView<Task> table1 = new TableView<Task>();
	@FXML
	private TableView<Task> table2 = new TableView<Task>();

	@FXML
	private TextField input = new TextField();
	@FXML
	private TextField input2 = new TextField();
	@FXML
	private TextField input3 = new TextField();
	@FXML
	private TextField input4 = new TextField();
	@FXML
	private TextField input5 = new TextField();

	private int troll = 6;

	final ObservableList<Task> data = FXCollections.observableArrayList(
			new Task(1, "Buy snake", "not funny", "not funny"), new Task(2, "Buy milk", "not funny", "not funny"),
			new Task(3, "Buy dog", "not funny", "not funny"), new Task(4, "Buy cat", "not funny", "not funny"),
			new Task(5, "Buy Min Oo", "not funny", "not funny"));

	public void enterPressed(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			cmdMsg.setText("You have entered: \"" + input.getText().trim() + "\"");
			add();
			// delete(3);
		}
	}

	public void add() {
		Task t = new Task(troll++, "Buy shit", "funny", "funny");
		data.add(t);

		// clearform
		input.clear();
	}

	public void delete(int id) {
		data.remove(id);

		// clearform
		input.clear();
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		id.setCellValueFactory(new PropertyValueFactory<Task, String>("id"));
		task.setCellValueFactory(new PropertyValueFactory<Task, String>("Task"));
		sDate.setCellValueFactory(new PropertyValueFactory<Task, String>("sDate"));
		eDate.setCellValueFactory(new PropertyValueFactory<Task, String>("eDate"));
		table1.setItems(data);

		input2.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("Textfield on focus");
					input.setPrefWidth(250);
				}
			}
		});
		input.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("Textfield out focus");
					input.setPrefWidth(4080);
				}
			}
		});

		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.equals("add")) {
					System.out.println("Roar");
				}
			}
		});
	}

	public static class Task {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty sDate;
		private final SimpleStringProperty eDate;

		private Task(int id, String task, String sDate, String eDate) {
			this.id = new SimpleIntegerProperty(id);
			this.task = new SimpleStringProperty(task);
			this.sDate = new SimpleStringProperty(sDate);
			this.eDate = new SimpleStringProperty(eDate);
		}

		public int getId() {
			return id.get();
		}

		public void setId(int value) {
			id.set(value);
		}

		public String getTask() {
			return task.get();
		}

		public void setTask(String value) {
			task.set(value);
		}

		public String getSDate() {
			return sDate.get();
		}

		public void setSDate(String value) {
			sDate.set(value);
		}

		public String getEDate() {
			return eDate.get();
		}

		public void setEDate(String value) {
			eDate.set(value);
		}
	}
}
