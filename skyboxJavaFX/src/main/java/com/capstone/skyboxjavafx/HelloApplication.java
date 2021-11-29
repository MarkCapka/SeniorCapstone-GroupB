package com.capstone.skyboxjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//TODO call skybox w/ new skybox if needed from here.
    //TODO where do we need to have image file placed?
    //TODO do i need to do a whole separate import of a file?
    //TODO set up github. invite team members
    //TODO need to update fxml file? how get Skybox to fxml, just make call within the controller?
    //TODO passing in image, what formats work?


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("JavaFX skybox");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}