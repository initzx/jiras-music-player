package com.jiras.music;

import java.util.ArrayList;

public abstract class TrackList {
    String name;
    ArrayList<Track> tracks;

    public TrackList(String name) {
        this.name = name;
        this.tracks = new ArrayList<>();
    }

    public void  addTrack(Track track) {
        this.tracks.add(track);
    }

    public Track[] getTracks() {
        return tracks.toArray(new Track[]{});
    }

    public String getName() {
        return name;
    }
}
