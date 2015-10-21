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
public class ParserTest {
	TimeProcessor tp;
	CommandProcessor cp;
	
	public void testTime(){
		tp = TimeProcessor.getInstance();
		
		tp.resolveTime("24 Jul 10 pm");
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
