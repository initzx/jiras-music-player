package com.jiras.music;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist extends TrackList {
    public Playlist() {
        this.name = "Unamed Playlist";
        this.tracks = new ArrayList<>();
    }

    public Playlist(String name, Track... tracks) {
        this.name = name;
        this.tracks = new ArrayList<>();
        this.tracks.addAll(Arrays.asList(tracks));
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    public Track[] getTracks() {
        return tracks.toArray(new Track[]{});
    }
}
