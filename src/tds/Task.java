/**
 * A task object used to store task name, time and different attributes.  
 *
 * @author amoshydra
 */
package tds;

public class Task implements Comparable<Task> {
	private static int taskNumber = 1;

	/**
	 * Field value for flag attribute indicating an unmarked flag.
	 */
	public final static int FLAG_NULL = 0;
	/**
	 * Field value for flag attribute indicating a marked done flag.
	 */
	public final static int FLAG_DONE = 1;

	/**
	 * Field value for start time or end time attribute indicating an empty
	 * date.
	 */
	public final static int DATE_NULL = 0;

	/**
	 * Field value for priority attribute indicating very high priority.
	 */
	public final static int PRIORITY_VERY_HIGH = 0;
	/**
	 * Field value for priority attribute indicating high priority.
	 */
	public final static int PRIORITY_HIGH = 1;
	/**
	 * Field value for priority attribute indicating above normal priority.
	 */
	public final static int PRIORITY_ABOVE_NORMAL = 2;
	/**
	 * Field value for priority attribute indicating normal priority.
	 */
	public final static int PRIORITY_NORMAL = 3;
	/**
	 * Field value for priority attribute indicating below normal priority.
	 */
	public final static int PRIORITY_BELOW_NORMAL = 4;
	/**
	 * Field value for priority attribute indicating low priority.
	 */
	public final static int PRIORITY_LOW = 5;
	/**
	 * Field value for priority attribute indicating very low priority.
	 */
	public final static int PRIORITY_VERY_LOW = 6;

	/**
	 * Field option for {@code getValue} to retrieve the ID of a {@code Task}.
	 */
	public final static long GET_VALUE_ID = TaskAttributeConstants.ID;
	/**
	 * Field option for {@code getValue} to retrieve the start time of a
	 * {@code Task}.
	 */
	public final static long GET_VALUE_START_TIME = TaskAttributeConstants.START_TIME;
	/**
	 * Field option for {@code getValue} to retrieve the end time of a
	 * {@code Task}.
	 */
	public final static long GET_VALUE_END_TIME = TaskAttributeConstants.END_TIME;
	/**
	 * Field option for {@code getValue} to retrieve the flag of a {@code Task}.
	 */
	public final static int GET_VALUE_FLAG = TaskAttributeConstants.FLAG;
	/**
	 * Field option for {@code getValue} to retrieve the priority of a
	 * {@code Task}.
	 */
	public final static int GET_VALUE_PRIORITY = TaskAttributeConstants.PRIORITY;
	/**
	 * Field option for {@code getValue} to retrieve the name or description of
	 * a {@code Task}.
	 */
	public final static String GET_VALUE_NAME = TaskAttributeConstants.NAME_TYPE_STRING;

	private final static int RETURN_VALUE_INVALID = -1;
	private final static Object RETURN_VALUE_NULL = null;
	private final static String TO_STRING_DELIMETER = "|";

	private int id;
	private String name;
	private long startTime;
	private long endTime;
	private int flag;
	private int priority;

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument. An
	 * internal ID will be used to differentiate duplicated value.
	 * 
	 * @param name
	 *            the name or description of the newly constructed {@code Task}
	 * @param startTime
	 *            the starting time of the newly constructed {@code Task} in
	 *            UNIX format
	 * @param endTime
	 *            the ending time of the newly constructed {@code Task} in UNIX
	 *            format
	 * @param flag
	 *            the given flag field; Task marked as done is specified with
	 *            this flag.
	 * @param priority
	 *            the given priority field;
	 * 
	 */
	public Task(String name, long startTime, long endTime, int flag, int priority) {
		this.id = taskNumber++;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
		this.priority = priority;
	}

	/**
	 * Initializes a newly created {@code Task} object which contain only the
	 * name or description of the task. The value of starting time, ending time
	 * and flag are set to null or zero. Priority will be initialized to normal.
	 * 
	 * @param name
	 *            the name or description of the newly constructed {@code Task}
	 */
	public Task(String name) {
		this(name, DATE_NULL, DATE_NULL, FLAG_NULL, PRIORITY_NORMAL);
	}

