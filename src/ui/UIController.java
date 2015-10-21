package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import constants.UIFieldIndex;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
import javafx.scene.text.Text;
import parser.CommandProcessor;
import taskCollections.Task;
import util.TimeUtil;

public class UIController implements Initializable {

	/*
	 * Tutorial video from https://www.youtube.com/watch?v=dF48KdNH1g4
	 */

	@FXML
	private static Text cmdMsg;
	@FXML
	private TableColumn<UITask1, String> id1;
	@FXML
	private TableColumn<UITask1, String> task1;
	@FXML
	private TableColumn<UITask1, String> sDate;
	@FXML
	private TableColumn<UITask1, String> eDate;
	@FXML
	private TableColumn<UITask2, String> id2;
	@FXML
	private TableColumn<UITask2, String> task2;
	@FXML
	private TableColumn<UITask2, String> priority;
	@FXML
	private TableView<UITask1> table1 = new TableView<UITask1>();
	@FXML
	private TableView<UITask2> table2 = new TableView<UITask2>();
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

	private int troll = 100;

	private static List<Task> _floatingTaskList;
	private static List<Task> _nonFloatingTaskList;

	private static UI ui;
	//private static Semaphore lock;
	private static ArrayList<String[]> inputBuffer = new ArrayList<>();

	final static ObservableList<UITask1> data = FXCollections.observableArrayList();
	final static ObservableList<UITask2> data1 = FXCollections.observableArrayList();
	
	private static final double WIDTH_EXPANDED = 4080;
	private static final double WIDTH_SHRINKED = 250;

	public void enterPressed() {
		//String in = input.getText().trim();
		
		synchronized (inputBuffer) {
            String[] in = new String[UIFieldIndex.INPUT_BUFFSIZE];
            // TODO: Convert to array
            in[0] = input.getText().trim();
            in[1] = input2.getText().trim();
            in[2] = input3.getText().trim();
            in[3] = input4.getText().trim();
            in[4] = input5.getText().trim();
            		
			inputBuffer.add(in);
            inputBuffer.notify();
        }
		
		// Other classes will do the job.
		clearInput();
	}
	
	public static String[] getInput() {
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

	public void add() {
		UITask1 t = new UITask1(troll--, "Buy Milk from Shop", 1442851200000L, 1443283140000L);
		data.add(t);
		
		UITask2 t1 = new UITask2(troll--, "Go Die in a fire", "N");
		data1.add(t1);
		table1.getSortOrder().add(id1);

		clearInput();
	}

	public void delete(int id) {
		data.remove(id);
		clearInput();
	}

	public static void createUI() {
		if (ui == null) {
			ui = new UI();
			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(UI.class);
				}
				//ui.run();
			}.start();
			//inputBuffer = new ArrayList<>();
			//lock = new Semaphore(1);
		}
	}
	
	public void clearInput(){
		input.clear();
		input2.clear();
		input3.clear();
		input4.clear();
		input5.clear();
	}
	
	public static void seperateTaskList(List<Task> taskList){
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
		
		genTable();
	}

	private static boolean isFloating(Task task){
		if(task.getEndTime() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	private static void genTable() {
		data.clear();
		data1.clear();
		
		for (Task t : _nonFloatingTaskList) {
			UITask1 ui1 = new UITask1(t.getId(), t.getName(), t.getStartTime(), t.getEndTime());
			data.add(ui1);
		}
		
		for (Task t : _floatingTaskList) {
			//TODO: RESOLVE PRIORITY
			UITask2 ui2 = new UITask2(t.getId(), t.getName(), "N");
			data1.add(ui2);
		}
	}
	
	public static void setOutputMsg(String a){
		//cmdMsg.setText(a);
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {				
				
		id1.setCellValueFactory(new PropertyValueFactory<UITask1, String>("id"));
		task1.setCellValueFactory(new PropertyValueFactory<UITask1, String>("Task"));
		sDate.setCellValueFactory(new PropertyValueFactory<UITask1, String>("sDate"));
		eDate.setCellValueFactory(new PropertyValueFactory<UITask1, String>("eDate"));
		
		id2.setCellValueFactory(new PropertyValueFactory<UITask2, String>("id"));
		task2.setCellValueFactory(new PropertyValueFactory<UITask2, String>("Task"));
		priority.setCellValueFactory(new PropertyValueFactory<UITask2, String>("priority"));

		id1.setSortType(TableColumn.SortType.ASCENDING);
		
		table1.setItems(data);
		table2.setItems(data1);
		
		table1.getSortOrder().add(id1);
		
		/*input2.focusedProperty().addListener(new ChangeListener<Boolean>() {
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
					input.setPrefWidth();
				}
			}
		});*/

		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (isValidCmd(input.getText().trim())) {
					shrinkCmdCell();
				} else {
					expandCmdCell();
				}
			}
		});
	}
	
	private void expandCmdCell(){
		input.setPrefWidth(WIDTH_EXPANDED);
		hideRedundantCells();
	}
	
	private void shrinkCmdCell(){
		input.setPrefWidth(WIDTH_SHRINKED);
		showRedundantCells();
	}
	
	private void hideRedundantCells(){
		input2.setVisible(false);
		input3.setVisible(false);
		input4.setVisible(false);
		input5.setVisible(false);
	}
	
	private void showRedundantCells(){
		input2.setVisible(true);
		input3.setVisible(true);
		input4.setVisible(true);
		input5.setVisible(true);
	}
	
	private boolean isValidCmd(String input){
		return CommandProcessor.getInstance().getEffectiveCmd(input) == null ? false : true;
	}

	// To Implement Priority
	public static class UITask2 {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty priority;
		
		private UITask2(int id, String task, String priority) {
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
	
	public static class UITask1 {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty task;
		private final SimpleStringProperty sDate;
		private final SimpleStringProperty eDate;
		private final SimpleLongProperty eDateLong;

		private UITask1(int id, String task, long sDate, long eDate) {
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
}
