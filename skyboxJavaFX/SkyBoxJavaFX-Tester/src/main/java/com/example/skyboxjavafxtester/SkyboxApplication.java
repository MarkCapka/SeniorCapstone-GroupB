package com.example.skyboxjavafxtester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class SkyboxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/IdeaProjects/SkyBoxJavaFX-Tester/src/main/resources/skyboxDesert.png"));
        //FXMLLoader fxmlLoader = new FXMLLoader(SkyboxApplication.class.getResource("skybox-view.fxml"));
        ImageView imageView = new ImageView(skyboxImage);
        imageView.setX(50);
        imageView.setY(25);
        Group skyboxImages = new Group(imageView);
        Scene scene = new Scene(skyboxImages, 1024, 768);
        stage.setTitle("Java Skybox");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}