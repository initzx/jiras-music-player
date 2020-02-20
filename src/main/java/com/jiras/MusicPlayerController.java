package com.jiras;

import com.jiras.music.*;
import com.jiras.user.UserData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {

    @FXML
    private ListView<Album> albums;
    
    @FXML
    private ListView<Playlist> playlists;
    
    @FXML
    private TableView<Track> tracks;

    @FXML
    private TableColumn<Track, String> artistCol;
    @FXML
    private TableColumn<Track, String> trackCol;
    @FXML
    private TableColumn<Track, String> durationCol;

    @FXML
    private Text listName;

    @FXML
    private GridPane stage;
    @FXML
    private FontAwesomeIconView playIcon;

    @FXML
    private Text trackTitle;
    @FXML
    private Text artist;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text timeElapsed;
    @FXML
    private Text duration;

    private UserData userData;
    private Queue<Track> queue;
    private MediaView mediaView;
    private Track current;
    private Track next;

    private MediaPlayer player;

    private TrackList currentSelTrackList;
    private String selectedType;

    public void injectUserData(UserData userData) {
        this.userData = userData;
    }

    public void initializePlayer() {
        System.out.println("Loading playlists / albums");
        initializeTracksTable();
        for (Album album : this.userData.getAllAlbums()) {
            albums.getItems().add(album);
        }
        for (Playlist playlist : this.userData.getAllPlaylists()) {
            playlists.getItems().add(playlist);
        }
        albums.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Album>) change -> {
            setTrackList(change.getList().get(0), "album");
        });
        playlists.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Playlist>) change -> {
            setTrackList(change.getList().get(0), "playlist");
        });
    }
    public void setTrackList(TrackList list, String type) {
        if(selectedType != type) {
            if(selectedType=="album") {
                albums.getSelectionModel().clearSelection();
            } else if(selectedType=="playlist") {
                playlists.getSelectionModel().clearSelection();
            }
            selectedType = type;
        }
        listName.setText(list.getName());
        tracks.getItems().setAll(list.getTracks());
        currentSelTrackList = list;
    }

    private void initializeTracksTable() {
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        trackCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        durationCol.setCellValueFactory(trackStringCellDataFeatures -> {
            int seconds = (int) Math.round(trackStringCellDataFeatures.getValue().getMedia().getDuration().toSeconds());
            return new ReadOnlyStringWrapper(String.format("%02d:%02d", seconds / 60, seconds % 60));
        });
        tracks.getSelectionModel().selectedItemProperty().addListener(change -> {
            play(tracks.getSelectionModel().getSelectedItem());
        });
    }

    private void play(Track track) {
        if (player != null && player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            this.queue.add(track);
            return;
        }

        current = track;
        player = new MediaPlayer(current.getMedia());
        player.setOnPlaying(this::onPlaying);
        player.setOnStopped(this::onStopped);
        player.setOnEndOfMedia(this::onEnd);
        player.play();
        playPause();
    }

    private void queue(Track track) {
        this.queue.add(track);
    }

    private void onPlaying() {
        System.out.println("playing " + current.getTitle());
        int seconds = (int) Math.round(player.getTotalDuration().toSeconds());
        duration.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        trackTitle.setText(current.getTitle());
        artist.setText(current.getArtist());
        player.currentTimeProperty().addListener(change -> {
            int elapsed = (int) Math.round(player.getCurrentTime().toSeconds());
            timeElapsed.setText(String.format("%02d:%02d", elapsed / 60, elapsed % 60));
            progressBar.setProgress(1.0 * player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis());
        });
    }

    private void onStopped() {
        System.out.println("onStopped " + current.getTitle());
        if (next != null)
            play(next);
        else
            endQueue();
    }

    private void onEnd() {
        System.out.println("ended " + current.getTitle());
        if (!queue.isAtEnd())
            play(queue.next());
        else
            endQueue();
    }

    private void endQueue() {
        player = null;
        queue.reset();
        resetGUI();
    }

    private void resetGUI() {
        timeElapsed.setText("00:00");
        duration.setText("00:00");
        artist.setText("");
        trackTitle.setText("");
    }

    private void stop() {
        if (player == null)
            return;
        System.out.println("stopped " + current.getTitle());
        player.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        queue = new Queue<>();
        mediaView = new MediaView();
        stage.getChildren().add(mediaView);
    }

    @FXML
    private void playPause() {
        if (player == null)
            return;
        MediaPlayer.Status status = player.getStatus();
        boolean notPaused = status == MediaPlayer.Status.PLAYING;

        if (notPaused) {
            player.pause();
            playIcon.setGlyphName("PLAY");
        } else {
            player.play();
            playIcon.setGlyphName("PAUSE");
        }
    }

    @FXML
    private void start(ActionEvent event) {

    }

    @FXML
    private void skip() {
        next = queue.next();
        stop();
    }

    @FXML
    private void prev() {
        next = queue.prev();
        stop();
    }
}
