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
        T item = head >= queue.size() ? null : queue.get(head);
        head++;
        return item;
    }

    public T prev() {
        head -= 2;
        T item = head >= queue.size() || head < 0 ? null : queue.get(head);
        head++;
        return item;
    }

    public boolean isAtEnd() {
        return head == queue.size();
    }

    public void reset() {
        queue.clear();
        head = 0;
    }
}
