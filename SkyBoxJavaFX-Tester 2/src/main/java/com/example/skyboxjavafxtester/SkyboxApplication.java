package com.example.skyboxjavafxtester;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.chart.ScatterChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.shape.CullFace;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.ImagePattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.scene.AmbientLight;

//heavily inspired by open source codehttp://www.mscs.mu.edu/~mikes/cosc3550/demos/3D_SkyboxDemo/SkyboxDemo.java


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
    private SkyBox skyBox;



    private void constructWorld(Group root) {
        // AmbientLight light = new AmbientLight();
        AmbientLight light = new AmbientLight(Color.rgb(160, 160, 160));

        PointLight pl = new PointLight();
        pl.setTranslateX(100);
        pl.setTranslateY(-100);
        pl.setTranslateZ(-100);
        root.getChildren().add(pl);


        try {
            Image skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/IdeaProjects/SkyBoxJavaFX-Tester/src/main/resources/skyboxDesert.png"));
            SkyBox skyBox = new SkyBox(skyboxImage);
            TriangleMesh cube = new TriangleMesh();
            
            MeshView skyBoxView = new MeshView(cube);

            root.getChildren().addAll(skyBoxView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }






        //original skybox code with six sided image being displayed 6 times instead of folded
        //Image back = new Image("skyboxDesert.png");

        //Box skybox = new Box(10000, 10000, 10000);
        //Sphere skybox = new Sphere(5000);


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
                0, 0, 0,            // Point 0 - Top
                0, h, -s / 2,         // Point 1 - Front
                -s / 2, h, 0,            // Point 2 - Left
                s / 2, h, 0,            // Point 3 - Right
                0, h, s / 2           // Point 4 - Back
        );
        // define faces
        pyramidMesh.getFaces().addAll(
                0, 0, 2, 1, 1, 2,          // Front left face
                0, 0, 1, 1, 3, 1,          // Front right face
                0, 0, 3, 1, 4, 2,          // Back right face
                0, 0, 4, 1, 2, 2,          // Back left face
                4, 1, 1, 4, 2, 2,          // Bottom left face
                4, 1, 3, 3, 1, 4           // Bottom right face
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
        box.setTranslateY(-20);
        box.setTranslateZ(-20);

        root.getChildren().addAll(xAxis, yAxis, zAxis);

        root.getChildren().addAll(sphere2, sphere, box, light);

    }



    @Override
    public void start(Stage stage) throws IOException {
        //initiates the scene, environment and camera

                //TODO directory for filesource is currently hard coded to path.

            //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
            // right now we are just setting up skybox
        //FXMLLoader fxmlLoader = new FXMLLoader(SkyboxApplication.class.getResource("skybox-view.fxml"));

        Image skyboxImage = new Image(new FileInputStream("/Users/katiepalmer/IdeaProjects/SkyBoxJavaFX-Tester/src/main/resources/skyboxDesert.png"));

        SkyBox skyBox = new SkyBox(skyboxImage);

        Group sceneRoot = new Group();
        constructWorld(sceneRoot);







        int sceneWidth = 600; int sceneHeight = 600;



        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);

        //scene.setFill(new ImagePattern(skyboxImage));
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

    private static class SkyBox {

            private final ObservableIntegerArray faces = FXCollections.observableIntegerArray();
            private final ObservableFloatArray texCoords = FXCollections.observableFloatArray();
            private final ObservableFloatArray points = FXCollections.observableFloatArray();

            private final double WIDTH, HEIGHT, DEPTH;
            private TriangleMesh cube;
            private MeshView skyBox;
            private float x0, x1, x2, x3, x4, y0, y1, y2, y3;
            private Image skyboxImage;

            public SkyBox(Image skyboxImage) {
                this(20000, 20000, 20000, skyboxImage);
            }/*  ww  w    . d   e m   o  2 s .    c o   m*/

            private SkyBox(double w, double h, double d, Image skyboxImage) {
                this.WIDTH = w;
                this.HEIGHT = h;
                this.DEPTH = d;
                this.cube = new TriangleMesh();
                this.skyBox = new MeshView();
                PhongMaterial skyMaterial = new PhongMaterial();
                skyMaterial.setSpecularColor(Color.RED);
                skyMaterial.setDiffuseMap(skyboxImage);
                this.skyboxImage = skyboxImage;
                this.calculatePoints();
                this.calculateTexCoords();
                this.calculateFaces();


                this.skyBox.setMesh(cube);
                this.skyBox.setMaterial(skyMaterial);
                this.skyBox.setCullFace(CullFace.NONE);
                //this.getChildren().add(skyBoxView);
            }

            private void calculatePoints() {
                float hw = (float) WIDTH / 2f;
                float hh = (float) HEIGHT / 2f;
                float hd = (float) DEPTH / 2f;

                points.addAll(hw, hh, hd, hw, hh, -hd, hw, -hh, hd, hw, -hh, -hd, -hw, hh, hd, -hw, hh, -hd, -hw, -hh, hd,
                        -hw, -hh, -hd);
                cube.getPoints().addAll(points);

            }

            private void calculateFaces() {
                faces.addAll(0, 10, 2, 5, 1, 9, 2, 5, 3, 4, 1, 9, 4, 7, 5, 8, 6, 2, 6, 2, 5, 8, 7, 3, 0, 13, 1, 9, 4, 12, 4,
                        12, 1, 9, 5, 8, 2, 1, 6, 0, 3, 4, 3, 4, 6, 0, 7, 3, 0, 10, 4, 11, 2, 5, 2, 5, 4, 11, 6, 6, 1, 9, 3,
                        4, 5, 8, 5, 8, 3, 4, 7, 3);
                cube.getFaces().addAll(faces);
            }

            private void calculateTexCoords() {
                x0 = 0f;
                x1 = 1 / 4f;
                x2 = 2 / 4f;
                x3 = 3 / 4f;
                x4 = 1f;
                y0 = 0f;
                y1 = 1 / 3f;
                y2 = 2 / 3f;
                y3 = 1f;
                // x4 = 0; x3 = iw * 0.25f; x2 = iw / 2.0f; x1 = iw * 0.75f; x0 = iw;
                // y3 = 0; y2 = ih * 0.33f; y1 = ih * 0.66f; y0 = ih;

                texCoords.addAll((x1 + 0.001f), (y0 + 0.001f), (x2 - 0.001f), y0, (x0), (y1 + 0.001f), (x1 + 0.001f),
                        (y1 + 0.001f), (x2 - 0.001f), (y1 + 0.001f), x3, (y1 + 0.001f), (x4), (y1 + 0.001f), (x0),
                        (y2 - 0.001f), (x1 + 0.001f), (y2 - 0.001f), x2, (y2 - 0.001f), x3, (y2 - 0.001f), (x4),
                        (y2 - 0.001f), (x1 + 0.001f), (y3 - 0.001f), x2, (y3 - 0.001f));
                cube.getTexCoords().addAll(texCoords);
            }

            public double getWidth() {
                return WIDTH;
            }

            public double getHeight() {
                return HEIGHT;
            }

            public double getDepth() {
                return DEPTH;
            }
        }
    }
