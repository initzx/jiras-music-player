package com.jiras.music;

public class Album extends TrackList {
    private String path;

    public Album(String name, String path) {
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
