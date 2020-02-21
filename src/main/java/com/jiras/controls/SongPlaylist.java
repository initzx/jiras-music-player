package com.jiras.controls;

public class SongPlaylist {
    Integer songID;
    Integer playlistID;
    String name;
    Boolean inPlaylist;
    public SongPlaylist(Integer playlistID, Integer songID, String name, Boolean inPlaylist) {
        this.playlistID = playlistID;
        this.songID = songID;
        this.name = name;
        this.inPlaylist = inPlaylist;
    }

    public String toString() {
        if(inPlaylist) {
            return name + "(Tilføjet)";
        }
        return name;
    }
    public int getPlaylistID() {
        return playlistID;
    }
    public int getSongID() {
        return songID;
    }
}
