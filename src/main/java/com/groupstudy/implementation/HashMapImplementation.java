 package com.groupstudy.implementation;

import com.groupstudy.adt.MapInterface;

import java.util.LinkedList;

public class HashMapImplementation<K, V> implements MapInterface<K, V> {

    private LinkedList<Entry<K, V>>[] buckets;
    private int capacity = 16;
    private int size;

    private static class Entry<K, V> {

        K key;
        V value;

        Entry(K k, V v) {
            key = k;
            value = v;
        }
    }

    @SuppressWarnings("unchecked")
    public HashMapImplementation() {
        buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        for (Entry<K, V> e : buckets[index]) {
            if (e.key.equals(key)) {
                e.value = value;
                return; //
            }
        }

        buckets[index].add(new Entry<>(key, value));
        size++; //
    }

    @Override
    public V get(K key) {
        int index = hash(key);

        for (Entry<K, V> e : buckets[index]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public void remove(K key) {
        int index = hash(key);

        for (Entry<K, V> e : buckets[index]) {
            if (e.key.equals(key)) {
                buckets[index].remove(e);
                size--; // ⭐ 删除才 -1
                return;
            }
        }
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value == null) ? defaultValue : value;
    }

    @Override
    public Iterable<K> keySet() {
        LinkedList<K> keys = new LinkedList<>();

        for (int i = 0; i < capacity; i++) {
            for (Entry<K, V> e : buckets[i]) {
                keys.add(e.key);
            }
        }
        return keys;
    }

    @Override
    public int size() {
        return size;
    }
}