package com.groupstudy.adt;

/**
 * Interface for List ADT - ordered collection with index-based access
 */
public interface ListInterface<T> {
	/**
	 * method to add entry to end of list
	 */
	public void add(T newEntry);
	
	/**
	 * add entry at specified position 
	 */
	public void add(int index, T newEntry);
	
	/**
	 * removes entry at position
	 */
	public T remove(int index);
	
	/**
	 * remove all entries
	 */
	public void clear();
	
	/**
	 * gets entry at position
	 */
	public T get(int index);
	
	/**
	 * replaces entry at position and return old entry 
	 */
	public T set(int index, T newEntry);
	
	/**
	 * checks if list contains entry
	 */
	public boolean contains(T entry);
	
	/**
	 * gets list size
	 */
	public int getLength();
	
	/**
	 * checks if empty
	 */
	public boolean isEmpty();
	
	/**
	 * converts to array
	 */
	public T[] toArray();
	
	
}
