package tds.comparators;

import java.util.Comparator;
import tds.Task;

/**
 * Provide {@code tds.Task} a comparator for the priority attribute  
 * 
 * @author amoshydra
 */
public class PriorityComparator implements Comparator<Task> {
	@Override
    public int compare(Task lhs, Task rhs) {
		return lhs.comparePriorityTo(rhs);
    }
}