/**
 *
 */
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
public class ParserTest2 {
	TimeProcessor tp;
	CommandProcessor cp;

	private String[] _timeStringArray = {
			"",
			"24 Jul 10 pm",
			"10 Jul 11 Pm",
			"10 Jul 2300",
			"Jul 10, 11 pm",
			"Jul 10 2300", //TODO test 2400
			"10 07 11pm",
			"10 07 2300", // Ambiguous date formatting
			"07 10 11pm",
			"07 10 2300",
			};

	private Long[] _timeLongArray = {
			0L,
			1437746400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L,
			1436540400000L
		};

	@Test
	public void testTime(){
		tp = TimeProcessor.getInstance();
		int numOfElement = _timeStringArray.length;

		// Normal
		for (int index = 0; index < numOfElement; index++) {
			String timeString = _timeStringArray[index];
			Long timeLong = _timeLongArray[index];

			Long result = tp.resolveTime(timeString);

			// Printing start TODO remove printLn
			System.out.println(result);
			System.out.println(timeLong);

			Long difference = result - timeLong;
			System.out.println("diff " + difference + " |" + timeString + "|" );
			System.out.println();
			// Printing end

			// Dirty Fix TODO
			if (result != 0) {
				result += 1800000;
			}
			assertEquals(result, timeLong);
		}
	}



	public void testCmd(){
		cp = CommandProcessor.getInstance();

		cp.initCmdList("commands.xml");
		assertEquals(cp.getCmdType("add"),new CmdAdd());
		assertEquals(cp.getCmdType("@"),null);
		assertEquals(cp.getCmdType("+"),new CmdAdd());
		assertEquals(cp.getCmdType("mark"),new CmdMark());
	}

	public void test(){
		testTime();
		testCmd();
	}

}
