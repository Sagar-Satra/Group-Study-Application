package com.groupstudy.adt;

/**
 * A simple Map ADT that stores key-value pairs.
 * This interface defines basic operations for mapping keys to values.
 */
public interface MapInterface<K, V> {

    /**
     * Adds a key-value pair into the map.
     * If the key already exists, update its value.
     */
    void put(K key, V value);

    /**
     * Returns the value associated with the given key.
     * Returns null if the key does not exist.
     */
    V get(K key);
    

    V getOrDefault(K key, V defaultValue);

    /**
     * Checks whether the given key exists in the map.
     */
    boolean containsKey(K key);

    /**
     * Removes the key-value pair associated with the given key.
     */
    void remove(K key);
    
    Iterable<K> keySet();
    
    int size();
}