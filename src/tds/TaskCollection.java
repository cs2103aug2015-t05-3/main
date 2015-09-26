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

	/**
	 * Adds the specified element to this collection
	 * 
	 * @param task
	 *            element to be added to this collection
	 * @return true if this set did not already contain the specified element
	 */
	public boolean add(E task);

	/**
	 * Removes the specified element to this collection
	 * 
	 * @param task
	 *            element to be removed from this collection
	 * @return true if this set contained the specified element
	 */
	public boolean remove(E task);

	/**
	 * Replace an old element from this collection with a new element
	 * 
	 * @param oldE
	 *            old element to be replaced from this collection
	 * @param newE
	 *            new element to replace the old element
	 * 
	 * @return true if this collection contained the old element and can be
	 *         replaced by the new element
	 */
	public boolean replace(E oldE, E newE);

	/**
	 * Returns a view of the portion of this collection whose elements contain
	 * the {@code searchTerm}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param searchTerm
	 *            character sequences to be searched.
	 * @return a view of the portion of this collection whose element contain
	 *         the {@code searchTerm}
	 */
	public List<E> searchName(String searchTerm);

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromStartTime}, inclusive, to {@code toStartTime}, inclusive.
	 * (If {@code fromElement} and {@code toElement} are equal, the returned
	 * {@code List} contains element that matches {@code fromEndTime}.) The
	 * returned {@code List} is backed by this collection, so changes in the
	 * returned {@code List} are reflected in this collection, and vice-versa.
	 * 
	 * @param fromStartTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toStartTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from fromStartTime, inclusive, to toStartTime, inclusive
	 */
	public List<E> queryStartTime(long toStartTime, long fromStartTime);

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from fromEndTime, inclusive, to toEndTime, inclusive. (If fromElement and
	 * toElement are equal, the returned {@code List} contains element that
	 * matches fromEndTime only.) The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 * 
	 * @param fromEndTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toEndTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from toEndTime, inclusive, to fromEndTime, inclusive
	 */
	public List<E> queryEndTime(long toEndTime, long fromEndTime);

	/**
	 * Returns a view of the portion of this collection whose elements matches
	 * the {@code flagSearch}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param flagSearch
	 *            flag to be searched.
	 * @return a view of the portion of this collection whose elements match the
	 *         {@code flagSearch}
	 * @see tds.Task
	 * 
	 */
	public List<E> searchFlag(int flagSearch);

	/**
	 * Returns a view of the portion of this collection whose elements matches
	 * the {@code prioritySearch}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param prioritySearch
	 *            priority to be searched.
	 * @return a view of the portion of this collection whose elements match the
	 *         {@code prioritySearch}
	 * @see tds.Task
	 * 
	 */
	public List<E> searchPriority(int prioritySearch);

	/**
	 * Returns a view of this collection whose elements are sorted according to
	 * its specified attribute type. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param taskAttributeType
	 *            attribute type to be sorted with.
	 * @return a view of this collection whose elements are sorted according to
	 *         its specified attribute type.
	 * @see tds.TaskAttributeConstants
	 */
	public List<E> getSortedList(int taskAttributeType);

	/**
	 * Returns the number of elements in this collection (its cardinality).
	 * 
	 * @return the number of elements in this collection (its cardinality)
	 */
	public int size();
}
