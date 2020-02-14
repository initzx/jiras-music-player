package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Track {
    private Media media;
    private String path;
    private String title = "Unknown track";
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

//        media.getMetadata().addListener((MapChangeListener<? extends String, ? extends Object>) c -> {
//            System.out.println(c.getKey() + " : " + c.getValueAdded());
//            if (c.wasAdded()) {
//                if ("artist".equals(c.getKey())) {
//                    artist = c.getValueAdded().toString();
//                } else if ("title".equals(c.getKey())) {
//                    title = c.getValueAdded().toString();
//                } else if ("album".equals(c.getKey())) {
//                    album = c.getValueAdded().toString();
//                }
//            }
//        });
    }

    public static Track loadTrack(Media media) {
        return new Track(media);
    }

    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer(media);
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
