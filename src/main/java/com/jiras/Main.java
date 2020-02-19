package com.jiras;

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
import java.sql.SQLException;
import java.util.ArrayList;


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

    private void loadController(MusicPlayerController controller) throws SQLException {
        UserData userData = UserData.createUserDataFromConf();
        controller.injectUserData(userData);
        controller.initializePlayer();
    }
}
