package taskCollections.comparators;

import java.util.Comparator;

import taskCollections.Task;

/**
 * Provide {@code tds.Task} a comparator for the ID attribute  
 * 
 * @author amoshydra
 */
public class IdComparator implements Comparator<Task> {
	@Override
	public int compare(Task lhs, Task rhs) {
		return lhs.compareIdTo(rhs);
	}
}