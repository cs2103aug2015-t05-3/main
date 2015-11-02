package taskCollections.comparators;

import java.util.Comparator;

import taskCollections.Task;

/**
 * Provide {@code tds.Task} a comparator for the name attribute  
 * 
 * @author amoshydra
 */
public class NameComparator implements Comparator<Task> {
	@Override
    public int compare(Task lhs, Task rhs) {
        return lhs.compareNameTo(rhs);
    }
}