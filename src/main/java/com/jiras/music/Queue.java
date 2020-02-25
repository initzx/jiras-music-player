package com.jiras.music;

import java.util.ArrayList;
import java.util.Collections;

public class Queue<T> {
    public ArrayList<T> queue;
    private int nextIndex;

    public Queue(T... items) {
        nextIndex = 0;
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
        T item = nextIndex >= queue.size() ? null : queue.get(nextIndex);
        nextIndex++;
        return item;
    }

    public T prev() {
        nextIndex -= 2;
        T item = nextIndex >= queue.size() || nextIndex < 0 ? null : queue.get(nextIndex);
        nextIndex++;
        return item;
    }

    public boolean isAtEnd() {
        return nextIndex == queue.size();
    }

    public void reset() {
        queue.clear();
        nextIndex = 0;
    }
}
