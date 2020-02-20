package com.jiras.user;

import com.jiras.music.Album;
import com.jiras.music.Playlist;
import com.jiras.music.Track;
import com.jiras.sql.Database;
import javafx.scene.media.Media;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserData {
    HashMap<Integer, Playlist> playlists;
    HashMap<Integer, Album> albums;
    HashMap<Integer, Track> tracks;


    public UserData(Database db) throws SQLException, MalformedURLException, URISyntaxException {
        this.playlists = new HashMap<>();
        this.albums = new HashMap<>();
        this.tracks = new HashMap<>();

        //sync folders with songs to load
        ResultSet syncFolders = db.selectAll("SELECT path FROM musicFolders");
        while(syncFolders.next()) {
            String path = syncFolders.getString("path");
            ArrayList<Album> albums = recursiveAlbums(path);
            for(Album album : albums) {
                int albumID;
                //check if album exists
                PreparedStatement albumStmt = db.initQuery("SELECT id FROM albums WHERE name = ?");
                albumStmt.setString(1, album.getName());
                ResultSet duplicates = db.executeStmt(albumStmt);
                if(duplicates.isClosed()) {
                    //insert album
                    PreparedStatement insertStmt = db.initQuery("INSERT INTO albums(name) VALUES (?)");
                    insertStmt.setString(1, album.getName());
                    albumID = db.executeUpdate(insertStmt);
                } else {
                    //reuse old
                    albumID = duplicates.getInt("id");
                }
                for(Track track : album.getTracks()) {
                    //check if song exists
                    PreparedStatement songStmt = db.initQuery("SELECT id FROM songs WHERE path = ?");
                    songStmt.setString(1, track.getPath());
                    ResultSet songDuplicates = db.executeStmt(songStmt);
                    if(songDuplicates.isClosed()) {
                        //insert song
                        PreparedStatement insertStmt = db.initQuery("INSERT INTO songs(name, path, year, artist, albumID) VALUES (?, ?, ?, ?, ?)");
                        insertStmt.setString(1, track.getTitle());
                        insertStmt.setString(2, track.getPath());
                        insertStmt.setString(3, track.getYear());
                        insertStmt.setString(4, track.getArtist());
                        insertStmt.setInt(5, albumID);
                        db.executeUpdate(insertStmt);
                    }
                }
            }
        }

        //initialize albums by selecting all albums
        ResultSet dbAlbums = db.selectAll("SELECT id, name FROM albums");
        while(dbAlbums.next()) {
            Integer id = dbAlbums.getInt("id");
            String name = dbAlbums.getString("name");
            this.albums.put(id, new Album(name));
        }

        //load all tracks into Track objects with list
        ResultSet dbTracks = db.selectAll("SELECT id, path, name, year, artist, albumID FROM songs");
        while(dbTracks.next()) {
            String path = dbTracks.getString("path");
            Integer id = dbTracks.getInt("id");
            Integer albumID = dbTracks.getInt("albumID");

            //check if file still exists
            Path realPath = Paths.get(new URL(path).toURI());;
            if(Files.exists(realPath)) {
                //add track
                Track track =  new Track(new Media(path), dbTracks.getString("name"), dbTracks.getString("year"), dbTracks.getString("artist"));
                this.tracks.put(id, track);
                if(albumID!=0) {
                    //also add it to the album
                    this.albums.get(albumID).addTrack(track);
                }
            } else {
                //remove it from database
                PreparedStatement deleteStmt = db.initQuery("DELETE FROM songs WHERE id = ?");
                deleteStmt.setInt(1, id);
                db.executeUpdate(deleteStmt);
            }
        }

        //initialize playlists
        //load all playlists
        ResultSet dbPlaylists = db.selectAll("SELECT id, name FROM playlists");
        while(dbPlaylists.next()) {
            Integer id = dbPlaylists.getInt("id");
            String name = dbPlaylists.getString("name");
            this.playlists.put(id, new Playlist(name));
        }

        //connect songs to playlists
        ResultSet dbPlaylistSongs = db.selectAll("SELECT songID, playlistID FROM playlistSongs");
        while(dbPlaylistSongs.next()) {
            Integer songID = dbPlaylistSongs.getInt("songID");
            Integer playlistID = dbPlaylistSongs.getInt("playlistID");
            Track track = this.tracks.get(songID);
            if(track != null) {
                //make sure the tracks knows it's in the playlist
                track.addPlaylist(playlistID);
                this.playlists.get(playlistID).addTrack(track);
            }
        }

    }

    public Playlist[] getAllPlaylists() {
        return playlists.values().toArray(new Playlist[0]);
    }

    public Album[] getAllAlbums() {
        return albums.values().toArray(new Album[0]);
    }

    public ArrayList<Album> recursiveAlbums(String path) {
        File musicDir = new File(path);
        ArrayList<Album> albums = new ArrayList<>();

        Album album = new Album(musicDir.getName());
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
