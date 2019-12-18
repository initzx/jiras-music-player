package com.jiras;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        URL resource = classLoader.getResource("main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        //The GUI visuals are loaded from the information given in the XML file
        Parent root = loader.load();

        //The stage sets the opening window
        stage.setScene(new Scene(root));
        stage.setTitle("Beer explorer 2.0");

        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
