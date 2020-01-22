package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Track {
    private Media media;
    private String path;
    private String name = "Unknown track";
    private String year = "Unknown year";
    private String artist = "Unknown artist";
    private String album = "Unknown album";

    private Track(Media media, String year, String artist, String album) {
        this.media = media;
        this.path = media.getSource();
        this.year = year;
        this.artist = artist;
        this.album = album;
    }

    private Track(Media media) {
        this.media = media;
        this.path = media.getSource();

        media.getMetadata().addListener((MapChangeListener<? super String, ? super Object>) change -> {
            if (change.wasAdded()) {
//                this.album = "wew";
            }
        });
    }

    public static Track loadTrack(Media media) {
        return new Track(media);
    }

    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer(media);
    }

    public String getName() {
        return name;
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

    public String getAlbum() {
        return album;
    }
}
