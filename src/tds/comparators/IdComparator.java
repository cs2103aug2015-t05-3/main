package tds.comparators;

import java.util.Comparator;

import tds.Task;

public class IdComparator implements Comparator<Task> {
	@Override
    public int compare(Task lhs, Task rhs) {
		return lhs.compareIdTo(rhs);
    }
}