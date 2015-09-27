package tds.comparators;

import java.util.Comparator;
import tds.Task;

/**
 * Provide {@code tds.Task} a comparator for the end time attribute
 * 
 * @author amoshydra
 */
public class EndTimeComparator implements Comparator<Task> {
	@Override
	public int compare(Task lhs, Task rhs) {
		return lhs.compareEndTimeTo(rhs);
	}
}