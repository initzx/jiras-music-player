package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Track {

    private Media media;
    private String path;
    private MediaPlayer mediaPlayer;
    private Integer trackNum = 0;
    private String title = "Unknown track";
    private String year = "Unknown year";
    private String artist = "Unknown artist";
    private String album = "Unknown album";

//    private Track(Media media, String year, String artist, String album) {
//        this.media = media;
//        this.path = media.getSource();
//        this.year = year;
//        this.artist = artist;
//        this.album = album;
//    }

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
                    case "album":
                        album = c.getValueAdded().toString();
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
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

    public String getAlbum() {
        return album;
    }
}
