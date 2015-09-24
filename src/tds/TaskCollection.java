/**
 * The {@code TaskCollection} class is an interface that 
 * provides methods for storing and manipulating {@code Task}. 
 *
 * @author amoshydra
 * 
 */
package tds;
import java.util.List;

interface TaskCollection<E> {
	
	public void add(Task task);
	public void remove(Task task);
	public void update(Task task);
	public List<E> searchName(String searchTerm);
	public List<E> searchStartTime(long startTimeSearch);
	public List<E> searchEndTime(long endTimeSearch);
	public List<E> searchFlag(int flagSearch);
	public int size();
}
