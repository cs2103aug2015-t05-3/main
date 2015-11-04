package taskCollections;

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
public final class Attributes {

	/**
	 * Attribute type constants used for attributes identification
	 */
	public enum TYPE {
		NAME(0), START_TIME(1), END_TIME(2), FLAG(3), PRIORITY(4), ID(5);

		private final int value;

		private TYPE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		private static final Map<Integer, TYPE> lookup = new HashMap<Integer, TYPE>();

		static {
			for (TYPE t : EnumSet.allOf(TYPE.class))
				lookup.put(t.getValue(), t);
		}

		static TYPE get(int value) {
			return lookup.get(value);
		}
	};

	final static int NUM_OF_ATTRIBUTES = 6;
}
