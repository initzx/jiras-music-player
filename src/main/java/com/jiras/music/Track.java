package com.jiras.music;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Track {
    private Integer id;
    private Media media;
    private String path;
    private MediaPlayer mediaPlayer;
    private Integer trackNum = 0;
    private String title;
    private String year = "Unknown year";
    private String artist = "Unknown artist";
    private ArrayList<Integer> addedPlaylists = new ArrayList<>();

    public Track(Integer id, String path, String title, String year, String artist) {
        this.id = id;
        this.media = new Media(Paths.get(path).toUri().toString());
        this.path = path;
        this.title = title;
        this.year = year;
        this.artist = artist;
    }
    public Track(String title, String artist,  String year, String path) {
        this.title = title;
        if(artist != null) this.artist = artist;
        if(year != null) this.year = year;
        this.path = path;
    }
    public static Track onlyMetadata(String path) {
        try {
            InputStream input = new FileInputStream(new File(path));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            return new Track(metadata.get("title"), metadata.get("creator"), metadata.get("year"), path);
        } catch (TikaException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
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
