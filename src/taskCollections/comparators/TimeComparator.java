//@@author A0126394B
package taskCollections.comparators;

import java.util.Comparator;

import taskCollections.Task;

/**
 * Provide {@code tds.Task} a comparator for the end time attribute
 *
 * @author amoshydra
 */
public class TimeComparator implements Comparator<Task> {
	@Override
	public int compare(Task lhs, Task rhs) {
		return lhs.compareTimeTo(rhs);
	}
}