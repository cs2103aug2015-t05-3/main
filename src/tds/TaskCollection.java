/**
 * The {@code TaskCollection} class is an interface that 
 * provides methods for storing and manipulating {@code Task}. 
 *
 * @author amoshydra
 * 
 */
package tds;
import java.util.Collection;
import java.util.List;

interface TaskCollection<E> {
	
	public void addAll(Collection<E> collection);
	public void add(Task task);
	public void remove(Task task);
	public void replace(Task taskOld, Task taskNew);
	public List<E> searchName(String searchTerm);
	public List<E> searchStartTime(long startTimeSearch);
	public List<E> searchEndTime(long endTimeSearch);
	public List<E> searchFlag(int flagSearch);
	public List<E> sortName();
	public List<E> sortStartTime();
	public List<E> sortEndTime();
	public List<E> sortFlag();
	public int size();
}
