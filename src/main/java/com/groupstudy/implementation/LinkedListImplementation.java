package com.groupstudy.implementation;

import com.groupstudy.adt.ListInterface;

/**
 * Singly Linked List implementation of ListInterface.
 * Uses a chain of Node objects where each node points to the next.
 * Supports traversal, append, and index-based access.
 */
public class LinkedListImplementation<T> implements ListInterface<T> {

	// inner Node class - each node holds data and a reference to the next node
	private class Node {
		T data;
		Node next;

		Node(T data) {
			this.data = data;
			this.next = null;
		}
	}

	private Node head;       // reference to first node in the chain
	private int numberOfEntries;

	public LinkedListImplementation() {
		this.head = null;
		this.numberOfEntries = 0;
	}

	/**
	 * Adds entry to end of list.
	 * Traverses to the last node and links the new node.
	 */
	@Override
	public void add(T newEntry) {
		if (newEntry == null) {
			throw new IllegalArgumentException("Cannot add null entry");
		}

		Node newNode = new Node(newEntry);

		if (head == null) {
			// empty list - new node becomes head
			head = newNode;
		} else {
			// traverse to last node
			Node current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newNode;
		}
		numberOfEntries++;
	}

	/**
	 * Adds entry at specified position.
	 * Shifts subsequent nodes by relinking pointers.
	 */
	@Override
	public void add(int index, T newEntry) {
		if (newEntry == null) {
			throw new IllegalArgumentException("Cannot add null entry");
		}
		if (index < 0 || index > numberOfEntries) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}

		Node newNode = new Node(newEntry);

		if (index == 0) {
			// insert at head
			newNode.next = head;
			head = newNode;
		} else {
			// traverse to the node just before the target index
			Node current = head;
			for (int i = 0; i < index - 1; i++) {
				current = current.next;
			}
			newNode.next = current.next;
			current.next = newNode;
		}
		numberOfEntries++;
	}

	/**
	 * Removes entry at given position.
	 * Relinks the previous node to skip over the removed node.
	 */
	@Override
	public T remove(int index) {
		if (index < 0 || index >= numberOfEntries) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}

		T removed;

		if (index == 0) {
			// remove head
			removed = head.data;
			head = head.next;
		} else {
			// traverse to node before the one to remove
			Node current = head;
			for (int i = 0; i < index - 1; i++) {
				current = current.next;
			}
			removed = current.next.data;
			current.next = current.next.next;
		}
		numberOfEntries--;
		return removed;
	}

	/**
	 * Removes all entries by unlinking the head.
	 * Garbage collector handles the rest.
	 */
	@Override
	public void clear() {
		head = null;
		numberOfEntries = 0;
	}

	/**
	 * Gets entry at given position by traversing from head.
	 */
	@Override
	public T get(int index) {
		if (index < 0 || index >= numberOfEntries) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}

		Node current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current.data;
	}

	/**
	 * Replaces entry at position and returns old entry.
	 */
	@Override
	public T set(int index, T newEntry) {
		if (newEntry == null) {
			throw new IllegalArgumentException("Cannot set null entry");
		}
		if (index < 0 || index >= numberOfEntries) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}

		Node current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		T old = current.data;
		current.data = newEntry;
		return old;
	}

	/**
	 * Checks if list contains the given entry by traversing all nodes.
	 */
	@Override
	public boolean contains(T entry) {
		if (entry == null) {
			return false;
		}

		Node current = head;
		while (current != null) {
			if (entry.equals(current.data)) {
				return true;
			}
			current = current.next;
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

	/**
	 * Converts linked list to array by traversing all nodes.
	 */
	@Override
	public T[] toArray() {
		@SuppressWarnings("unchecked")
		T[] result = (T[]) new Object[numberOfEntries];

		Node current = head;
		for (int i = 0; i < numberOfEntries; i++) {
			result[i] = current.data;
			current = current.next;
		}
		return result;
	}

	/**
	 * Returns the first element without removing it.
	 */
	public T getFirst() {
		if (head == null) {
			throw new IllegalStateException("List is empty");
		}
		return head.data;
	}

	/**
	 * Returns the last element without removing it.
	 */
	public T getLast() {
		if (head == null) {
			throw new IllegalStateException("List is empty");
		}
		Node current = head;
		while (current.next != null) {
			current = current.next;
		}
		return current.data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node current = head;
		while (current != null) {
			sb.append(current.data);
			if (current.next != null) {
				sb.append(", ");
			}
			current = current.next;
		}
		sb.append("]");
		return sb.toString();
	}
}
