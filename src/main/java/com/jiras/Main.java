package com.jiras;

import com.jiras.sql.Database;
import com.jiras.user.UserData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;


public class Main extends Application {
    Database db;

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
        db = new Database();

        UserData userData = new UserData(db);
        controller.injectUserData(userData);
        controller.initializePlayer();
    }
}
