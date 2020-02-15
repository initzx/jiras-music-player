package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist extends TrackList {
    public Playlist() {
        super("Unamed Playlist");
    }

    public Playlist(String name, Track... tracks) {
        super(name);
        this.tracks.addAll(Arrays.asList(tracks));
    }


}
