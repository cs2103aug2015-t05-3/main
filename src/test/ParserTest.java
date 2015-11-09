//@@author A0125496X
package test;

import parser.CommandProcessor;
import parser.TimeProcessor;
import static org.junit.Assert.*;

import logic.command.*;

import org.junit.Test;

/**
 * @author Yan Chan Min Oo
 *
 */
public class ParserTest {
	/*
	 * Constants
	 */
	private static final String CMD_FILENAME = "commands.xml";
	/*
	 * Variables
	 */
	TimeProcessor tp;
	CommandProcessor cp;

	private String[] _timeStringArray = {
			"",
			"0", // Zero/Date removal test
			"24 Jul 10 pm",
			"10 Jul 11 Pm",
			"10 Jul 2300",
			"Jul 10, 11 pm",
			"Jul 10 2300",
			"10 07 11pm",
			"10 07 2300", // 10 Jul 2300
			"07 10 11pm", // 7 Oct 11pm
			"07 10 2300", // 7 Oct 11pm
			"08/12/16 1pm",
			"7 10 16 720", // Padding test
			"07 10 16", // No time test
			"07 10 16 2400" // Time range test
			};

	private Long[] _timeLongArray = {
			-1L,
			0L,
			1437746400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1444230000000L,
			1444230000000L,
			1481173200000L,
			1475796000000L,
			-1L,
			-1L
		};

	@Test
	public void testTime(){
		tp = TimeProcessor.getInstance();
		int numOfElement = _timeStringArray.length;

		for (int index = 0; index < numOfElement; index++) {
			String timeString = _timeStringArray[index];
			Long timeLong = _timeLongArray[index];

			Long result = tp.resolveTime(timeString);
			
			assertEquals(result, timeLong);
		}
	}

	@Test
	public void testCmd(){
		cp = CommandProcessor.getInstance();

		cp.initCmdList(CMD_FILENAME);
		assertTrue(cp.getCmdType("add") instanceof CmdAdd);
		assertEquals(cp.getCmdType("@"),null);
		assertTrue(cp.getCmdType("+") instanceof CmdAdd);
		assertTrue(cp.getCmdType("mark") instanceof CmdMark);
	}

	@Test
	public void test(){
		testTime();
		testCmd();
	}

}
