package com.jiras;

import com.jiras.music.Playlist;
import com.jiras.music.Queue;
import com.jiras.music.StopReason;
import com.jiras.music.Track;
import com.jiras.user.UserData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {

    @FXML private ListView<Playlist> playlists;

    @FXML private TableView<Track> tracks;

    @FXML private TableColumn<Track, String> artistCol;
    @FXML private TableColumn<Track, String> albumCol;
    @FXML private TableColumn<Track, String> trackCol;
    @FXML private TableColumn<Track, String> durationCol;

    @FXML private GridPane stage;
    @FXML private FontAwesomeIconView playIcon;

    @FXML private Text trackTitle;
    @FXML private Text artist;
    @FXML private ProgressBar progressBar;
    @FXML private Text timeElapsed;
    @FXML private Text duration;

    private UserData userData;
    private Queue<Track> queue;
    private MediaView mediaView;
    private Track current;
    private Track next;
    private StopReason stopReason;

    private MediaPlayer player;

    private Playlist currentSelPlaylist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        queue = new Queue<>();
        mediaView = new MediaView();
        stopReason = StopReason.NONE;
        stage.getChildren().add(mediaView);
    }

    public void injectUserData(UserData userData) {
        this.userData = userData;
    }

    public void initializePlayer() {
        System.out.println("Initializing player");
        initializePlaylistsChooser();
        initializeTracksTable();
    }

    private void initializePlaylistsChooser() {
        playlists.getItems().clear();
        // add all playlists to the side chooser
        for (Playlist playlist : userData.getAllPlaylists()) {
            playlists.getItems().add(playlist);
        }

        playlists.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Playlist>) change -> {
            Playlist playlist = change.getList().get(0);
            // tracks are put in the tracks table here
            tracks.getItems().setAll(playlist.getTracks());
            currentSelPlaylist = playlist;
        });
    }

    private void initializeTracksTable() {
        tracks.getItems().clear();

        // columns are initialized with the respective **field names** of the Track object
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
        trackCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        durationCol.setCellValueFactory(trackStringCellDataFeatures -> {
            // duration must be formatted
            int seconds = (int) Math.round(trackStringCellDataFeatures.getValue().getMedia().getDuration().toSeconds());
            return new ReadOnlyStringWrapper(String.format("%02d:%02d", seconds / 60, seconds % 60));

        });

        // if the user clicks on a track
        tracks.getSelectionModel().selectedItemProperty().addListener(change -> {
            Track selected = tracks.getSelectionModel().getSelectedItem();
            if (selected == null)
                return;

            // replace the current playing song
            if (player != null) {
                queue.reset();
                stop(StopReason.REPLACED);
            }

            Track[] tracks = currentSelPlaylist.getTracks();
            boolean addFollowing = false;
            for (Track track : tracks) {
                // iterate over the playlist until we find our selected track in the playlist
                if (!addFollowing && track == selected) {
                    addFollowing = true;
                }
                // add the song and all after it if we have found our selected track
                if (addFollowing) {
                    queue(track);
                }
            }
        });
    }

    private void play(Track track) {
        current = track;
        player = new MediaPlayer(current.getMedia());
        player.setOnPlaying(this::onPlaying);
        player.setOnPaused(this::onPaused);
        player.setOnStopped(this::onFinished);
        player.setOnEndOfMedia(this::onFinished);
        player.play();
    }

    private void queue(Track track) {
        this.queue.add(track);

        if (player == null || stopReason == StopReason.REPLACED ) {
            if (stopReason == StopReason.REPLACED) // the user clicked on a new track
                stopReason = StopReason.CONTINUE;
            play(queue.next());
        }
    }

    private void onPlaying() {
        // here the actual player is updated in terms of appearance
        playIcon.setGlyphName("PAUSE");
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

    private void onPaused() {
        playIcon.setGlyphName("PLAY");
    }

    private void onFinished() {
        switch (stopReason) {
            case SKIPPED:
                stopReason = StopReason.NONE;
                if (next != null) {
                    play(next);
                    return;
                }
                break;
            case CONTINUE:
                stopReason = StopReason.NONE;
                return;
            default:
                if (!queue.isAtEnd()) {
                    play(queue.next());
                    return;
                }
                break;
        }
        endQueue();
    }

    private void endQueue() {
        player = null;
        queue.reset();
        resetPlayerGUI();
    }

    private void resetPlayerGUI() {
        timeElapsed.setText("00:00");
        duration.setText("00:00");
        progressBar.setProgress(0);
        artist.setText("");
        trackTitle.setText("");
        playIcon.setGlyphName("PLAY");
    }

    private void stop(StopReason reason) {
        if (player == null)
            return;
        stopReason = reason;
        player.stop();
    }

    @FXML private void playPause() {
        if (player == null)
            return;
        MediaPlayer.Status status = player.getStatus();
        boolean notPaused = status == MediaPlayer.Status.PLAYING;

        if (notPaused) {
            player.pause();
        } else {
            player.play();
        }
    }

    @FXML private void skip() {
        if (player == null)
            return;
        next = queue.next();
        stop(StopReason.SKIPPED);
    }

    @FXML private void prev() {
        if (player == null)
            return;
        if (player.getCurrentTime().greaterThan(new Duration(1000))) {
            player.seek(Duration.ZERO);
            return;
        }

        next = queue.prev();
        stop(StopReason.SKIPPED);
    }

    @FXML private void importSongs() {
        // if the user clicks on the import songs button

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        File selected = directoryChooser.showDialog(stage.getScene().getWindow());
        userData = UserData.createUserDataFromPath(selected.getPath());
        initializePlayer();
    }
}
