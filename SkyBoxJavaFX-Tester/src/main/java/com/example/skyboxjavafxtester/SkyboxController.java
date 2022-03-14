package com.example.skyboxjavafxtester;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SkyboxController {

    @FXML
    static FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    static AnchorPane entireFrame;

    @FXML
    static AnchorPane skyboxUIPane;

    @FXML
    static AnchorPane skyboxPane;

    @FXML
    private Label welcomeText;


    @FXML
    static
    Scene uiSceneRoot, sceneRoot; //TODO not sure about this, right now trying to load the views as FXML files instead of scenes, but maybe scenes is the way to go


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }



    //loads in skybox application for - > this is the method making the call to SkyboxApplication
    @FXML
    private AnchorPane initialize()
    {

        try {
            skyboxPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/skybox-viewUI.fxml")));
            skyboxPane.setId("skyboxPane");

        } catch (IOException e) {
             String errorOutput = "error initializing Skybox in controller";
             System.out.println(errorOutput +e);
             e.printStackTrace();
        }


        //TODO extract the scene from SkyboxApplication
        // set the pane to display the scene from the SkyboxApplication
        //return skyboxPane that is constructed and passed to initialize to beging displaying it along with UI
        return skyboxPane;
    }
//
    @FXML
    protected static AnchorPane initializeUI()
    {


        try {
            skyboxUIPane =  FXMLLoader.load(Objects.requireNonNull(SkyboxController.class.getResource("/skybox-viewUI.fxml")));
            skyboxUIPane.setId("skyboxUIPane");
        } catch (IOException e) {
            String errorOutput = "error initializing UI Pane in controller";
            System.out.println(errorOutput + e);
            e.printStackTrace();
        }




        //TODO extract the scene from SkyboxApplication
        // set the pane to display the scene from the SkyboxApplication
        //return skyboxPane that is constructed and passed to initialize to beging displaying it along with UI
        return skyboxUIPane;
    }


    // this contains the code for starting up the skybox and linking the UI from FXML and visuals and code from java
        //handles the entire frame


    @FXML
    public AnchorPane initializeFrame(AnchorPane skyboxUIPane, AnchorPane skyboxPane) {
        try {
            entireFrame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/skybox-entireFrame.fxml")));
        } catch (IOException e) {
           String errorOutput = "error initializing entireFrame in controller";
            System.out.println(errorOutput + e);
            e.printStackTrace();
        }entireFrame = new AnchorPane(skyboxUIPane, skyboxPane);
         entireFrame.setId("entireFrame");

        return entireFrame;
    }


}