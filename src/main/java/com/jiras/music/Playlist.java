package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist extends TrackList {

    public Playlist(String name) {
        super(name);
    }


    @Override
    public String toString() {
        return name;
    }


}
