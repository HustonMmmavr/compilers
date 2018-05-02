package com.company.compilers;

public class Pair<K, V> {
    private K data1;
    private V data2;

    public Pair(K key, V value) {
        this.data1 = key;
        this.data2 = value;
    }

    public K getKey() {
        return data1;
    }

    public V getValue() {
        return data2;
    }

    public void setKey(K key) {
        this.data1 = key;
    }

    public void setValue(V value) {
        this.data2 = value;
    }
}
