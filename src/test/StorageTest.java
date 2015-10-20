package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import storage.CommandFileHandler;
import storage.TaskFileHandler;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import util.TimeUtil;

public class StorageTest {
	
	static CommandFileHandler cmd;
	static TaskFileHandler task;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cmd = new CommandFileHandler("commands.xml");
		task = new TaskFileHandler("tasks.xml");
	}

	@Test
	public void testHashMap() {
		String mappings = "[add=add, edit=edit, change=edit, show=list, insert=add, "
				+ "*=list, revert=undo, +=add, list=list, delete=delete, -=delete, "
				+ "remove=delete, modify=edit, undo=undo, search=search, find=search]";
		
		HashMap<String, String> hmMappings = cmd.getCmdTable();
		String toCompare = hmMappings.entrySet() + "";
		
		assertEquals(toCompare, mappings);
	}
	
	@Test
	public void testArrayList() {
		ArrayList<Task> taskList = task.retrieveTaskList();
		//t = new Task(id, title, "", startTime, endTime, flag, priority);
		ArrayList<Task> checker = new ArrayList<>();
		
		long t0Start = TimeUtil.getLongTime("Tuesday: 22/09/2015 00:00 GMT+0800");
		long t0End = TimeUtil.getLongTime("Saturday: 26/09/2015 23:59 GMT+0800");
		long t1Start = TimeUtil.getLongTime("Thursday: 01/10/2015 14:00 GMT+0800");
		long t1End = TimeUtil.getLongTime("Thursday: 01/10/2015 15:00 GMT+0800");
		long t2Start = TimeUtil.getLongTime("Tuesday: 01/12/2015 12:00 GMT+0800");
		long t2End = TimeUtil.getLongTime("Tuesday: 01/12/2015 12:05 GMT+0800");
		long t3Start = TimeUtil.getLongTime("Saturday: 31/10/2015 12:00 GMT+0800");
		long t3End = TimeUtil.getLongTime("Saturday: 31/10/2015 20:05 GMT+0800");
		
		checker.add(new Task(0, "Buy Milk from Supermarket", "", t0Start, t0End, 
				FLAG_TYPE.DONE, PRIORITY_TYPE.NORMAL));
		checker.add(new Task(1, "CS2103T Surprise Quiz", "", t1Start, t1End, 
				FLAG_TYPE.NULL, PRIORITY_TYPE.VERY_HIGH));
		checker.add(new Task(2, "Delete the Task Program", "", t2Start, t2End, 
				FLAG_TYPE.NULL, PRIORITY_TYPE.ABOVE_NORMAL));
		checker.add(new Task(3, "Run Around the Campus 10 Times", "", t3Start, t3End, 
				FLAG_TYPE.NULL, PRIORITY_TYPE.VERY_LOW));
		checker.add(new Task(4, "this task", "", 0, 0, FLAG_TYPE.NULL, PRIORITY_TYPE.NORMAL));
		
		System.out.println(taskList.size() + " " + checker.size());
		assertEquals(checker, taskList);
	}
}
