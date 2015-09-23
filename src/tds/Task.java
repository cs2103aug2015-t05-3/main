/**
 * A task object used to store task name, time and different attributes.  
 *
 * @author amoshydra
 */
package tds;

import java.util.Date;

public class Task {
	public final static int FLAG_NULL = 0;
	public final static int FLAG_DONE = 1;

	private String name;
	private Date startTime;
	private Date endTime;
	private int flag;

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time and the flag as the argument.
	 */
	public Task(String name, Date startTime, Date endTime, int flag) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
	}

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time and the flag as the argument. This
	 * constructor will accept both starting time and ending time in
	 * {@code long} format.
	 */
	public Task(String name, long startTimeSecond, long endTimeSecond, int flag) {
		this(name, 
			 new Date(startTimeSecond), 
			 new Date(endTimeSecond), 
			 flag
		);
	}

	/**
	 * Initializes a newly created {@code Task} object which contain only the
	 * name or description of the task. The value of starting time, ending time
	 * and flag are set to null or zero.
	 */
	public Task(String name) {
		this(name, null, null, FLAG_NULL);
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
	 * Returns the start time of this task in {@code Date} object.
	 * 
	 * @return the start time of this task.
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time of this task in {@code Date} object.
	 * 
	 * @return the end time of this task.
	 */
	public Date getEndTime() {
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
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Change the end time of this task.
	 * 
	 * @param endTime
	 *			the new end time for the task.
	 */
	public void setEndTime(Date endTime) {
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