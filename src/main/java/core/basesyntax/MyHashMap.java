package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] nodesArray;
    private int capacity;
    private int size;

    public MyHashMap() {
        nodesArray = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    private int getHash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempNodesArray = nodesArray;
        capacity *= CAPACITY_MULTIPLIER;
        nodesArray = new Node[capacity];
        for (Node<K, V> node: tempNodesArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == capacity * LOAD_FACTOR) {
            resize();
        }
        int index = getHash(key);
        Node<K, V> nodeToPut = new Node<>(index, key, value, null);
        Node<K, V> currentNode = nodesArray[index];
        if (currentNode == null) {
            nodesArray[index] = nodeToPut;
            size++;
            return;
        }
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.hash == index && Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = nodeToPut;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key);
        Node<K, V> currentNode = nodesArray[index];
        while (currentNode != null) {
            if (currentNode.hash == index && Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
