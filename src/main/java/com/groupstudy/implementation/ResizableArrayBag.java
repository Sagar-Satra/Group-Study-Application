package com.groupstudy.implementation;

import java.util.Arrays;

import com.groupstudy.adt.BagInterface;

public class ResizableArrayBag<T> implements BagInterface<T> {

	//private fields to store state of the objects
	private T[] myBag;
	private int numOfEntries;
	
	// safety check
	private boolean arrayInitialized = false;
	
	// default capacity of the array, will increase as per pokemon unlocked
	// given constraints as per problem
	private static final int DEFAULT_CAPACITY = 27;
	private static final int MAXIMUM_CAPACITY = 1000;
	
	public ResizableArrayBag() {
		this(DEFAULT_CAPACITY);
	}
	
	public ResizableArrayBag(int capacity) {
		// capacity should be positive and less than equal to max capacity 
		if ( (0 < capacity) && (capacity <= MAXIMUM_CAPACITY)) {
			// to suppress warnings for unchecked casting concept
			@SuppressWarnings("unchecked")
			T[] tempBag = (T[]) new Object[capacity];
			myBag = tempBag;
			// initial number of entries will be 0
			numOfEntries = 0;
			// marking the object as initialized
			arrayInitialized = true;
		} else {
			throw new IllegalStateException("The capacity should be between 1 and the maximum capacity of 1000.");
		}
	}
	
	/**
	 * checks if myBag array is empty
	 * 
	 * @return True if bag is empty or false if not
	 */
	@Override
	public boolean isEmpty() {
		return numOfEntries == 0;
	}

	/**
	 * checks if myBag array is full
	 * 
	 * @return true if the bag is full otherwise false
	 */
	@Override
	public boolean isBagFull() {
		return numOfEntries >= myBag.length;
	}

	/**
	 * gets the current number of items in the myBag array 
	 * 
	 * @return an integer number representing items in myBag
	 */
	@Override
	public int getCurrentSize() {
		return numOfEntries;
	}

	/**
	 * adds an item to the myBag array. If the myBag reaches its desired capacity, 
	 * the size is gradually increased in steps of 5 upto MAXIMUM_CAPACITY of 50
	 * if the capacity goes beyond MAXIMUM_CAPACITY = 50, an exception is thrown and add operation returns false
	 * 
	 * @return true if the add operation is successful otherwise false
	 */
	@Override
	public boolean add(T newItem) {
		// performing security check
		checkInitialization();
		// cannot add null item
		if (newItem == null) {
			return false;
		}
		// checking if bag is full
		if (isBagFull()) {
			// if full, increase capacity upto 50
			try {
				increaseCapacity();
			} catch( Exception e) {
				// if capacity goes beyond 50, an exception is thrown and message is printed
				System.out.println(e.getMessage());
				return false;
			}
		}
		// add the new item at the last index
		myBag[numOfEntries] = newItem;
		// increment the index
		numOfEntries++;
		return true;
	}

	/**
	 *  checks if an item exists in the myBag
	 *  
	 * @param item the item to search for in the myBag
	 * 
	 * @return returns true if specified item is found, otherwise false
	 */
	@Override
	public boolean contains(T item) {
		// cannot search for null
		if (item == null) return false;
		// security check
		checkInitialization();
		// calling helper method to find index of specified item
		int indx = getIndexOfItem(item);
		// if index == -1, item not found in myBag array hence return false, otherwise return true
		return indx > -1;
	}
	

	/**
	 * method to remove a particular item from myBag
	 * 
	 * @param item the item to be removed from bag array
	 * @return return true if element is found and removed, otherwise false 
	 */
	@Override
	public boolean removeItem(T item) {
		// cannot remove null
		if (item == null) return false;
		// security check
		checkInitialization();
		// calling helper method to find the index of specified item
		int itemIdx = getIndexOfItem(item);
		// if index==-1, item not found in myBag array, return false
		if (itemIdx > -1) {
			// if found, remove the item and check if correct item is removed
			T removedEntry = removeAnEntry(itemIdx);
			return item.equals(removedEntry);
		}
		return false;
	}

