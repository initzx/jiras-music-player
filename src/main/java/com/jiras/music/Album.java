package com.jiras.music;

public class Album extends TrackList {

    public Album(String name) {
        super(name);
    }


    @Override
    public String toString() {
        return name;
    }
}
