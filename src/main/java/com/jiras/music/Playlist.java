package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist extends TrackList {
    Integer id;
    public Playlist(Integer id, String name) {
        super(name);
        this.id = id;
    }


    @Override
    public String toString() {
        return name;
    }

    public Integer getID() {
        return id;
    }

}
