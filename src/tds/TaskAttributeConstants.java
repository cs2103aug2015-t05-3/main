package tds;

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

	public final static int NAME = 0;
	public final static String NAME_TYPE_STRING = "0";

	public final static int START_TIME = 1;

	public final static int END_TIME = 2;

	public final static int FLAG = 3;

	public final static int PRIORITY = 4;

	public final static int ID = 5;

	public final static int NUM_OF_ATTRIBUTES = 6;
}
