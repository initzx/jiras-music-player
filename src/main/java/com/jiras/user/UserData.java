package com.jiras.user;

import com.jiras.music.Album;
import com.jiras.music.Playlist;
import com.jiras.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserData {
    HashMap<String, Playlist> playlists;
    HashMap<String, Album> albums;


    public UserData(Database db) throws SQLException {
        this.playlists = new HashMap<>();
        this.albums = new HashMap<>();
        //sync folders with songs to load
        ResultSet syncFolders = db.selectAll("SELECT path, albumID FROM musicFolders");
        //while(syncFolders.next()) {
            //read stuff about the song
            //Insert the songs into the database if path isn't already in there
        //}

        //initialize albums by selecting all albums
        //loop the albums and find all songs connected to that album, initialize the song and add it to the album list

        //initialize playlists
        //load all playlists
        //loop playlists, load all songs connected to the playlist. Add the song to the playlists list

        //Hvordan kan vi genbruge sange objektet når det er loadet heri?
        //Altså således at vi ikke skal lave et sang objekt af samme sange fordi en sang både er i et album samt en playlist
    }

    public Playlist[] getAllPlaylists() {
        return playlists.values().toArray(new Playlist[0]);
    }

    public Album[] getAllAlbums() {
        return albums.values().toArray(new Album[0]);
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
