package com.example.skyboxjavafxtester;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SkyboxController extends SkyboxApplication {

    @FXML
    static FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    static StackPane entireFrame;

    @FXML
    static AnchorPane skyboxUIPane;

    @FXML
    static AnchorPane skyboxPane;

    @FXML
    private Label welcomeText;


    @FXML
    static
    Scene uiSceneRoot, sceneRoot;

    @FXML
    Stage stage, uiStage;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }



    //loads in skybox application for - > this is the method making the call to SkyboxApplication
    @FXML
    private static AnchorPane initializeSkybox()
    {

        try {
            skyboxPane = FXMLLoader.load(SkyboxController.class.getResource("/skybox-view.fxml"));
        } catch (IOException e) {
             String errorOutput = "error initializing Skybox in controller";
             System.out.println(errorOutput +e);
             e.printStackTrace();
        }
        skyboxPane.setId("skyboxPane");
       // skyboxPane.autosize();

        //TODO extract the scene from SkyboxApplication
        // set the pane to display the scene from the SkyboxApplication
        //return skyboxPane that is constructed and passed to initialize to beging displaying it along with UI
        return skyboxPane;
    }

    @FXML
    private static AnchorPane initializeUI()
    {


        try {
            skyboxUIPane =  FXMLLoader.load(SkyboxController.class.getResource("/skybox-viewUI.fxml"));

        } catch (IOException e) {
            String errorOutput = "error initializing UI Pane in controller";
            System.out.println(errorOutput + e);
            e.printStackTrace();
        }
        skyboxUIPane.setId("skyboxUIPane");
        skyboxUIPane.autosize();


        //TODO extract the scene from SkyboxApplication
        // set the pane to display the scene from the SkyboxApplication
        //return skyboxPane that is constructed and passed to initialize to beging displaying it along with UI
        return skyboxUIPane;
    }


    // this contains the code for starting up the skybox and linking the UI from FXML and visuals and code from java
        //handles the entire frame
    protected static Scene initializeProgram() {
        //
        //we have the UI view, do we need separate FXML for display window? could just build it right into skybox-viewUI.fxml
        skyboxUIPane = initializeUI();
        skyboxPane = initializeSkybox();

        entireFrame = initializeFrame(skyboxUIPane, skyboxPane);



    //    entireFrame.getChildren().addAll(skyboxUIPane, skyboxPane);



        return entireFrame.getScene();
    }


    public static StackPane initializeFrame(AnchorPane skyboxUIPane, AnchorPane skyboxPane) {
         entireFrame = new  StackPane(skyboxUIPane, skyboxPane);


        return entireFrame;
    }
}