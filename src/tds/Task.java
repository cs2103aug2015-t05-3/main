/**
 * A task object used to store task name, time and different attributes.  
 *
 * @author amoshydra
 */
package tds;

public class Task {
	public final static int FLAG_NULL = 0;
	public final static int FLAG_DONE = 1;

	public final static int DATE_NULL = 0;

	public final static int PRIORITY_VERY_HIGH = 0;
	public final static int PRIORITY_HIGH = 1;
	public final static int PRIORITY_ABOVE_NORMAL = 2;
	public final static int PRIORITY_NORMAL = 3;
	public final static int PRIORITY_BELOW_NORMAL = 4;
	public final static int PRIORITY_LOW = 5;
	public final static int PRIORITY_VERY_LOW = 6;
	
	public final static int GET_VALUE_INVALID = -1;
	public final static Object GET_VALUE_NULL = null;
	public final static String GET_VALUE_NAME = "1";
	public final static long GET_VALUE_START_TIME = 2;
	public final static long GET_VALUE_END_TIME = 3;
	public final static int GET_VALUE_FLAG = 4;
	public final static int GET_VALUE_PRIORITY = 5;
	
	private String name;
	private long startTime;
	private long endTime;
	private int flag;
	private int priority;

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument.
	 */
	public Task(String name, long startTime, long endTime, int flag, int priority) {
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
	 */
	public Task(String name) {
		this(name, DATE_NULL, DATE_NULL, FLAG_NULL, PRIORITY_NORMAL);
	}

	/**
	 * Returns the value of the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public long getValue(long option) {
		if (option == GET_VALUE_START_TIME) {
			return getStartTime();
		} else if (option == GET_VALUE_END_TIME) {
			return getEndTime();
		} else {
			return GET_VALUE_INVALID;
		}
	}
	
	/**
	 * Returns the value of the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public String getValue(String option) {
		if (option.equals(GET_VALUE_NAME)) {
			return getName();
		} else {
			return (String) GET_VALUE_NULL;
		}
	}
	
	/**
	 * Returns the value of the given option field.
	 * 
	 * @return the value of the given option field.
	 */
	public int getValue(int option) {
		if (option == GET_VALUE_FLAG) {
			return getFlag();
		} else if (option == GET_VALUE_PRIORITY) {
			return getPriority();
		} else {
			return GET_VALUE_INVALID;
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
	 *			the new name or description for the task.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Change the start time of this task.
	 * 
	 * @param startTime
	 *			the new start time for the task.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Change the end time of this task.
	 * 
	 * @param endTime
	 *			the new end time for the task.
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Change the flag of this task.
	 * 
	 * @param flag
	 *			the new flag for the task.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * Change the priority of this task.
	 * 
	 * @param priority
	 *			the new priority for the task.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * Determines whether or not two task are equal. 
	 * The two tasks are equal if the values name, start time,
	 * end time, flag and priority are equal.
	 * 
	 * @param obj
	 * 		an object to be compared with this {@code Task}
	 * 
	 * @return
	 * 		{@code true} if the object to be compared is an 
	 * 		instance of Task and has the same values; false 
	 * 		otherwise.
	 */
    public boolean equals(Object obj) {
       if (!(obj instanceof Task))
            return false;
        if (obj == this)
            return true;

        Task rhs = (Task) obj;
        return (name.equals(rhs.name)) &&
            (startTime == rhs.startTime) &&
            (endTime == rhs.endTime) &&
            (flag == rhs.flag) && 
            (priority == rhs.priority);
    }
    
	/**
	 * Compares this {@code Task} instance with another lexicographically and
	 * numerically. The value returned is determined by the first difference in
	 * value returned by:
	 * 
	 * <pre>
	 * {@code this.compareNameTo(rhs)}
	 * {@code this.compareStartTimeTo(rhs)}
	 * {@code this.compareEndTimeTo(rhs)}
	 * {@code this.compareFlagTo(rhs)}
	 * {@code this.comparePriorityTo(rhs)}
	 * </pre>
	 * 
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * 
	 * @return the value 0 if this {@code Task} is equal to the argument
	 *         {@code Task}; a value less than 0 if this {@code Task} is
	 *         lexicographically or numerically less than the argument
	 *         {@code Task}; and a value greater than 0 if this {@code Task} is
	 *         lexicographically or numerically greater than the argument
	 *         {@code Task}.
	 */
	public int compareTo(Task rhs) {

		if (this.name.equals(rhs.name)) {
			if (this.startTime == rhs.startTime) {
				if (this.endTime == rhs.endTime) {
					if (this.flag == rhs.flag) {
						return comparePriorityTo(rhs);
					} else {
						return compareFlagTo(rhs);
					}
				} else {
					return compareEndTimeTo(rhs);
				}
			} else {
				return compareStartTimeTo(rhs);
			}
		} else {
			return compareNameTo(rhs);
		}
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
		return this.name.compareTo(rhs.name);
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
		return startTimeLongThis.compareTo(startTimeLongRhs);
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
		return endTimeLongThis.compareTo(endTimeLongRhs);
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
		return this.flag - rhs.flag;
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
		return this.priority - rhs.priority;
	}
}