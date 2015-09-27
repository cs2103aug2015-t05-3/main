package tds;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A list of constants used to identify a specify attribute type. These
 * constants are used for differentiating {@code treeType} for
 * {@code TaskCollection} and {@code TaskTree}.
 * 
 * @see TaskTree
 * @see Task
 * 
 * @author amoshydra 
 */
public final class TaskAttributeConstants {

	public enum TREE_TYPE {
		NAME(0), START_TIME(1), END_TIME(2), FLAG(3), PRIORITY(4), ID(5);

		private final int value;
		
		private TREE_TYPE(int value) {
			this.value = value;
		}

		int getValue() {
			return value;
		}
		
		private static final Map<Integer, TREE_TYPE> lookup = new HashMap<Integer, TREE_TYPE>();

		static {
			for (TREE_TYPE t : EnumSet.allOf(TREE_TYPE.class))
				lookup.put(t.getValue(), t);
		}

		static TREE_TYPE get(int value) {
			return lookup.get(value);
		}
	};
	
	
	final static int NAME = 0;
	final static String NAME_TYPE_STRING = "0";

	final static int START_TIME = 1;

	final static int END_TIME = 2;

	final static int FLAG = 3;

	final static int PRIORITY = 4;

	final static int ID = 5;

	final static int NUM_OF_ATTRIBUTES = 6;
}
