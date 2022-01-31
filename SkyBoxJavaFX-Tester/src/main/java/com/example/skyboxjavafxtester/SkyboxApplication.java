package com.example.skyboxjavafxtester;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

//heavily inspired by http://www.mscs.mu.edu/~mikes/cosc3550/demos/3D_SkyboxDemo/SkyboxDemo.java


public class SkyboxApplication extends Application {

    //camera controls and scene settings declarations
    private PerspectiveCamera camera;
    private Group cameraDolly;
    private final double cameraQuantity = 10.0;

    //Mouse control variable declarations
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    //Model Import Declaration
    private final File house = new File("/Users/katiepalmer/Downloads/SeniorCapstone-GroupB-MarkCapka/SkyBoxJavaFX-Tester/src/main/resources/House.3DS");
    private final File solarPanel = new File("/Users/katiepalmer/Downloads/SeniorCapstone-GroupB-MarkCapka/SkyBoxJavaFX-Tester/src/main/resources/SolarPanel(Export).3DS");
    private Group solarPanelImport;


        Image skyboxImage;
    {
        try {
            skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/Downloads/SeniorCapstone-GroupB-MarkCapka/SkyBoxJavaFX-Tester/src/main/resources/skyboxExample.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void constructWorld(Group root) {
        // AmbientLight light = new AmbientLight();
        AmbientLight light = new AmbientLight(Color.rgb(160, 160, 160));

        PointLight pl = new PointLight();
        pl.setTranslateX(100);
        pl.setTranslateY(-100);
        pl.setTranslateZ(-100);
        root.getChildren().add(pl);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.FORESTGREEN);
        greenMaterial.setSpecularColor(Color.LIMEGREEN);
        Box xAxis = new Box(200, 10, 10);
        xAxis.setMaterial(greenMaterial);
        Box yAxis = new Box(10, 200, 10);
        yAxis.setMaterial(greenMaterial);
        Box zAxis = new Box(10, 10, 200);
        zAxis.setMaterial(greenMaterial);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.TOMATO);
        final Sphere sphere = new Sphere(30);
        sphere.setMaterial(redMaterial);

        sphere.setTranslateX(150);

        final PhongMaterial yellowMaterial = new PhongMaterial();
        yellowMaterial.setDiffuseColor(Color.rgb(200, 200, 0));
        // yellowMaterial.setDiffuseColor(Color.YELLOW);
        // yellowMaterial.setSpecularColor(Color.WHITE);
        final Sphere sphere2 = new Sphere(30);
        sphere2.setMaterial(yellowMaterial);
        // sphere2.setDrawMode(DrawMode.LINE);

        sphere2.setTranslateX(110);

        // Example from JavaFX for Dummies
        TriangleMesh pyramidMesh = new TriangleMesh();
        // define (a trivial) texture map
        pyramidMesh.getTexCoords().addAll(
                0.5f, 0,
                0, 0.5f,
                1, 0.5f,
                0, 1,
                1, 1
        );
        // define vertices
        float h = 100;                    // Height
        float s = 200;                    // Base hypotenuse
        pyramidMesh.getPoints().addAll(
                0,    0,    0,            // Point 0 - Top
                0,    h,    -s/2,         // Point 1 - Front
                -s/2, h,    0,            // Point 2 - Left
                s/2,  h,    0,            // Point 3 - Right
                0,    h,    s/2           // Point 4 - Back
        );
        // define faces
        pyramidMesh.getFaces().addAll(
                0,0,  2,1,  1,2,          // Front left face
                0,0,  1,1,  3,1,          // Front right face
                0,0,  3,1,  4,2,          // Back right face
                0,0,  4,1,  2,2,          // Back left face
                4,1,  1,4,  2,2,          // Bottom left face
                4,1,  3,3,  1,4           // Bottom right face
        );
        pyramidMesh.getFaceSmoothingGroups().addAll(
                1, 2, 3, 4, 5, 5);
        MeshView pyramid = new MeshView(pyramidMesh);
        //pyramid.setDrawMode(DrawMode.LINE);
        final PhongMaterial pyrMaterial = new PhongMaterial();
        //pyrMaterial.setDiffuseMap(new Image("pyr_tex.png")); //TODO missing this image, need to determine the diffuse map
        pyrMaterial.setDiffuseColor(Color.BLUE);
        pyrMaterial.setSpecularColor(Color.WHITE);
        pyramid.setMaterial(pyrMaterial);
        pyramid.setTranslateX(-50);
        pyramid.setTranslateY(-200);
        pyramid.setTranslateZ(0);
        root.getChildren().add(pyramid);

//		// Dump out all characteristics of the TriangleMesh
//		// for debugging purposes
//		TriangleMesh tm = pyramidMesh;
//		System.out.println("Faces: "+tm.getFaceElementSize());
//		System.out.println(tm.getFaces() );
//		System.out.println(tm.getFaceSmoothingGroups());
//		System.out.println("Normals: "+tm.getNormalElementSize());
//		System.out.println(tm.getNormals());
//		System.out.println("Points: "+tm.getPointElementSize());
//		System.out.println(tm.getPoints());
//		System.out.println("TexCoords: "+tm.getTexCoordElementSize());
//		System.out.println(tm.getTexCoords());

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        //blueMaterial.setDiffuseMap(back);
        blueMaterial.setSpecularColor(Color.WHITE);
        Box box = new Box(40, 60, 80);
        box.setMaterial(blueMaterial);
        //box.setCullFace(CullFace.NONE);
        // box.setDrawMode(DrawMode.LINE);

        box.setTranslateX(-30);
        box.setTranslateY(-30);
        box.setTranslateZ(-30);

        root.getChildren().addAll(xAxis, yAxis, zAxis);

        root.getChildren().addAll(sphere2, sphere, box, light);

        Image back = new Image("skyboxDesert.png"); //if the skybox is working we can remove this.

        final PhongMaterial skyMaterial = new PhongMaterial();
        skyMaterial.setDiffuseMap(skyboxImage);
        Box skybox = new Box(10000, 10000, 10000);
        //Sphere skybox = new Sphere(5000);
        skybox.setMaterial(skyMaterial);
        skybox.setCullFace(CullFace.NONE);
        root.getChildren().add(skybox);

    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        //initiates the scene, environment and camera

                //TODO directory for filesource is currently hard coded to path.
         Image skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/Downloads/SeniorCapstone-GroupB-MarkCapka/SkyBoxJavaFX-Tester/target/classes/skyboxExample.png"));

            //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
            // right now we are just setting up skybox
        //FXMLLoader fxmlLoader = new FXMLLoader(SkyboxApplication.class.getResource("skybox-view.fxml"));

        Group sceneRoot = new Group();
        constructWorld(sceneRoot);

        //-----------SeanZ House Import ---------------------//
        //-----------Calls helper method that sets X, Y, Z variables for models. Method at bottom -----------------//
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer

        ///Anthony code
        Light.Distant light = new Light.Distant();

        //Setting the properties of the light source
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        //Instantiating the Lighting class
        Lighting lighting = new Lighting();

        //Setting the source of the light
        lighting.setLight(light);

        modelImporter.read(house); //Read in the house model
        Node[] oneStoryHouse = modelImporter.getImport(); //create House object with Node[]
        modelImporter.clear(); // clear the importer

        setHouseVariables(oneStoryHouse); //call to helper method

        Group houseImport = new Group(oneStoryHouse); //create new group with the house

        ///apply lighting effect on houese model
        houseImport.setEffect(lighting);

        sceneRoot.getChildren().add(houseImport); // add the house group to the scene

        //sceneRoot.setEffect(lighting);

        //--------------End of SeanZ House Import------------------//


        //TODO change to 1024
        double sceneWidth = 1024;
        //TODO change to 768
        double sceneHeight = 768;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);
        scene.setFill(new ImagePattern(skyboxImage));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);
        scene.setCamera(camera);
        // translations through dolly
        cameraDolly = new Group();
        cameraDolly.setTranslateZ(-1000);
        cameraDolly.setTranslateX(200);
        // rotation transforms
        Group turn = new Group();
        Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(xRotate);
        turn.getTransforms().addAll(yRotate);

        sceneRoot.getChildren().add(cameraDolly);
        cameraDolly.getChildren().add(turn);
        turn.getChildren().add(camera);

        //-------------- SeanZ Newest Addition----------------//
        //-------------- Buttons to add and remove solar panels in the scene----------------//
        //-------------- Can add or remove any number of solar panels----------------//
        //-------------- Need to un-hard code number of children in sceneroot group----------------//

        Button addSolarPanelToScene = new Button();
        addSolarPanelToScene.setText("Add Solar Panel");
        addSolarPanelToScene.setTranslateX(0);
        addSolarPanelToScene.setTranslateY(-250);

        addSolarPanelToScene.setOnAction(new EventHandler()
        {
            @Override
            public void handle(Event event)
            {
                modelImporter.read(solarPanel); //Read in the solar panel model
                Node[] theSolarPanel = modelImporter.getImport(); //create Solar Panel object with Node[]

                setSolarVariables(theSolarPanel); //call to helper method

                solarPanelImport = new Group(theSolarPanel); // create new group with the solar panel
                solarPanelImport.setEffect(lighting); // set lighting effects
                sceneRoot.getChildren().add(solarPanelImport); // add the solar panel group to the scene
            }
        });

        Button removeSolarPanel = new Button();
        removeSolarPanel.setText("Remove Solar Panel");
        removeSolarPanel.setTranslateX(300);
        removeSolarPanel.setTranslateY(-250);
        //final int allChildren = sceneRoot.getChildren().size(); Need to get number of children added

        removeSolarPanel.setOnAction(new EventHandler()
        {
            @Override
            public void handle(Event event)
            {
                int num = sceneRoot.getChildren().size() - 1;
                if(num > 13) //hardcoded number of children in group, index 13 is the remove button
                {
                    sceneRoot.getChildren().remove(num); // remove the most recent solar panel
                }
            }
        });

        sceneRoot.getChildren().add(addSolarPanelToScene); //adding both buttons to scene
        sceneRoot.getChildren().add(removeSolarPanel); // later should be out of skybox in ui bar

        //--------------End of SeanZ Newest Addition----------------//


        // Use keyboard to control camera position
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            // What key did the user press?
            KeyCode keycode = event.getCode();

            Point3D delta = null;
            if (keycode == KeyCode.COMMA) {
                delta = new Point3D(0, 0, change);
            }
            if (keycode == KeyCode.PERIOD) {
                delta = new Point3D(0, 0, -change);
            }
            if (keycode == KeyCode.A) {
                delta = new Point3D(-change, 0, 0);
            }
            if (keycode == KeyCode.D) {
                delta = new Point3D(change, 0, 0);
            }
            if (keycode == KeyCode.W) {
                delta = new Point3D(0, -change, 0);
            }
            if (keycode == KeyCode.S) {
                delta = new Point3D(0, change, 0);
            }
            if (delta != null) {
                Point3D delta2 = camera.localToParent(delta);
                cameraDolly.setTranslateX(cameraDolly.getTranslateX() + delta2.getX());
                cameraDolly.setTranslateY(cameraDolly.getTranslateY() + delta2.getY());
                cameraDolly.setTranslateZ(cameraDolly.getTranslateZ() + delta2.getZ());

            }
        });

        // Use mouse to control camera rotation
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            yRotate.setAngle(((yRotate.getAngle() - mouseDeltaX * 0.2) % 360 + 540) % 360 - 180); // +
            xRotate.setAngle(((xRotate.getAngle() + mouseDeltaY * 0.2) % 360 + 540) % 360 - 180); // -
        });

        stage.setTitle("Skybox");
        stage.setScene(scene);
        stage.show();
    }

    /* Sean Z
    House needs to be placed on the ground at input coordinates
     */
    private void setHouseVariables(Node[] model) //----Model Helper Method----//
    {
        for (Node node : model) {
            node.setScaleX(.6);
            node.setScaleY(.6);
            node.setScaleZ(.6);
            node.getTransforms().setAll(new Rotate(25, Rotate.Y_AXIS), new Rotate(-90, Rotate.X_AXIS));
            node.setTranslateX(0); // These place the house towards the ground and to the right of the view
            node.setTranslateY(200); // ^^^^^^^^^^^^^^^
        }
    }

    // Sean Z Helper method with solar panels. Puts new solar panels in random spots at the moment
    private void setSolarVariables(Node[] model) //----Model Helper Method----//
    {
        int min = 0;
        int max = 300;
        Random randomInt = new Random();
        int result = randomInt.nextInt((max-min) + min);

        for (Node node : model) {
            node.setScaleX(.6);
            node.setScaleY(.6);
            node.setScaleZ(.6);                      //90
            node.getTransforms().setAll(new Rotate(0, Rotate.Y_AXIS), new Rotate(15, Rotate.X_AXIS), new Rotate(25, Rotate.Z_AXIS));
            node.setTranslateX(result); // These place the house towards the ground and to the right of the view
            node.setTranslateY(200); // ^^^^^^^^^^^^^^^
        }
    }




/*
        //TODO below may be moved into skybox-view.fxml so we have control of all view functionality in one place
        ImageView imageView = new ImageView(skyboxImage);
        imageView.setX(50);
        imageView.setY(25);
        Group skyboxImages = new Group(imageView);
        stage.setTitle("Java Skybox");
        stage.setScene(scene);
        stage.show();*/


    public static void main(String[] args) {
        launch();
    }
}