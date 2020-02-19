package com.jiras.user;

import com.jiras.music.Playlist;

import java.util.HashMap;

public class UserData {
    HashMap<String, Playlist> playlists;


    public UserData() {
        this.playlists = new HashMap<>();
    }

    public Playlist[] getAllPlaylists() {
        return playlists.values().toArray(new Playlist[0]);
    }


    public Playlist getPlaylist(String name) {
        return playlists.get(name);
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.put(playlist.getName(), playlist);
        // TODO save playlist to db
    }

}
