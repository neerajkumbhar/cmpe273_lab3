package edu.sjsu.cmpe.cache.methods;

import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

    public ConsistentHash(HashFunction hashFunction,
                          int numberOfReplicas, Collection<T> nodes) {

        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {

            CharSequence c=node.toString()+i;
            circle.put(hashFunction.hashString(c).asInt(),node);

            System.out.println("Character Sequence is: "+c+"After applying Hash function: "+hashFunction.hashString(c).asInt());
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hashString(node.toString() + i).asInt());
        }
    }

    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        CharSequence c=key.toString();
       int hash = hashFunction.hashString(c).asInt();
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap =
                    circle.tailMap(hash);
            hash = tailMap.isEmpty() ?
                    circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
}