	/**
	 * method to remove all the items from the myBag
	 */
	@Override
	public void removeAllItems() {
		// security check
		checkInitialization();
		// if myBag is not empty, keep on removing all items 
		while(!isEmpty()) {
			removeAnEntry(numOfEntries - 1);
		}
	}
	
	/**
	 * 
	 * returns the count of a particular item in the bag
	 */
	@Override
	public int getFrequencyOf(T item) {
		if (item == null) throw new IllegalArgumentException("Null object cannot be counted");
		int count = 0;
		for (int i= 0; i < numOfEntries; i++) {
			if (item.equals(myBag[i])) {
				count++;
			}
		}
		return count;
	}

    /**  
     * retrieves all entries that are in the myBag array
     * 
     * @return a newly allocated array of all the entries in the bag
     * if the bag is empty, the returned array is empty
     */
	@Override
	public T[] toArray() {
		// security check
		checkInitialization();
		// allocate a new array and copy the items to return a new array
		@SuppressWarnings("unchecked")
		T[] temp = (T[]) new Object[numOfEntries];
		for (int idx = 0; idx < numOfEntries; idx++) {
			temp[idx] = myBag[idx];
		}
		return temp;
	}
	
	/**
	 * toString() method to return string representation of myBag array
	 */
	@Override
	public String toString() {
		return "myBag = [" + Arrays.toString(myBag) + "]";
	}
	
	//======== other helper methods =================
	
	/**
	 * method for security check to ensure proper MyResizableBag object initialization
	 */
	private void checkInitialization() {
		// if arrayInitialized is false, throw error
		if (!arrayInitialized) {
			throw new SecurityException("MyResizableBag object has not been initialied properly.");
		}
	}
	
	/**
	 * helper method to increase the array capacity dynamically
	 * 
	 */
	private void increaseCapacity() {
		// increase the size of array by 15
		int newSize = myBag.length + 15;
		// check if the newSize overshoots max capacity
		if (newSize > MAXIMUM_CAPACITY) {
			throw new IllegalStateException("The new capacity is exceedding the max capacity of " + MAXIMUM_CAPACITY + ". Hence, cannot add next new element. Capacity kept at 50.");
		}
		// if new size is within bounds, copy the items of old array into new array and assign it to myBag reference variable
		myBag = Arrays.copyOf(myBag, newSize);
	}
	
	/**
	 * helper method to find the index of the given item
	 * 
	 * @param item  the item for which corresponding index needs to be returned
	 * @return index of the given item
	 */
	private int getIndexOfItem(T item) {
		int indx = -1;
		// cannot find index of null
		if (item == null) return indx;
		// looping through myBag array to find the item and return the index
		for (int i = 0; i < numOfEntries; i++) {
			if (item.equals(myBag[i])) {
				indx = i;
				// index found, break the loop
				break;
			}
		}
		return indx;
	}
	
	
	/**
	 * helper method to remove the item at a particular index
	 * if itemIdx is less than 0 or myBag is empty, return null. Otherwise return the removed item
	 * 
	 * @param itemIdx the index at which an item needs to be removed
	 * @return the removed item or null
	 */
	private T removeAnEntry(int itemIdx) {
		T removedEntry = null;
		// checking if index is >= 0 and myBag array is not empty
		if (!isEmpty() && (itemIdx > -1)) {
			// save the item
			removedEntry = myBag[itemIdx];
			// swap the last item in the array at the given index position
			myBag[itemIdx] = myBag[numOfEntries - 1];
			// assigning the last item as null
			myBag[numOfEntries - 1] = null;
			// decrementing the count of items in the myBag array
			numOfEntries--;
		}
		return removedEntry;
	}
	
	
	

}
