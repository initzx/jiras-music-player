package com.jiras;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller  {
    enum Routes {
        playlists,
        albums,
        songs
    }
    @FXML
    private Button playlistsBtn;
    @FXML
    private Button albumsBtn;
    @FXML
    private Button songsBtn;
    @FXML
    private AnchorPane image;


    private static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    // Add a public no-args constructor
    public Controller() {
    }

    @FXML
    public void initialize() {
        playlistsBtn.setOnAction(this::goToPlaylists);
        albumsBtn.setOnAction(this::goToAlbums);
        songsBtn.setOnAction(this::goToSongs);
    }

    @FXML
    private void goToPlaylists(ActionEvent event) {
        playlistsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, true);
        albumsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);
        songsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);
    }
    @FXML
    private void goToAlbums(ActionEvent event) {
        playlistsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);
        albumsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, true);
        songsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);

    }
    @FXML
    private void goToSongs(ActionEvent event) {
        playlistsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);
        albumsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, false);
        songsBtn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, true);

    }
    @FXML
    private void buttonClicked() {
        System.out.println("Button clicked!");
    }
}
