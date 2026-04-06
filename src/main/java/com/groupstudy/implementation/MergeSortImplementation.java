package com.groupstudy.implementation;

import com.groupstudy.adt.SortInterface;

public class MergeSortImplementation implements SortInterface {
    
    @Override
    public <T extends Comparable<? super T>> void sort(T[] array) {
        if (array == null) {
        	throw new IllegalArgumentException("Array cannot be null");
        }
        
        if (array.length <= 1) {
        	return;
        }
        
        @SuppressWarnings("unchecked")
        T[] tempArray = (T[]) new Comparable[array.length];
        mergeSort(array, tempArray, 0, array.length - 1);
    }
    
    @Override
    public <T extends Comparable<? super T>> void sort(T[] array, int left, int right) {
        if (array == null || left >= right) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        T[] tempArray = (T[]) new Comparable[array.length];
        mergeSort(array, tempArray, left, right);
    }
    
    private <T extends Comparable<? super T>> void mergeSort(T[] a, T[] tempArray, int first, int last) {
        if (first < last) {
            int mid = first + (last - first) / 2;
            mergeSort(a, tempArray, first, mid);
            mergeSort(a, tempArray, mid + 1, last);
            merge(a, tempArray, first, mid, last);
        }
    }
    
    private <T extends Comparable<? super T>> void merge(T[] a, T[] tempArray, int first, int mid, int last) {
        int beginHalf1 = first;
        int endHalf1 = mid;
        int beginHalf2 = mid + 1;
        int endHalf2 = last;
        int index = beginHalf1;
        
        while ((beginHalf1 <= endHalf1) && (beginHalf2 <= endHalf2)) {
            if (a[beginHalf1].compareTo(a[beginHalf2]) <= 0) {
                tempArray[index] = a[beginHalf1];
                beginHalf1++;
            } else {
                tempArray[index] = a[beginHalf2];
                beginHalf2++;
            }
            index++;
        }
        
        while (beginHalf1 <= endHalf1) {
            tempArray[index] = a[beginHalf1];
            beginHalf1++;
            index++;
        }
        
        while (beginHalf2 <= endHalf2) {
            tempArray[index] = a[beginHalf2];
            beginHalf2++;
            index++;
        }
        
        for (index = first; index <= last; index++) {
            a[index] = tempArray[index];
        }
    }
}