package com.groupstudy.implementation;

import java.util.Arrays;

import com.groupstudy.adt.ListInterface;

public class ArrayListImplementation<T> implements ListInterface<T> {

	private T[] list;
	private int numberOfEntries;
	private boolean integrityCheck;
	
	private static final int DEFAULT_CAPACITY = 25;
	private static final int MAXIMUM_CAPACITY = 10000;
	
	public ArrayListImplementation() {
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayListImplementation(int capacity) {
		if (capacity < 1 || capacity > MAXIMUM_CAPACITY) {
			throw new IllegalStateException("Capacity must be between 1 and 10000");
		}
		
		@SuppressWarnings("unchecked")
		T[] temp = (T[]) new Object[capacity];
		list = temp;
		numberOfEntries = 0;
		integrityCheck = true;
	}
	
	@Override
	public void add(T newEntry) {
		checkIntegrity();
		if (newEntry == null) {
			throw new IllegalArgumentException("Cannot add null entry");
		}
		ensureCapacity();
		list[numberOfEntries] = newEntry;
		numberOfEntries++;
		
	}

	@Override
	public void add(int index, T newEntry) {
		checkIntegrity();
		if (newEntry == null) {
            throw new IllegalArgumentException("Cannot add null entry");
        }
        if (index < 0 || index > numberOfEntries) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        ensureCapacity();
        // shift elements right
        for (int i = numberOfEntries; i > index; i--) {
        	list[i] = list[i-1];
        }
        list[index] = newEntry;
        numberOfEntries++;
		
	}

	@Override
	public T remove(int index) {
		checkIntegrity();
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        T removed = list[index];
        
        // Shift elements left
        for (int i = index; i < numberOfEntries - 1; i++) {
            list[i] = list[i + 1];
        }
        
        list[numberOfEntries - 1] = null;
        numberOfEntries--;
        
        return removed;
		
	}

	@Override
	public void clear() {
		checkIntegrity();
        for (int i = 0; i < numberOfEntries; i++) {
            list[i] = null;
        }
        numberOfEntries = 0;
		
	}

	@Override
	public T get(int index) {
		checkIntegrity();
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return list[index];
	}

	@Override
	public T set(int index, T newEntry) {
		checkIntegrity();
        if (newEntry == null) {
            throw new IllegalArgumentException("Cannot set null entry");
        }
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        T old = list[index];
        list[index] = newEntry;
        return old;
	}

	@Override
	public boolean contains(T entry) {
		checkIntegrity();
        if (entry == null) {
            return false;
        }
        
        for (int i = 0; i < numberOfEntries; i++) {
            if (entry.equals(list[i])) {
                return true;
            }
        }
        return false;
	}

	@Override
	public int getLength() {
		return numberOfEntries;
	}

	@Override
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	@Override
	public T[] toArray() {
		checkIntegrity();
        
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];
        
        for (int i = 0; i < numberOfEntries; i++) {
            result[i] = list[i];
        }
        
        return result;
	}
	
	@Override
	public String toString() {
		checkIntegrity();
		T[] activeEntries = Arrays.copyOf(list, numberOfEntries);
	    return Arrays.toString(activeEntries);
	}

	
	// private methods to support implementations\
	private void checkIntegrity() {
        if (!integrityCheck) {
            throw new SecurityException("ArrayList object is corrupted");
        }
    }
	
	private void ensureCapacity() {
		if (numberOfEntries >= list.length) {
			if (list.length >= MAXIMUM_CAPACITY) {
				throw new IllegalStateException("List reached maximum capacity");
			}
			
			// increase the size of array by 15
			int newSize = list.length + 15;
			int newCapacity = Math.min(newSize, MAXIMUM_CAPACITY);
			
			// if new size is within bounds, copy the items of old array into new array and assign it to myBag reference variable
			list = Arrays.copyOf(list, newCapacity);
		}	
	}
	
}
