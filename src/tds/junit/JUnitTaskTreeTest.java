package tds.junit;
import org.junit.Test;

import tds.Task;

public class JUnitTaskTreeTest {
	private static final int FIRST_ELEMENT = 0;
	private static final int LAST_ELEMENT = 4;
	private static final int NUM_OF_ITEMS = 5;
	private static final int FIVE_SECONDS = 5000;
	private String[] names = {
			"Buy mushroom", 
			"Buy milk", 
			"Buy hotdog", 
			"Do homework",
			"Learn swimming"
			};
	private long[] startTimes = {
			0L,
			0L,
			0L,
			0L,
			0L
			};
	private long[] endTimes = {
			0L,			// floating task
			1441119600000L,	// 01 Sep 2015 15:00:00
			1442734205000L, // 20 Sep 2015 07:30:05
			1443112320000L, // 24 Sep 2015 16:32:00
			1444059228000L	// 05 Oct 2015 15:33:48
			};
	private int[] flags = {
			Task.FLAG_DONE,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL
			};
	private int[] priorities = {
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_HIGH,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_VERY_HIGH,
			Task.PRIORITY_NORMAL
			};

	@Test
	public void testAddElementsToTree () {
		
	}

}
