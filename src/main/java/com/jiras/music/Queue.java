package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Queue {
    public ArrayList<Track> queue;

    public Queue(Track... tracks) {
        queue = new ArrayList<>();
        queue.addAll(Arrays.asList(tracks));
    }

    public void add(Track track) {
        queue.add(track);
    }

    public Track popNext() {
        return queue.remove(0);
    }
}
