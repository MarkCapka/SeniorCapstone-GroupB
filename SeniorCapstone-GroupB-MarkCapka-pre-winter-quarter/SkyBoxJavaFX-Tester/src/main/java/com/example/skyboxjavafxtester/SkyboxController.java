package com.example.skyboxjavafxtester;

import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader; //TODO need to uncomment when we utilize the view
import javafx.beans.property.StringProperty;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;




//referenced https://openjfx.io/javadoc/17/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html for controller functions

public class SkyboxController extends VBox implements Initializable {

    @FXML
    private Label welcomeText;
    private Label skyboxImport;
    private Button button;
    private Skybox skybox; //TODO need to pull in definition and declare the skybox here.
    private URL location;
    private ResourceBundle resources;

    @FXML
    protected void loadSkyboxView(ActionEvent event) throws FileNotFoundException {
      //this.skybox.getSize();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("skybox-view.fxml"));
        //setup for fxml stuff

        URL location = getClass().getResource("SkyBoxJavaFX-Tester/src/main/resources/com/example/skyboxjavafxtester");
        ResourceBundle resources = ResourceBundle.getBundle("SkyBoxJavaFX-Tester/src/main/java/com/example/skyboxjavafxtester");
        fxmlLoader = new FXMLLoader(location, resources);
        welcomeText.setText("Welcome to JavaFX Application: TEST : Sunlight Optimization Simulator!");
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

       this.generateSkybox(event);
    }

// TODO below may be moved into skybox-view.fxml so we have control of all view functionality in one place
//      //the image is what will be displayed (for our skybox) within the scene

 @FXML
 protected void generateSkybox(ActionEvent event) throws FileNotFoundException {
     //event is for passing whcih skybox to generate (image 1 or 2) w/ 1 or 6 images

//
     //IMPORTANT may need to change name of fileinputstream below if we change the name of the project (specifically once we remove "tester")

     //TODO explore limitations of filetype -> are we limited to .png?
     Image skyboxImage = new Image(new FileInputStream("SkyBoxJavaFX-Tester/src/main/resources/skyboxDesert.png"));

     ImageView imageView = new ImageView(skyboxImage);
     imageView.setX(50);
     imageView.setY(25);
     //TODO


     initialize(location, resources); //TODO see below, we need to fix this and adjust for URL location and resources below

//TODO these are already handled in a switch statement below.
    //TODO need to link in the methods that build the skybox here.
    }
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("You clicked me!");
                }
            }
            );
    }

    public void setText(String s) {
        welcomeText.setText(s);
    }


}