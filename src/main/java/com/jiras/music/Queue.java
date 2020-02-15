package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;

public class Queue<T> {
    public ArrayList<T> queue;
    private int head;

    public Queue(T... items) {
        head = 0;
        queue = new ArrayList<>();
        addAll(items);
    }

    public void add(T item) {
        queue.add(item);
    }

    public void addAll(T... items) {
        Collections.addAll(queue, items);
    }

    public T next() {
        T item = queue.get(head);
        head = Math.min(head+1, queue.size()-1);
        return item;
    }

    public T prev() {
        head = Math.max(head-2, 0);
        T item = queue.get(head);
        head++;
        return item;
    }

    public ListIterator<T> getIterator() {
        return queue.listIterator();
    }
}
