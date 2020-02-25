package com.jiras.music;

public class Playlist extends TrackList {
    private String path;

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
