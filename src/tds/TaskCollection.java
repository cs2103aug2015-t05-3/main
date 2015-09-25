/**
 * The {@code TaskCollection} class is an interface that 
 * provides methods for storing and manipulating {@code Task}. 
 *
 * @author amoshydra
 * 
 */
package tds;
import java.util.Comparator;
import java.util.List;

interface TaskCollection<E> {
	
	public void add(E task);
	public E remove(E task);
	public E replace(E taskOld, E taskNew);
	public List<E> searchName(String searchTerm);
	public List<E> queryStartTime(long startTimeUpperBound, long startTimeLowerBound);
	public List<E> queryEndTime(long endTimeUpperBound, long endTimeLowerBound);
	public List<E> searchFlag(int flagSearch);
	public List<E> searchPriority(int prioritySearch);
	public List<E> getSortedList(Comparator<E> comparator);
	public int size();
}
