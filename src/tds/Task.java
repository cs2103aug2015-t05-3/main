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

	private String name;
	private long startTime;
	private long endTime;
	private int flag;

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time and the flag as the argument.
	 */
	public Task(String name, long startTime, long endTime, int flag) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
	}

	/**
	 * Initializes a newly created {@code Task} object which contain only the
	 * name or description of the task. The value of starting time, ending time
	 * and flag are set to null or zero.
	 */
	public Task(String name) {
		this(name, DATE_NULL, DATE_NULL, FLAG_NULL);
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

}