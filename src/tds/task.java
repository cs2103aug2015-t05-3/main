/**
 * A task object used to store task name, time and different attributes.  
 *
 * @author amoshydra
 */
package tds;
import java.util.Date;

public class task {
	private String name;
	private Date startTime;
	private Date endTime;
	private int flag;

	/**
     * Initializes a newly created {@code Task} object so that it store
     * the name, starting time, ending time and the flag as the argument.
     */	
	public task(String name, Date startTime, Date endTime, int flag) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
	}
	
	/**
     * Initializes a newly created {@code Task} object so that it store
     * the name, starting time, ending time and the flag as the argument. 
     * This constructor will accept both starting time and ending time in 
     * {@code long} format.  
     */	
	public task(String name, long startTimeSecond, long endTimeSecond, int flag) {
		this(
			name, 
			new Date(startTimeSecond),
			new Date(endTimeSecond),
			flag
		);
	}
	
	/**
     * Initializes a newly created {@code Task} object which contain only
     * the name or description of the task. The value of starting time, ending time and
     * flag are set to null or zero.
     */	
	public task(String name) {
		this(name, null, null, 0);
	}
}