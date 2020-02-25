package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Track {

    private Media media;
    private String path;
    private Integer trackNum = 0;
    private String title = "Unknown track";
    private String year = "Unknown year";
    private String artist = "Unknown artist";
    private String album = "Unknown album";

    public Track(Media media, String defaultTitle) {
        // it is required to initialize media with MediaPlayer to get the duration
        new MediaPlayer(media);

        this.media = media;
        this.path = media.getSource();
        this.title = defaultTitle;
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
