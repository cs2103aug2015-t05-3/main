/**
 * The {@code TaskCollection} class is an abstract class that 
 * provides methods for storing and manipulating {@code Task}. 
 *
 * @author amoshydra
 * 
 */
package tds;

abstract class TaskCollection {
	
	private int size;
	
	public abstract void add(Task task);
	public abstract void remove(Task task);
	public abstract void update(Task task);
	public abstract Task search(String searchTerm);	
	public int size() {
		return this.size;
	}
}
