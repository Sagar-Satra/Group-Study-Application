package com.groupstudy.adt;


/*
 * an interface that describe operations of a bag of objects
 */
public interface BagInterface<T> {

    /**
     * checks if bag array is empty.
     * 
     * @return True if bag is empty or false if not
     */
	public boolean isEmpty();
	
	
	/**
	 * checks if bag array is full
	 * 
	 * @return true if the bag is full otherwise false
	 */
	public boolean isBagFull();
	
	
	/**
	 * gets the current number of items in the bag 
	 * 
	 * @return an integer number representing items in the bag
	 */
	public int getCurrentSize();
	
	
	/**
	 * adds an item to the bag array
	 * 
	 * @return true if the add operation is successful otherwise false
	 */
	public boolean add(T newItem);
	
	
	/**
	 *  checks if an item exists in the array bag
	 *  
	 * @param item the item to search for in the array bag
	 * 
	 * @return returns true if specific item is found, otherwise false
	 */
	public boolean contains(T item);
	
	
	/**
	 * method to remove a particular item
	 * 
	 * @param item the item to be removed from bag array
	 * @return return true if element is found and removed, otherwise false
	 */
	public boolean removeItem(T item);
	
	
	/**
	 * method to remove all the items from the array bag
	 */
	public void removeAllItems();
	
	/**
	 * counts how many times an entry appears in a bag
	 */
	public int getFrequencyOf(T item);
	
    /**  
     * retrieves all entries that are in the bag array
     * 
     * @return a newly allocated array of all the entries in the bag
     * If the bag is empty, the returned array is empty
     * 
     */
	public T[] toArray();
	
}

