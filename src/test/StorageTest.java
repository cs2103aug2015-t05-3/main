//@@author A0076510M
package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import storage.CommandFileHandler;
import storage.TaskFileHandler;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import util.TimeUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StorageTest {

	static CommandFileHandler cmd;
	static TaskFileHandler taskFH;
	
	final static String COMMAND_FILE = "commands.xml";
	
	final static String ORIGINAL_TASK_FILE = "tasksTest.xml";
	final static String BACKUP_TASK_FILE = "tasksOriginal.xml";
	final static String ADD_TASK_FILE = "tasksAdd.xml";
	final static String UPDATE_TASK_FILE = "tasksUpdate.xml";
	final static String DELETE_TASK_FILE = "tasksDelete.xml";
	

	@BeforeClass
	public static void beforeClass() throws Exception {
		cmd = new CommandFileHandler();
		taskFH = new TaskFileHandler();
	}
	
	@Test
	public void testCommandFileLoading() {
		String fileName = COMMAND_FILE;
		assertEquals(cmd.loadCommandFile(fileName), true);
	}

	@Test
	public void testHashMap() {
		String mappings = "[add=add, edit=edit, change=edit, show=list, insert=add, "
				+ "*=list, revert=undo, +=add, list=list, delete=delete, -=delete, "
				+ "remove=delete, modify=edit, undo=undo, search=search, find=search]";

		HashMap<String, String> hmMappings = cmd.getCmdTable();
		String toCompare = hmMappings.entrySet().toString();

		assertEquals(toCompare, mappings);
	}

	@Test
	public void testAcceptTaskFile() {
		
		String backupFile = BACKUP_TASK_FILE;
		String fileToUse = ORIGINAL_TASK_FILE;
		
		fileCopy(backupFile, fileToUse);
		assertEquals(taskFH.loadTaskFile(fileToUse), true);
	}
	
	@Test
	public void testArrayList() {
		ArrayList<Task> taskList = taskFH.retrieveTaskList();
		// t = new Task(id, title, "", startTime, endTime, flag, priority);
		ArrayList<Task> checker = new ArrayList<>();

		long t0Start = TimeUtil.getLongTime("Tuesday: 22/09/15 00:00 GMT+0800");
		long t0End = TimeUtil.getLongTime("Saturday: 26/09/15 23:59 GMT+0800");
		long t1Start = TimeUtil.getLongTime("Thursday: 01/10/15 14:00 GMT+0800");
		long t1End = TimeUtil.getLongTime("Thursday: 01/10/15 15:00 GMT+0800");
		long t2Start = TimeUtil.getLongTime("Tuesday: 01/12/15 12:00 GMT+0800");
		long t2End = TimeUtil.getLongTime("Tuesday: 01/12/15 12:05 GMT+0800");
		long t3Start = TimeUtil.getLongTime("Saturday: 31/10/15 12:00 GMT+0800");
		long t3End = TimeUtil.getLongTime("Saturday: 31/10/15 20:05 GMT+0800");

		checker.add(new Task(0, "Buy Milk from Supermarket", t0Start, t0End, FLAG_TYPE.DONE, PRIORITY_TYPE.NORMAL));
		checker.add(new Task(1, "CS2103T Surprise Quiz", t1Start, t1End, FLAG_TYPE.NULL, PRIORITY_TYPE.HIGH));
		checker.add(
				new Task(2, "Delete the Task Program", t2Start, t2End, FLAG_TYPE.NULL, PRIORITY_TYPE.NORMAL));
		checker.add(new Task(3, "Run Around the Campus 10 Times", t3Start, t3End, FLAG_TYPE.NULL,
				PRIORITY_TYPE.LOW));
		checker.add(new Task(4, "this task", 0, 0, FLAG_TYPE.NULL, PRIORITY_TYPE.NORMAL));
		assertEquals(checker, taskList);
	}

	@Test
	public void testAtAdd() {
		Task t = new Task(5, "New Task Added for JUnit Testing", 0, 0, FLAG_TYPE.DONE, PRIORITY_TYPE.NORMAL);
		taskFH.add(t);
		assertEquals(getCheckSum(ADD_TASK_FILE), getCheckSum(ORIGINAL_TASK_FILE));
	}


	@Test
	public void testUpdateAndDelete() {
		long t3Start = TimeUtil.getLongTime("Saturday: 31/10/15 12:00 GMT+0800");
		long t3End = TimeUtil.getLongTime("Saturday: 31/10/15 20:05 GMT+0800");
		Task t = new Task(3, "Run Around the Campus 100000 Times", t3Start, t3End, FLAG_TYPE.NULL,
				PRIORITY_TYPE.LOW);
		taskFH.update(t);
		assertEquals(getCheckSum(UPDATE_TASK_FILE), getCheckSum(ORIGINAL_TASK_FILE));
		
		taskFH.delete(4);
		assertEquals(getCheckSum(DELETE_TASK_FILE), getCheckSum(ORIGINAL_TASK_FILE));
	}	
	//@@author A0076510M-reused
	public String getCheckSum(String file) {
		
		StringBuffer sb1 = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
			byte[] mdbytes = md.digest();
			sb1 = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb1.toString();
	}
	
	public void fileCopy(String toCopy, String newFileStr) {
		File newFile = new File(newFileStr);
		newFile.delete();

		File file = new File(toCopy);
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			inputChannel = fis.getChannel();
			FileOutputStream fos = new FileOutputStream(newFile);
			outputChannel = fos.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			inputChannel.close();
			outputChannel.close();
			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
