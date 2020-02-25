package com.jiras.user;

import com.jiras.music.Playlist;
import com.jiras.music.Track;
import javafx.scene.media.Media;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserData {
    public final static String DEFAULT_CONF_PATH = "music_dir.conf";

    private HashMap<String, Playlist> playlists;

    private UserData(String musicFolderPath) {
        this.playlists = new HashMap<>();

        for (Playlist playlist: recursiveImportFromDir(musicFolderPath)) {
            playlists.put(playlist.getName(), playlist);
        }
    }

    public UserData() {
        this.playlists = new HashMap<>();
    }

    public static UserData createUserDataFromConf() {
        try {
            FileReader reader = new FileReader(DEFAULT_CONF_PATH);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String path = bufferedReader.readLine();
            reader.close();
            return new UserData(path);
        } catch (IOException e) {
            return new UserData();
        }
    }

    public static UserData createUserDataFromPath(String musicDirPath) {
        try {
            FileWriter writer = new FileWriter(DEFAULT_CONF_PATH);
            writer.write(musicDirPath+"\r\n");
            writer.close();
            return new UserData(musicDirPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Playlist[] getAllPlaylists() {
        return playlists.values().toArray(new Playlist[0]);
    }

    public Playlist getPlaylist(String name) {
        return playlists.get(name);
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.put(playlist.getName(), playlist);
    }

    private ArrayList<Playlist> recursiveImportFromDir(String path) {
        // start music folder
        File musicDir = new File(path);
        ArrayList<Playlist> playlists = new ArrayList<>();

        Playlist playlist = new Playlist(musicDir.getName(), musicDir.getPath());
        if (musicDir.listFiles() == null)
            // if no files are found in this folder, return the playlist objects which have been found
            return playlists;

        for (File file : musicDir.listFiles()) {
            if (file.isDirectory()) {
                // import is called again here if one of the files is a directory
                playlists.addAll(recursiveImportFromDir(file.getAbsolutePath()));
            }
            // add only if it's mp3
            else if (file.getName().endsWith(".mp3")) {
                // added to a playlist object which later gets added to an ArrayList containing multiple playlists
                playlist.addTrack(
                        new Track(new Media(Paths.get(file.getAbsolutePath()).toUri().toString()),
                        file.getName().replace(".mp3", ""))
                );
            }
        }

        if (playlist.getTracks().length != 0)
            playlists.add(playlist);

        return playlists;
    }
}
