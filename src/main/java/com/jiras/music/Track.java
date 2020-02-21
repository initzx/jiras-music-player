package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class Track {
    private Integer id;
    private Media media;
    private String path;
    private MediaPlayer mediaPlayer;
    private Integer trackNum = 0;
    private String title = "Unknown track";
    private String year = "Unknown year";
    private String artist = "Unknown artist";
    private ArrayList<Integer> addedPlaylists = new ArrayList<>();

    public Track(Integer id, Media media, String title, String year, String artist) {
        this.id = id;
        this.media = media;
        this.path = media.getSource();
        this.title = title;
        this.year = year;
        this.artist = artist;
    }

    private Track(Media media) {
        this.media = media;
        this.mediaPlayer = new MediaPlayer(media);
        this.path = media.getSource();


        media.getMetadata().addListener((MapChangeListener<? super String, ? super Object>) c -> {
            if (c.wasAdded()) {
                switch ((String) c.getKey()) {
                    case "artist":
                        artist = c.getValueAdded().toString();
                        break;
                    case "title":
                        title = c.getValueAdded().toString();
                        break;
                    case "year":
                        year = c.getValueAdded().toString();
                        break;
                    case "track number":
                        trackNum = Integer.parseInt(c.getValueAdded().toString());
                        break;
                }
            }
        });
    }

    public static Track loadTrack(Media media) {
        return new Track(media);
    }

    public void addPlaylist(Integer playlistID) {
        addedPlaylists.add(playlistID);
    }
    public void resetPlaylists() {
        addedPlaylists = new ArrayList<>();
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    public Integer getID() {
        return id;
    }
    public ArrayList<Integer> getPlaylists() {
        return addedPlaylists;
    }
    public Media getMedia() {
        return media;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getYear() {
        return year;
    }

    public String getArtist() {
        return artist;
    }

}
