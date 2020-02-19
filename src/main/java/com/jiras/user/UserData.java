package com.jiras.user;

import com.jiras.music.Album;
import com.jiras.music.Playlist;
import com.jiras.music.Track;
import com.jiras.sql.Database;
import javafx.scene.media.Media;

import java.io.File;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserData {
    HashMap<String, Playlist> playlists;
    HashMap<String, Album> albums;


    public UserData(Database db) throws SQLException {
        this.playlists = new HashMap<>();
        this.albums = new HashMap<>();
        //sync folders with songs to load
        ResultSet syncFolders = db.selectAll("SELECT path FROM musicFolders");
        while(syncFolders.next()) {
            String path = syncFolders.getString("path");
            ArrayList<Album> albums = recursiveAlbums(path);
            for(Album album : albums) {
                int albumID;
                //check if album exists
                PreparedStatement albumStmt = db.initQuery("SELECT id FROM albums WHERE name = ?");
                albumStmt.setString(0, album.getName());
                ResultSet duplicates = db.executeStmt(albumStmt);
                if(duplicates == null) {
                    PreparedStatement insertStmt = db.initQuery("INSERT INTO albums(name) VALUES (?)");
                    insertStmt.setString(0, album.getName());
                    albumID = db.executeUpdate(insertStmt);
                } else {
                    albumID = duplicates.getInt("id");
                }
                for(Track track : album.getTracks()) {
                }
            }
        }


        //load all tracks into Track objects with list

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

    public ArrayList<Album> recursiveAlbums(String path) {
        File musicDir = new File(path);
        ArrayList<Album> albums = new ArrayList<>();

        Album album = new Album(musicDir.getName(), musicDir.getPath());
        for (File file : musicDir.listFiles()) {
            if (file.isDirectory()) {
                albums.addAll(recursiveAlbums(file.getAbsolutePath()));
                continue;
            }
            album.addTrack(Track.loadTrack(new Media(Paths.get(file.getAbsolutePath()).toUri().toString())));
        }

        if (album.getTracks().length != 0)
            albums.add(album);

        return albums;
    }
}
