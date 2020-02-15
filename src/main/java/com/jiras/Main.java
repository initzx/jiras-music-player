package com.jiras;

import com.jiras.music.Album;
import com.jiras.music.Playlist;
import com.jiras.music.Track;
import com.jiras.user.UserData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL resource = classLoader.getResource("main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();
        loadController(loader.getController());
        //The GUI visuals are loaded from the information given in the XML file
        //The stage sets the opening window
        stage.setScene(new Scene(root));
        stage.setTitle("Jiras MP");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void loadController(MusicPlayerController controller) {
//        Playlist playlist = new Playlist("Mac <3");
//        File musicDir = new File("/home/init0/Music/Stop Staring at the Shadows");
//
//        for (File file: Objects.requireNonNull(musicDir.listFiles())) {
//            if (file.isDirectory()) {
//
//            }
//            Track track = Track.loadTrack(new Media(Paths.get(musicDir+"/"+file).toUri().toString()));
//            playlist.addTrack(track);
//        }
        UserData userData = new UserData();
        ArrayList<Album> albums = recursiveAddFromDir("/home/init0/Music");
        for (Album album : albums)
            userData.addAlbum(album);

//        userData.addPlaylist(playlist);
        controller.injectUserData(userData);
        controller.initializePlayer();
    }

    public ArrayList<Album> recursiveAddFromDir(String path) {
        File musicDir = new File(path);
        ArrayList<Album> albums = new ArrayList<>();

        Album album = new Album(musicDir.getName(), musicDir.getPath());
        for (File file : musicDir.listFiles()) {
            if (file.isDirectory()) {
                albums.addAll(recursiveAddFromDir(file.getAbsolutePath()));
                continue;
            }
            album.addTrack(Track.loadTrack(new Media(Paths.get(file.getAbsolutePath()).toUri().toString())));
        }

        if (album.getTracks().length != 0)
            albums.add(album);

        return albums;
    }
}
