package com.groupstudy.adt;

public interface SortInterface {
	public <T extends Comparable<? super T>> void sort(T[] array);
	public <T extends Comparable<? super T>> void sort(T[] array, int left, int right);
}