	/**
	 * Returns the value of the given option field.
	 * 
	 * @param option
	 *            the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public long getValue(long option) {
		if (option == GET_VALUE_START_TIME) {
			return getStartTime();
		} else if (option == GET_VALUE_END_TIME) {
			return getEndTime();
		} else {
			return RETURN_VALUE_INVALID;
		}
	}

	/**
	 * Returns the value of the given option field.
	 * 
	 * @param option
	 *            the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public String getValue(String option) {
		if (option.equals(GET_VALUE_NAME)) {
			return getName();
		} else {
			return (String) RETURN_VALUE_NULL;
		}
	}

	/**
	 * Returns the value of the given option field.
	 * 
	 * @param option
	 *            the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public int getValue(int option) {
		if (option == GET_VALUE_FLAG) {
			return getFlag();
		} else if (option == GET_VALUE_PRIORITY) {
			return getPriority();
		} else {
			return RETURN_VALUE_INVALID;
		}
	}

	/**
	 * Returns the name or description of this task in {@code String}.
	 * 
	 * @return the name or description of this task.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the id of this task in {@code long}.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns the start time of this task in {@code long}.
	 * 
	 * @return the start time of this task.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time of this task in {@code long}.
	 * 
	 * @return the end time of this task.
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Returns the flag of this task in {@code int}.
	 * 
	 * @return the flag of this task.
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * Returns the priority of this task in {@code int}.
	 * 
	 * @return the priority of this task.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Change the name or description of this task.
	 * 
	 * @param name
	 *            the new name or description for the task.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Change the start time of this task.
	 * 
	 * @param startTime
	 *            the new start time for the task.
	 */
	void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Change the end time of this task.
	 * 
	 * @param endTime
	 *            the new end time for the task.
	 */
	void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Change the flag of this task.
	 * 
	 * @param flag
	 *            the new flag for the task.
	 */
	void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * Change the priority of this task.
	 * 
	 * @param priority
	 *            the new priority for the task.
	 */
	void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return a hash code value for this object enumerated using the ID of this
	 *         {@code Task}.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * Determines whether or not two task are equal. The two tasks are equal if
	 * the values name, start time, end time, flag and priority are equal.
	 * 
	 * @return {@code true} if the object to be compared is an instance of Task
	 *         and has the same attributes; false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Task))
			return false;
		if (obj == this)
			return true;

		Task rhs = (Task) obj;
		return (name.equals(rhs.name)) && (startTime == rhs.startTime) && (endTime == rhs.endTime) && (flag == rhs.flag)
				&& (priority == rhs.priority);
	}

	/**
	 * Compares this {@code Task} instance with another based on the order they
	 * are created.
	 * 
	 * @return the value 0 if this {@code Task} is equal to the argument
	 *         {@code Task}; a value less than 0 if this {@code Task} is created
	 *         earlier than the argument {@code Task}; and a value greater than
	 *         0 if this {@code Task} is created later than the argument
	 *         {@code Task}.
	 */
	@Override
	public int compareTo(Task rhs) {
		return this.compareIdTo(rhs);
	}

	/**
	 * Compares this {@code Task} instance with another to generate an array of
	 * check bits.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return a boolean array with all element as {@code true} if the
	 *         {@code Task} is identical to the other; A difference in
	 *         attributes between two task will cause a boolean in the array to
	 *         become false.
	 */
	public boolean[] getAttributesDiff(Task rhs) {
		boolean[] checkBits = new boolean[TaskAttributeConstants.NUM_OF_ATTRIBUTES];

		checkBits[TaskAttributeConstants.NAME] = (this.name.equals(rhs.name));
		checkBits[TaskAttributeConstants.START_TIME] = (this.startTime - rhs.startTime == 0);
		checkBits[TaskAttributeConstants.END_TIME] = (this.endTime - rhs.endTime == 0);
		checkBits[TaskAttributeConstants.FLAG] = (this.flag - rhs.flag == 0);
		checkBits[TaskAttributeConstants.PRIORITY] = (this.priority - rhs.priority == 0);
		checkBits[TaskAttributeConstants.ID] = (this.compareIdTo(rhs) == 0);

		return checkBits;
	}

	/**
	 * Compares the name of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the name of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this name is
	 *         lexicographically less than the argument name; a value greater
	 *         than 0 if this name is lexicographically greater than the
	 *         argument name.
	 */
	public int compareNameTo(Task rhs) {
		int result = this.name.compareTo(rhs.name);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the starting time of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the starting time of this {@code Task} is equal to
	 *         the argument {@code Task}; a value less than 0 if this starting
	 *         time is numerically less than the argument starting time; a value
	 *         greater than 0 this starting time is numerically greater than the
	 *         argument starting time.
	 */
	public int compareStartTimeTo(Task rhs) {
		Long startTimeLongThis = new Long(this.startTime);
		Long startTimeLongRhs = new Long(rhs.startTime);
		int result = startTimeLongThis.compareTo(startTimeLongRhs);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the ending time of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the ending time of this {@code Task} is equal to
	 *         the argument {@code Task}; a value less than 0 if this ending
	 *         time is numerically less than the argument ending time; a value
	 *         greater than 0 this ending time is numerically greater than the
	 *         argument ending time.
	 */
	public int compareEndTimeTo(Task rhs) {
		Long endTimeLongThis = new Long(this.endTime);
		Long endTimeLongRhs = new Long(rhs.endTime);
		int result = endTimeLongThis.compareTo(endTimeLongRhs);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the flag of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the flag of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this flag time is
	 *         numerically less than the argument flag; a value greater than 0
	 *         this flag is numerically greater than the argument flag.
	 */
	public int compareFlagTo(Task rhs) {
		int result = this.flag - rhs.flag;
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the priority of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the priority of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this priority time
	 *         is numerically less than the argument priority; a value greater
	 *         than 0 this priority is numerically greater than the argument
	 *         priority.
	 */
	public int comparePriorityTo(Task rhs) {
		int result = this.priority - rhs.priority;
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the ID of this {@code Task} instance with another.
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if the ID of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this ID is
	 *         numerically less than the argument ID; a value greater than 0
	 *         this ID is numerically greater than the argument ID.
	 */
	public int compareIdTo(Task rhs) {
		return (this.id - rhs.id);
	}

	/**
	 * Allow comparator to differentiate two duplicated attributes via its ID of
	 * creation
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * @param result
	 *            result obtained from previous comparison
	 * 
	 * @return the original result if it is already different. Otherwise, the
	 *         difference in ID is returned
	 */
	private static int handleDuplicatedAttributes(Task lhs, Task rhs, int result) {
		if (result == 0) {
			return lhs.compareIdTo(rhs);
		} else {
			return result;
		}
	}

	/**
	 * Represent this {@code Task} into a {@code String} format
	 * 
	 * @return a string representation of this task in the format such as:
	 * 
	 *         <pre>
	 *         name | startTime | endTime | flag | priority
	 *         </pre>
	 */
	@Override
	public String toString() {
		return "" + name + TO_STRING_DELIMETER + startTime + TO_STRING_DELIMETER + endTime + TO_STRING_DELIMETER + flag
				+ TO_STRING_DELIMETER + priority;
	}
}