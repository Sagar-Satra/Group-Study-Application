package com.groupstudy.implementation;

import java.util.Arrays;

import com.groupstudy.adt.QueueInterface;

public class QueueImplementation<T> implements QueueInterface<T> {

	// using array implementation
	private T[] queue;
	private int size;
	private boolean arrayInitialized = false;
	
	private static final int DEFAULT_CAPACITY = 20;
	private static final int MAXIMUM_CAPACITY = 1000;
	
	public QueueImplementation() {
		this(DEFAULT_CAPACITY);
	}
	
	public QueueImplementation(int capacity) {
		if (capacity < 1 || capacity > MAXIMUM_CAPACITY) {
            throw new IllegalStateException("Capacity must be between 1 and " + MAXIMUM_CAPACITY);
        }
        
        @SuppressWarnings("unchecked")
        T[] temp = (T[]) new Object[capacity];
        queue = temp;
        size = 0;
        arrayInitialized = true;
	}
	
	@Override
	public void enqueue(T newEntry) {
		if (newEntry == null) {
			throw new IllegalArgumentException("Cannot enqueue null");
		}
		checkInitialization();
		if (size>= queue.length) {
			increaseCapacity();
		}
		
		queue[size] = newEntry;
		size++;
	}

	@Override
	public T dequeue() {
		checkInitialization();
		if(isEmpty()) {
			throw new IllegalStateException("Queue is empty.");
		}
		// get the first element
		T front = queue[0];
		// now shift all elements left
		for (int i=0; i < size-1; i++) {
			queue[i] = queue[i+1];
		}
		// put null at the last element
		queue[size-1] = null;
		size--;
		return front;
	}

	@Override
	public T getFront() {
		checkInitialization();
		if(isEmpty()) {
			throw new IllegalStateException("Queue is empty.");
		}
		return queue[0];
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public void clear() {
		checkInitialization();
		for(int i = 0; i < size; i++) {
			queue[i] = null;
		}
		size = 0;
	}

	@Override
	public int getSize() {
		return size;
	}
	
	// copying into new arrays to avoid null entries in the output
	@Override
    public String toString() {
        checkInitialization();
        T[] activeEntries = Arrays.copyOf(queue, size);
        return Arrays.toString(activeEntries);
    }
	
	// private methods to support above implementation
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
		int newSize = queue.length + 20;
		// check if the newSize overshoots max capacity
		if (newSize > MAXIMUM_CAPACITY) {
			throw new IllegalStateException("The new capacity is exceedding the max capacity of " + MAXIMUM_CAPACITY + ". Hence, cannot add next new element");
		}
		// if new size is within bounds, copy the items of old array into new sized array and assign it back
		queue = Arrays.copyOf(queue, newSize);
	}

}
