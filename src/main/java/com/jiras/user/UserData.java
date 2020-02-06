package com.jiras.user;

import com.jiras.music.Album;
import com.jiras.music.Playlist;

import java.util.HashMap;

public class UserData {
    HashMap<String, Playlist> playlists;
    HashMap<String, Album> albums;


    public UserData() {
        this.playlists = new HashMap<>();
        this.albums = new HashMap<>();
    }

    public Playlist getPlaylist(String name) {
        return playlists.get(name);
    }

    public Album getAlbum(String name) {
        return albums.get(name);
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.put(playlist.getName(), playlist);
        // TODO save playlist to db
    }

    public void addAlbum(Album album) {
        this.albums.put(album.getName(), album);
    }
}
