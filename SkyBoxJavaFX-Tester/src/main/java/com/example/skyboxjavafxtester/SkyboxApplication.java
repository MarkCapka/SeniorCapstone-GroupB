package com.example.skyboxjavafxtester;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import javafx.scene.transform.Transform;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
    private final File house = new File("C:\\House.3ds");
    private final File solarPanel = new File("C:\\SolarPanel(Export).3ds");
    private final File groundSolarPanel = new File("C:\\GroundSolarPanel.3ds");
    private Group solarPanelImport;


        Image skyboxImage;
    {
        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxDesert.png"));
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
        Box xAxis = new Box(500, 10, 10);
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
      //  root.getChildren().add(pyramid);



        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.WHITE);
        Box box = new Box(40, 60, 80);
        box.setMaterial(blueMaterial);

        box.setTranslateX(-30);
        box.setTranslateY(-20);
        box.setTranslateZ(-20);


        root.getChildren().add(light);

        Image back = new Image("skyboxDesert.png");
        final PhongMaterial skyMaterial = new PhongMaterial();
        skyMaterial.setDiffuseMap(back);
        Box skybox = new Box(10000, 10000, 10000);
        skybox.setMaterial(skyMaterial);
        skybox.setCullFace(CullFace.NONE);
        root.getChildren().add(skybox);

    }

    @Override
    public void start(Stage stage) {
        //initiates the scene, environment and camera

                //TODO directory for filesource is currently hard coded to path.
       // Image skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/IdeaProjects/SkyBoxJavaFX-Tester/src/main/resources/skyboxDesert.png"));

            //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
            // right now we are just setting up skybox
        //FXMLLoader fxmlLoader = new FXMLLoader(SkyboxApplication.class.getResource("skybox-viewUI.fxml"));

        Group sceneRoot = new Group();
        constructWorld(sceneRoot);

        //----------Anthony: Creating the sun-------------------//

        Sphere sphere = new Sphere(80.0f);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.YELLOWGREEN);
        sphere.setMaterial(material);

        // create a point light
        PointLight pointlight = new PointLight();

        // create a Group
        Group sun = new Group(sphere, pointlight);
        // translate the sphere to a position

        sphere.setTranslateX(100);
        sphere.setTranslateY(-200);
        pointlight.setTranslateZ(-1000);
        pointlight.setTranslateX(+1000);
        pointlight.setTranslateY(+10);
        pointlight.setColor(Color.YELLOWGREEN);
        sceneRoot.getChildren().add(sun);

        //-------End of creating the sun-----------//




        //-----------Sean and Anthony---------------------//
        //-----------Adding house and solar panels w/ box to scene -----------------//

        //Coordinates for placement of solar panels to look nice on the roof
        int p1X = 300, p1Y = -74, p1Z = 190;
        int p2X = 395, p2Y = -74, p2Z = 400;
        int rAY = -68, rAX = -68, rAZ = 0; // Right side of roof angles for solar panels

        int p3X = 190, p3Y = -43, p3Z = 250;
        int p4X = 275, p4Y = -43, p4Z = 440;
        int lAY = -68, lAX = -113, lAZ = 0; // Left side of roof angles for solar panels

        int gP1X = 0, gP1Y = 180, gP1Z = 190; //Ground solar panel coordinates
        int gP2X = 460, gP2Y = 180, gP2Z = 100;
        int gAY = 100, gAX = -90, gAZ = 0;
        int gAYTwo = -60, gAXTwo = -90, gAZTwo = 0;

        Group houseImport = setHouse(); //create new group with the house

        Group solarPanelOne = setAllSolarPanels(solarPanel, p1X, p1Y, p1Z, rAY, rAX, rAZ); //4 roof panels
        Group solarPanelTwo = setAllSolarPanels(solarPanel, p2X, p2Y, p2Z, rAY, rAX, rAZ);
        Group solarPanelThree = setAllSolarPanels(solarPanel, p3X, p3Y, p3Z, lAY, lAX, lAZ);
        Group solarPanelFour = setAllSolarPanels(solarPanel, p4X, p4Y, p4Z, lAY, lAX, lAZ);

        Group gPanelOne = setAllSolarPanels(groundSolarPanel, gP1X, gP1Y, gP1Z, gAY, gAX, gAZ); //2 ground panels
        Group gPanelTwo = setAllSolarPanels(groundSolarPanel, gP2X, gP2Y, gP2Z, gAYTwo, gAXTwo, gAZTwo);

        //Creating the boxes to match the solar panels
        Box boxers = createsolar(solarPanelOne, 39, 3.64, 65, rAX, rAZ, rAY);
        Box boxers2 = createsolar(solarPanelTwo, 39, 3.64, 65, rAX, rAZ, rAY);
        Box boxers3 = createsolar(solarPanelThree, 39, 3.64, 65, -rAX, -rAZ, rAY);
        Box boxers4 = createsolar(solarPanelFour, 39, 3.64, 65, -rAX, -rAZ, rAY);
        //Box boxers5 = createsolar(gPanelOne, 39, 3.64, 65, gAX, gAZ, gAY);

        //Grouping together solar panel w/ respective box
        Group solarPanelOnewR = new Group(solarPanelOne, boxers);
        Group solarPanelTwowR = new Group(solarPanelTwo, boxers2);
        Group solarPanelThreewR = new Group(solarPanelThree, boxers3);
        Group solarPanelFourwR = new Group(solarPanelFour, boxers4);
        //Group gPanelOnewR = new Group(gPanelOne, boxers5);

        Group panelsWHouse = new Group(solarPanelOnewR, solarPanelTwowR, solarPanelThreewR, solarPanelFourwR, gPanelOne, gPanelTwo, houseImport);
        sceneRoot.getChildren().add(panelsWHouse);
        //---------End of adding solar panels with boxes, and house to the scene-----------//




        //-------------Scene and Camera set up----------------------------//
        //TODO change to 1024
        double sceneWidth = 600;
        //TODO change to 768
        double sceneHeight = 600;
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
        //-------------END of Scene and Camera set up----------------------------//




        //----------------Controls Section----------------------------//

        // Use keyboard to control camera position
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            // What key did the user press?
            KeyCode keycode = event.getCode();
            Rotate r;
            Transform t = new Rotate();

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
            if (keycode == KeyCode.M) {

                r = new Rotate(1, Rotate.Y_AXIS); // Rotate House and Panels on/around Left
                t = t.createConcatenation(r);
                panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.N) { // Rotate House and Panels on/around Right
                r = new Rotate(-1, Rotate.Y_AXIS);
                t = t.createConcatenation(r);
                panelsWHouse.getTransforms().addAll(t);
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

    //------------------------Helper Methods----------------------------------------//
    private Group setHouse()
    {
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer

        modelImporter.read(house); //Read in the house model
        Node[] oneStoryHouse = modelImporter.getImport(); //create House object with Node[]
        modelImporter.clear(); // clear the importer

        for (Node node : oneStoryHouse) {
            node.setScaleX(1);
            node.setScaleY(1);
            node.setScaleZ(1);
            node.getTransforms().setAll(new Rotate(25, Rotate.Y_AXIS), new Rotate(-90, Rotate.X_AXIS));
            node.setTranslateX(0); // These place the house towards the ground and to the right of the view
            node.setTranslateY(200); // ^^^^^^^^^^^^^^^
        }
        Group houseImport = new Group(oneStoryHouse); //create new group with the house
        return houseImport;
    }

    private Group setAllSolarPanels(File solar,int pX, int pY, int pZ, int AY, int AX, int AZ) //----Model Helper Method----//
    {
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer
        modelImporter.read(solar);
        Node[] model = modelImporter.getImport();

        for (Node node : model) {
            node.setScaleX(1);
            node.setScaleY(1);
            node.setScaleZ(1);                                                         //Slope of roof
            node.getTransforms().setAll(new Rotate(AY, Rotate.Y_AXIS), new Rotate(AX, Rotate.X_AXIS), new Rotate(AZ, Rotate.Z_AXIS));
            node.setTranslateX(pX); // Move right or left
            node.setTranslateY(pY); // Move Up or down ... Height of roof
            node.setTranslateZ(pZ); // Move forward or backward
        }
        solarPanelImport = new Group(model);
        return solarPanelImport;
    }

    private Box createsolar(Group group1, double height, double depth, double width, double rax, double raz, double ray){
        Box box = new Box();
        Bounds cord = group1.getBoundsInLocal();
        box.getTransforms().setAll(new Rotate(ray, Rotate.Y_AXIS), new Rotate(rax, Rotate.X_AXIS), new Rotate(raz, Rotate.Z_AXIS));
        box.setTranslateX(cord.getCenterX());
        box.setTranslateZ(cord.getCenterZ());
        box.setTranslateY(cord.getCenterY());
        box.setHeight(height);
        box.setDepth(depth);
        box.setWidth(width);
        return box;
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