package com.jiras.user;

import com.jiras.controls.MusicFolder;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    Database db;
    HashMap<Integer, Playlist> playlists;
    HashMap<Integer, Album> albums;
    HashMap<Integer, Track> tracks;
    HashMap<Integer, MusicFolder> musicFolders;


    public UserData(Database db) throws SQLException, MalformedURLException, URISyntaxException {
        this.db = db;

        sync();

    }
    public void deleteMusicFolder(String path) throws SQLException, MalformedURLException, URISyntaxException {
        PreparedStatement deleteStmt = db.initQuery("DELETE FROM musicFolders WHERE path = ?");
        deleteStmt.setString(1, path);
        db.executeUpdate(deleteStmt);
        //resync
        sync();
    }

    public void addMusicFolder(String path) throws SQLException, MalformedURLException, URISyntaxException {
        PreparedStatement insertStmt = db.initQuery("INSERT INTO musicFolders(path) VALUES (?)");
        insertStmt.setString(1, path);
        db.executeUpdate(insertStmt);
        //resync
        sync();
    }

    public boolean toggleSongPlaylist(Integer songID, Integer playlistID) throws SQLException, MalformedURLException, URISyntaxException {
        PreparedStatement playlistSongStmt = db.initQuery("SELECT id FROM playlistSongs WHERE songID = ? AND playlistID = ?");
        playlistSongStmt.setInt(1, songID);
        playlistSongStmt.setInt(2, playlistID);
        ResultSet duplicates = db.executeStmt(playlistSongStmt);
        if(duplicates.isClosed()) {
            PreparedStatement insertStmt = db.initQuery("INSERT INTO playlistSongs(songID, playlistID) VALUES (?, ?)");
            insertStmt.setInt(1, songID);
            insertStmt.setInt(2, playlistID);
            db.executeUpdate(insertStmt);

        } else {
            Integer id = duplicates.getInt("id");
            PreparedStatement deleteStmt = db.initQuery("DELETE FROM playlistSongs WHERE id = ?");
            deleteStmt.setInt(1, id);
            db.executeUpdate(deleteStmt);
        }
        //resync
        syncPlaylists(true);
        return duplicates.isClosed();
    }

    public Playlist[] getAllPlaylists() {
        return playlists.values().toArray(new Playlist[0]);
    }
    public MusicFolder[] getAllMusicFolders() {
        return musicFolders.values().toArray(new MusicFolder[0]);
    }

    public Album[] getAllAlbums() {
        return albums.values().toArray(new Album[0]);
    }
    public Track getIndexedTrack(Integer id) {
        return tracks.get(id);
    }
    private void sync() throws SQLException, MalformedURLException, URISyntaxException {
        syncFolders();
        syncAlbums();
        syncTracks();
        syncPlaylists(false);
    }
    private void syncPlaylists(boolean resetTracks) throws SQLException {
        if(resetTracks) {
            for (Map.Entry<Integer, Track> entry : tracks.entrySet()) {
                entry.getValue().resetPlaylists();
            }
        }
        this.playlists = new HashMap<>();

        //initialize playlists
        //load all playlists
        ResultSet dbPlaylists = db.selectAll("SELECT id, name FROM playlists");
        while(dbPlaylists.next()) {
            Integer id = dbPlaylists.getInt("id");
            String name = dbPlaylists.getString("name");
            this.playlists.put(id, new Playlist(id, name));
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
    private void syncTracks() throws SQLException, MalformedURLException, URISyntaxException {
        this.tracks = new HashMap<>();

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
                Track track =  new Track(dbTracks.getInt("id"), new Media(path), dbTracks.getString("name"), dbTracks.getString("year"), dbTracks.getString("artist"));
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
                //remove it
                PreparedStatement deleteStmt2 = db.initQuery("DELETE FROM playlistSongs WHERE songID = ?");
                deleteStmt2.setInt(1, id);
                db.executeUpdate(deleteStmt2);
            }
        }
    }
    private void syncAlbums() throws SQLException {
        this.albums = new HashMap<>();

        //initialize albums by selecting all albums
        ResultSet dbAlbums = db.selectAll("SELECT id, name FROM albums");
        while(dbAlbums.next()) {
            Integer id = dbAlbums.getInt("id");
            String name = dbAlbums.getString("name");
            this.albums.put(id, new Album(name));
        }
    }
    private void syncFolders() throws SQLException {
        //sync folders with songs to load

        this.musicFolders = new HashMap<>();

        ResultSet syncFolders = db.selectAll("SELECT id, path FROM musicFolders");
        while(syncFolders.next()) {
            Integer id = syncFolders.getInt("id");
            String path = syncFolders.getString("path");
            //add the folder to userdata
            this.musicFolders.put(id, new MusicFolder(path));

            //find all albums inside the folder
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
    }
    public ArrayList<Album> recursiveAlbums(String path) {
        File musicDir = new File(path);
        ArrayList<Album> albums = new ArrayList<>();

        Album album = new Album(musicDir.getName());
        if(musicDir.listFiles() != null) {
            for (File file : musicDir.listFiles()) {
                if (file.isDirectory()) {
                    albums.addAll(recursiveAlbums(file.getAbsolutePath()));
                    continue;
                }
                String extension = "";

                int i = file.getAbsolutePath().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getAbsolutePath().substring(i + 1);
                }

                if (extension.equals("mp3") || extension.equals("m4a")) {

                    album.addTrack(Track.loadTrack(new Media(Paths.get(file.getAbsolutePath()).toUri().toString())));
                }
            }
        }

        if (album.getTracks().length != 0)
            albums.add(album);

        return albums;
    }
}
