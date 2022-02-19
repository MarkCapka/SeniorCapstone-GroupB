package com.example.skyboxjavafxtester;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.luckycatlabs.sunrisesunset.*;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    private final Affine affine = new Affine();
    //setting up for the folding of our image into a skybox
    private final ImageView
            top   = new ImageView(),
            bottom= new ImageView(),
            left  = new ImageView(),
            right = new ImageView(),
            back  = new ImageView(),
            front = new ImageView();
    {
        top.setId("top ");
        bottom.setId("bottom ");
        left.setId("left ");
        right.setId("right ");
        back.setId("back ");
        front.setId("front ");
    }

    //aggregating these views into a list
    private final ImageView[] views = new ImageView[]
            {
                    top, left, back, right, front, bottom
            };

    //Model Import Declaration
    private final File house = new File("C:\\House.3ds");
    private final File solarPanel = new File("C:\\SolarPanel(Export).3ds");
    private final File groundSolarPanel = new File("C:\\GroundSolarPanel.3ds");
    private Group solarPanelImport;

    //Location and Dates
    private String sunriseTime;
    private String sunsetTime;
    private Calendar cal;
    private String theDate; //User input date, if Date picker in UI gives date no reason for this string
    private Date date; //Input date turned into date object
    private Location location;


        Image skyboxImage;
    {
        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxExample.png"));
            skyboxImage.isSmooth(); //TODO confirm if I need this, I THINK it helps with blending the photo together for the skybox corners .

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void constructWorld(Group sceneRoot) {
        AmbientLight light = new AmbientLight(Color.rgb(200, 200, 200));
        PointLight pl = new PointLight();
        pl.setTranslateX(100);
        pl.setTranslateY(-100);
        pl.setTranslateZ(-100);
        sceneRoot.getChildren().add(pl);
        sceneRoot.getChildren().add(light);

        modifySkybox(sceneRoot);


    }

    private void modifySkybox(Group sceneRoot) {
        // Image back = new Image("skyboxExample.png"); //TODO this is the actual skybox image????
        final PhongMaterial skyMaterial = new PhongMaterial();
       // skyMaterial.setSpecularColor(Color.TRANSPARENT);
        skyMaterial.setDiffuseMap(skyboxImage);
        //TriangleMesh cube = new TriangleMesh();
       // skyMaterial.setDiffuseColor(Color.TRANSPARENT);
        //MeshView meshView = new MeshView();
        //cube = new TriangleMesh(skyboxImage)
        //TODO may change from box to a mesh cube since we can easily make that transparent
        Box skybox = new Box(10000, 10000, 10000);
        skybox.setMaterial(skyMaterial);

        skybox.setCullFace(CullFace.NONE);
        sceneRoot.getChildren().add(skybox);

    }

    //animations for sun movement setup, written so we can move objects with time generically
    private PathTransition createPathTransition(double second, Path path, Node node)
    {
        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(second));
        transition.setPath(path);
        transition.setNode(node);
        transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT); //this should keep our Sun perpindicular to the scene while on its path
        transition.setCycleCount(Timeline.INDEFINITE); //TODO change this to correlate with start and end of day?

        return transition;
    }
    //orbit for the sun to follow
    private Path createOrbitalPath(double centerX,
                                   double centerY,double radiusX, double radiusY,
                                   double rotate) {
        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radiusX + 1);
        arcTo.setY(centerY - radiusY);
        arcTo.setSweepFlag(false);
        arcTo.setLargeArcFlag(true);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);
        arcTo.setXAxisRotation(rotate);
        Path path = new Path();
        path.getElements()
                .add(new MoveTo(centerX - radiusX, centerY -
                        radiusY));
        path.getElements().add(arcTo);
        path.getElements().add(new ClosePath());
        path.setVisible(false);
        return path;
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


    private Box createSolar(Group group1, double height, double depth, double width, double rax, double raz, double ray){
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


    //TODO write an elliptical path for the sun to follow






    //TODO separate scenes -> sceneRoot = groundobjects,     maybe     sceneSun = sun + movement + light effects
        //kind of started doing this very roughly
            //sceneRoot = objects and environment
                //      scene = transformed sceneRoot with skybox and camera in environment


    @Override
    public void start(Stage stage) throws ParseException {
        //initiates the scene, environment and camera
            //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
            // right now we are just setting up skybox

        //TODO add FXML call and window with which to build skybox in.
            //TODO
        //FXMLLoader fxmlLoader = new FXMLLoader(SkyboxApplication.class.getResource("skybox-viewUI.fxml"));

        Group sceneRoot = new Group();
        double width = skyboxImage.getWidth();
        double height = skyboxImage.getHeight();
        constructWorld(sceneRoot);
        createSun(sceneRoot);
        sunriseSunset();

        modifySkybox(sceneRoot);

        //TODO make a group and call for animations of the sun.
         //  Group sunOrbit = new Group(second, path, sun);
       // createPathTransition(second, path, node);

        //sceneRoot.getTransforms().add(sunOrbit);





        //TODO methodize out scene and camera controls
        //-------------Scene and Camera set up----------------------------//
        //TODO change to 1024
        double sceneWidth = 1024;
        //TODO change to 768;
        double sceneHeight = 768;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);
        scene.setFill(Color.TRANSPARENT);    //Change this to change the background color. TODO leaving this in to explore setting the scene's background entirely as the skybox
                    //
        Group panelsWHouse = addSolarPanel(sceneRoot);
       // scene.setFill(new ImagePattern(skyboxImage));  //Background image
        cameraAndControls(sceneRoot, panelsWHouse, scene);

        //TODO I think this will be set in FXML, but maybe not
        stage.setTitle("Skybox");
        stage.setScene(scene);
        stage.show();
    }

    private void cameraAndControls(Group sceneRoot, Group panelsWHouse, Scene scene) {
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
        //----------------Controls & Camera Controls Section----------------------------//
        //TODO methodize out controls //NOTE i did try to split this from the scene and camera setup, but they were too intertwined at the time.
        // Use keyboard to control camera position
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            // What key did the user press?
            KeyCode keycode = event.getCode();
            Rotate r;
            Transform t = new Rotate();
          // Translate pivot = new Translate(); //TODO could use for a more elegant rotation of the camera
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
            //TODO make a path for the camera to follow in a sphere around the house based on current distance
            if (keycode == KeyCode.Q) {                     //rotate camera clockwise
                r = new Rotate(-1, Rotate.Y_AXIS);
                t = t.createConcatenation(r);
                camera.getTransforms().addAll(t);
            }
            //TODO make a path for the camera to follow in a sphere around the house based on current distance, opposite direction of Q
            if (keycode == KeyCode.E) {                     //rotate camera counterclockwise
                r = new Rotate(+1, Rotate.Y_AXIS);
                t = t.createConcatenation(r);
                camera.getTransforms().addAll(t);
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
    }

    private Group addSolarPanel(Group sceneRoot) {
        //-----------Sean and Anthony---------------------//
        //-----------Adding house and solar panels w/ box to scene -----------------//

        //Coordinates for placement of solar panels to look nice on the roof

        //int p1X = 300, p1Y = -74, p1Z = 190;
        //int p2X = 395, p2Y = -74, p2Z = 400;
        int rAY = -68, rAX = -68, rAZ = 0; // Right side of roof angles for solar panels
        int panelOneCoordinates[] = {300, -74, 190, rAY, rAX, rAZ};
        int panelTwoCoordinates[] = {395, -74, 400, rAY, rAX, rAZ};

        int p3X = 190, p3Y = -43, p3Z = 250;
        int p4X = 275, p4Y = -43, p4Z = 440;
        int lAY = -68, lAX = -113, lAZ = 0; // Left side of roof angles for solar panels

        int gP1X = 0, gP1Y = 180, gP1Z = 190; //Ground solar panel coordinates
        int gP2X = 460, gP2Y = 180, gP2Z = 100;
        int gAY = 105, gAX = -90, gAZ = 0;
        int gAYTwo = -60, gAXTwo = -90, gAZTwo = 0;

        Group houseImport = setHouse(); //create new group with the house

        Group solarPanelOne = setAllSolarPanels(solarPanel, panelOneCoordinates[0], panelOneCoordinates[1], panelOneCoordinates[2], rAY, rAX, rAZ); //4 roof panels
        Group solarPanelTwo = setAllSolarPanels(solarPanel, panelTwoCoordinates[0], panelTwoCoordinates[1], panelTwoCoordinates[2], rAY, rAX, rAZ);
        Group solarPanelThree = setAllSolarPanels(solarPanel, p3X, p3Y, p3Z, lAY, lAX, lAZ);
        Group solarPanelFour = setAllSolarPanels(solarPanel, p4X, p4Y, p4Z, lAY, lAX, lAZ);

        Group gPanelOne = setAllSolarPanels(groundSolarPanel, gP1X, gP1Y, gP1Z, gAY, gAX, gAZ); //2 ground panels
        Group gPanelTwo = setAllSolarPanels(groundSolarPanel, gP2X, gP2Y, gP2Z, gAYTwo, gAXTwo, gAZTwo);

        //Creating the boxes to match the solar panels
        Box boxers = createSolar(solarPanelOne, 39, 3.64, 65, rAX, rAZ, rAY);
        Box boxers2 = createSolar(solarPanelTwo, 39, 3.64, 65, rAX, rAZ, rAY);
        Box boxers3 = createSolar(solarPanelThree, 39, 3.64, 65, -rAX, -rAZ, rAY);
        Box boxers4 = createSolar(solarPanelFour, 39, 3.64, 65, -rAX, -rAZ, rAY);
        //Box boxers5 = createSolar(gPanelOne, 39, 3.64, 65, gAX, gAZ, gAY);

        //Grouping together solar panel w/ respective box
        Group solarPanelOnewR = new Group(solarPanelOne, boxers);
        Group solarPanelTwowR = new Group(solarPanelTwo, boxers2);
        Group solarPanelThreewR = new Group(solarPanelThree, boxers3);
        Group solarPanelFourwR = new Group(solarPanelFour, boxers4);
        //Group gPanelOnewR = new Group(gPanelOne, boxers5);

        Group panelsWHouse = new Group(solarPanelOnewR, solarPanelTwowR, solarPanelThreewR, solarPanelFourwR, gPanelOne, gPanelTwo, houseImport);
        sceneRoot.getChildren().add(panelsWHouse);
        //---------End of adding solar panels with boxes, and house to the scene-----------//
        return panelsWHouse;
    }

    //TODO check parse exception
    private void sunriseSunset() throws ParseException {
        //-------End of creating the sun-----------//
        //TODO methodize out sunrise times
        //------Getting sunset/sunrise time based on coordinates and date------//

        theDate = "20200419"; //Will be date extracted from date picker tool in UI
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //Formatter
        Date date = formatter.parse(theDate.toString()); //Parse string to create Date object
        cal = Calendar.getInstance(); //Calendar object created
        cal.setTime(date); //Calender object given corresponding date

        location = new Location(47.6588, -117.4260); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, "GMT-0700"); // Creates calculator for sun times //doesn't account for daylight savings

        sunriseTime = calculator.getOfficialSunriseForDate(cal); // Gets sunrise based on date and calculator created
        sunsetTime = calculator.getOfficialSunsetForDate(cal); // Gets sunset based on date and calculator created
        System.out.println(sunriseTime); //Testing :)  //TODO our sunriseTime and sunSet time might be pulling backwards, note: sunset prints first then sunrise
        System.out.println(sunsetTime);

        //--------------End of sunset/sunrise times---------------//
    }

    private void createSun(Group sceneRoot) {

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
//        sun.selfIllumination = new AmbientLight(); //TODO may need to change object that calls this, may want to do object sphere or maybe assign before grouping?
//        sun.emissive = pointlight;
//        sun.setSpecularPower(5);
        sphere.setTranslateX(1000);
        sphere.setTranslateY(-2000);
        pointlight.setTranslateZ(-10000);
        pointlight.setTranslateX(+10000);
        pointlight.setTranslateY(+10);
        pointlight.setColor(Color.YELLOWGREEN);


        sceneRoot.getChildren().add(sun);
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



/*
        //BELOW IS JUST EXAMPLES:
//        final PhongMaterial greenMaterial = new PhongMaterial();
//        greenMaterial.setDiffuseColor(Color.FORESTGREEN);
//        greenMaterial.setSpecularColor(Color.LIMEGREEN);
//        Box xAxis = new Box(500, 10, 10);
//        xAxis.setMaterial(greenMaterial);
//        Box yAxis = new Box(10, 200, 10);
//        yAxis.setMaterial(greenMaterial);
//        Box zAxis = new Box(10, 10, 200);
//        zAxis.setMaterial(greenMaterial);
//
//        final PhongMaterial redMaterial = new PhongMaterial();
//        redMaterial.setDiffuseColor(Color.RED);
//        redMaterial.setSpecularColor(Color.TOMATO);
//        final Sphere sphere = new Sphere(30);
//        sphere.setMaterial(redMaterial);
//
//        sphere.setTranslateX(150);

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
*/