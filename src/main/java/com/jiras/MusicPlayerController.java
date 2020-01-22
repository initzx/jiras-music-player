package com.jiras;

import com.jiras.music.Playlist;
import com.jiras.music.Track;
import com.jiras.user.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {

    @FXML
    AnchorPane stage;

    @FXML
    private Button playButton;

    @FXML
    private MediaView playerView;

    @FXML
    private ImageView albumArt;


    private UserData userData;
    private ArrayDeque<Track> queue;
    private MediaView mediaView;
    private MediaPlayer current;

    public void injectUserData(UserData userData) {
        this.userData = userData;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        queue = new ArrayDeque<>();
        mediaView = new MediaView();
        stage.getChildren().add(mediaView);
    }

    @FXML
    private void playClick(ActionEvent event) {
        if (current != null)
            current.stop();

        current = queue.pop().getMediaPlayer();
        mediaView.setMediaPlayer(current);
        current.play();
    }

    @FXML
    private void start(ActionEvent event) {
        System.out.println("Loading playlist");
        Playlist playlist = this.userData.getPlaylist("Mac <3");
        queue.addAll(Arrays.asList(playlist.getTracks()));
    }

    @FXML
    private void playSkip(ActionEvent event) {
    }
}
