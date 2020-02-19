package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist extends TrackList {
    private String path;

    public Playlist() {
        super("Unamed Playlist");
    }

    public Playlist(String name, String path) {
        super(name);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }
}
