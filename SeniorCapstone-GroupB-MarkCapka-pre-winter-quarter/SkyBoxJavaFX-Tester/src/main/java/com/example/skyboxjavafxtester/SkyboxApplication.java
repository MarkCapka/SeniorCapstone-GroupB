package com.example.skyboxjavafxtester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader; //TODO need to uncomment when we utilize the view
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class SkyboxApplication extends Application {
    @Override
    public void start(Stage stage)  {

            //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
            // right now we are just setting up skybox


        //setup for fxml stuff
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("skybox-view.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

        //  v TODO this is the call into setting the controller. need to finish up wrapping in with the application and constructor
        HBox hbox = new HBox();
        SkyboxController skyboxController = new SkyboxController();
        skyboxController.setText("Java world changed text test");
        hbox.getChildren().add(skyboxController);


        //  ^TODO through here
        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception + "error in SkyboxController onHelloButtonClick");
        }

        //I think this is the same as loading above? maybe? same with below, either redundant or doesn't below here.
     //  SkyboxController skyboxController = (SkyboxController) fxmlLoader.getController();



        //stage is the window our program opens in
        stage.setTitle("Java Skybox");  //titles window
        stage.setScene(stage.getScene());
//        stage.setScene(skyboxController.getScene());  //sets our scene (skybox) within the stage (window)
        stage.show();   //displays window
    }


    public static void main(String[] args) {
        launch();
    }
}