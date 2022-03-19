package com.example.skyboxjavafxtester;

import javafx.application.Application;
import javafx.collections.ObservableIntegerArray;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.luckycatlabs.sunrisesunset.*;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;



public class SkyBoxApplication extends Application {


    static Group skybox = new Group();

    private static Image skyboxImage;
    private static TriangleMesh cubeMesh;
    //camera controls and scene settings declarations
    private PerspectiveCamera camera;
    private Group cameraDolly;
    private final double cameraQuantity = 10.0;
    private static final int WIDTH = 680;
    private static final int HEIGHT = 849;
    private static final int DEPTH = 700;

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
    private double size;


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
    private static final File house = new File("C:\\House.3ds");
    private static final File solarPanel = new File("C:\\SolarPanel(Export).3ds");
    private static final File groundSolarPanel = new File("C:\\GroundSolarPanel.3ds");
    private static Group solarPanelImport;
    private static Group gPanelOne;
    private static Group gPanelTwo;
    private static Group houseImport;
    static Group solarPanelOnewR;
    static Group solarPanelTwowR;
    static Group solarPanelThreewR;
    static Group solarPanelFourwR;
    static Group gPanelOneBox;
    static Group gPanelTwoBox;
    static Group panelsWHouse;
    static Group sun;
    private Boolean oneSelected = false;
    private Boolean twoSelected = false;
    static PhongMaterial clear = new PhongMaterial(Color.TRANSPARENT);

    //Location and Dates
    static String sunriseTime;
    static String sunsetTime;
    static Calendar cal;
    static String theDate = "20220310";
    static String theLocation;
    static String timeZone = "GMT-8";
    static Location location;
    static Double latitude = 47.6588;
    static Double longitude = -117.4260;
    static Date date;

    //Sun movement variables
    static int timeAS;
    static int timeBS;
    static int sliceofday;
    static int totalMinutes = 720;
    static int sunrise = 0;
    static int sunset = 720;
    static boolean box1closest;
    static boolean box2closest;
    static boolean box3closest;
    static boolean box4closest;
    static boolean gbox1closest;
    static boolean gbox2closest;
    static PhongMaterial optimal = new PhongMaterial(Color.GREEN);
    static PhongMaterial subOptimal = new PhongMaterial(Color.RED);

    private AnchorPane sliderAndDate;
    private AnchorPane uiPane;
    private Label label;


    //private final double WIDTH, HEIGHT, DEPTH;
    private static TriangleMesh cube;
    private MeshView skyBox;
    private static float x0;
    private static float x1;
    private static float x2;
    private static float x3;
    private static float x4;
    private static float y0;
    private static float y1;
    private static float y2;
    private static float y3; //values for points of skybox

    //private final Image textureImage;


    //private static final double depth = skyboxImage.getDepth(); //MAY NOT NEED FOR cube since shoudl scale evenly
    private static final ObservableIntegerArray faces = FXCollections.observableIntegerArray();
    private static final ObservableFloatArray texCords = FXCollections.observableFloatArray();
    private static final ObservableFloatArray points = FXCollections.observableFloatArray();



    static Group root = new Group();
    {
        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxExample.png"));
            //TODO confirm if I need this, I THINK it helps with blending the photo together for the skybox corners .
//           final double width = skyboxImage.getWidth();
//            final double height = skyboxImage.getHeight();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SkyBoxApplication.class.getResource("skybox-viewUI.fxml"));
        Pane entireFrame = new Pane();
        Pane skyboxPane = new Pane();
        try {
            skyboxPane = SkyBoxController.setSkyboxPane();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //TODO: make thie borderpane the root, but load the fxml
//        double width = skyboxImage.getWidth();
//        double height = skyboxImage.getHeight();

        //  createSkybox(root);
           //constructWorld(root); //lights

//        createSun(root);
//        sunriseSunset();
        //    modifySkybox(root);

//        Group panelsWHouse = addSolarPanel(root);

        entireFrame.getChildren().add(fxmlLoader.load());
       // skyboxPane.getChildren().addAll((Collection<? extends Node>) skyBox);
        entireFrame.getChildren().addAll(skyboxPane);

//        SubScene subScene = new SubScene(skyBox, 768, 600);

//        PerspectiveCamera camera = new PerspectiveCamera();
//        camera.setNearClip(0.1);
//        camera.setFarClip(30000.0);

//        subScene.setCamera(camera);
//        subScene.setRoot(skyBox);

        root.getChildren().addAll(entireFrame, skyboxPane);
        Scene scene = new Scene(root, 1024, 768); // Make the whole scene with everything
        cameraAndControls(root, panelsWHouse, scene);
        scene.setRoot(root);

        /*
        // This needs to set up the inside of the skyboxPane?
        scene.setFill(new ImagePattern(skyboxImage)); //THIS causes whole UI to get filled over, because we are adding it to the scene which encompassdes everything, could translate or
      *(
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);
        //sceneRoot.getScene().setCamera(camera);
        root.getScene().setCamera(camera);
        // translations through dolly
        */

        stage.setTitle("Solar Optimization Simulator!");
        stage.setScene(scene);
        stage.show();
    }

    private void cameraAndControls(Group root, Group panelsWHouse, Scene scene) {
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
            //TODO we do have combo of
        root.getChildren().add(cameraDolly);
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

            // Translate pivot = new Translate(); //TODO could use for a more elegant rotation of the camera

            Rotate r = new Rotate(-1, Rotate.Y_AXIS); //rotate house right
            Rotate l = new Rotate(1, Rotate.Y_AXIS); //rotate house left
            Rotate n = new Rotate(45, Rotate.Y_AXIS); //rotate Ground Panel One
            Rotate n1 = new Rotate(45, Rotate.Y_AXIS); //rotate Ground Panel Two

            setCenters(r, houseImport); //Get centers to rotate from center
            setCenters(l, houseImport);
            setCenters(n, gPanelOne);
            setCenters(n1, gPanelTwo);

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
            if(keycode == KeyCode.DIGIT0){ //Clears selected panels
                clearSelected();
            }
            if(keycode == KeyCode.DIGIT1){ // Selects ground panel number 1
                gPanelOneSelected();
            }
            if(keycode == KeyCode.DIGIT2) //selects ground panel number 2
            {
                gPanelTwoSelected();
            }
            if (keycode == KeyCode.RIGHT) { //Move selected ground panel to the right in the screen
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() + 1);
                }
                else if(twoSelected == true){
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() + 1);
                }
            }

            if (keycode == KeyCode.LEFT) { //Move selected ground panel to the left in the screen
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() - 1);
                }
                else if(twoSelected == true){
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() - 1);
                }
            }

            if (keycode == KeyCode.UP) { // Move selected ground panel back
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() + 1);
                }
                else if(twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() + 1);
                }
            }

            if (keycode == KeyCode.DOWN) { // Move selected ground panel forward
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() - 1);
                }
                else if(twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() - 1);
                }
            }

            if (keycode == KeyCode.SPACE) {  //Rotate selected ground solar panel
                if(oneSelected == true) {
                    t = t.createConcatenation(n);
                    gPanelOneBox.getTransforms().addAll(t);
                }
                else if(twoSelected == true) {
                    t = t.createConcatenation(n1);
                    gPanelTwoBox.getTransforms().addAll(t);
                }
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

    static void constructWorld(Group skyBox) {
        // AmbientLight light = new AmbientLight();
        AmbientLight light = new AmbientLight(Color.rgb(160, 160, 160));

        PointLight pl = new PointLight();
        pl.setTranslateX(1000);
        pl.setTranslateY(-100);
        pl.setTranslateZ(-100);
        skyBox.getChildren().add(pl);

        //TODO delete this once mesh is implemented -- Mark: 3/11
        skyBox.getChildren().add(light);
    }

    // Example converted from JavaFX for Dummies from triangle mesh to cube mesh
    public static Group createSkybox(Group skyboxGroup)
    {
       TriangleMesh cube = new TriangleMesh();
        //TODO NOTE: this is messy since i've been trying a few different approahces.
        Image textureImage = skyboxImage;
       // loadImageViews(); //folded skybox

//        TriangleMesh cube = createMesh(WIDTH, HEIGHT, DEPTH);
//        calculatePoints();
//        calculateTexCords();
//        calculateFaces();

//        MeshView cubeMesh= new MeshView(cube);
//        cubeMesh.setOpacity(.75);

        PhongMaterial skyboxMaterial = new PhongMaterial();

        skyboxMaterial.setSpecularColor(Color.TRANSPARENT);
        skyboxMaterial.setDiffuseMap(textureImage);

        Box box = new Box(WIDTH, HEIGHT, DEPTH);

        box.setMaterial(skyboxMaterial);

        box.setTranslateX(500);
        box.setTranslateY(400);
        box.setTranslateZ(200);
        box.setScaleX(1.5);
        box.setScaleY(1.5);
        box.setScaleZ(1.5);

//        cubeMesh.setTranslateX(1000);
//        cubeMesh.setTranslateY(400);
//        cubeMesh.setTranslateZ(200);
        box.setCullFace(CullFace.FRONT);
//        cubeMesh.setCullFace(CullFace.NONE);
//        cubeMesh.setMaterial(skyboxMaterial);
        //TODO  maybe try something like:
            //getpoints/add points, etc... then adding into start or initialize?

        //skyboxGroup.getChildren().add(cubeMesh);
       skyboxGroup.getChildren().add(box);

       return skyboxGroup;
    }

//    private static TriangleMesh createMesh(int WIDTH, int HEIGHT, int DEPTH) {
//
//        calculatePoints();
//        calculateFaces();
//        calculateTexCords();
//
//        return cubeMesh;
//    }


    //skybox meshview implementation pulled and made specific from https://www.demo2s.com/java/javafx-trianglemesh-tutorial-with-examples.htm

//    private static void calculatePoints() {
//        float hw = (float) WIDTH/2;
//        float hh = (float) HEIGHT/2;
//        float hd = (float) DEPTH/2;
//
//
//    //triangle mesh points: width, height, depth
//        points.addAll(hw, hh, hd,
//                hw, hh, -hd,
//                hw, -hh, hd,
//                hw, -hh, -hd,
//                -hw, hh, hd,
//                -hw, hh, -hd,
//                -hw, -hh, hd,
//                -hw, -hh, -hd);
//        cube.getPoints().addAll(points);
//
//    }
//    //Below is for setting faces for the values of the cubeMesh: uses 6 each, for each of the 6 faces of the cube
//    private static void calculateFaces() {
//        faces.addAll(0, 10, 2, 5, 1, 9,
//                2, 5, 3, 4, 1, 9,
//                4, 7, 5, 8, 6, 2,
//                6, 2, 5, 8, 7, 3,
//                0, 13, 1, 9, 4, 12,
//                4, 12, 1, 9, 5, 8,
//                2, 1, 6, 0, 3, 4,
//                3, 4, 6, 0, 7, 3,
//                0, 10, 4, 11, 2, 5,
//                2, 5, 4, 11, 6, 6,
//                1, 9, 3, 4, 5, 8,
//                5, 8, 3, 4, 7, 3);
//        cube.getFaces().addAll(faces);
//    }
//    //texture coords are x,y coords
//    private static void calculateTexCords() {
//        float x0 = 0f;
//        float x1 = 1f / 4f;
//        float x2 = 2f / 4f;
//        float x3 = 3f / 4f;
//        float x4 = 1f;
//        float y0 = 0f;
//        float y1 = 1f / 3f;
//        float y2 = 2f / 3f;
//        float y3 = 1f;
//
//
//
//        // x4 = 0; x3 = iw * 0.25f; x2 = iw / 2.0f; x1 = iw * 0.75f; x0 = iw;
//        // y3 = 0; y2 = ih * 0.33f; y1 = ih * 0.66f; y0 = ih;
//
//        texCords.addAll((x1 + 0.001f), (y0 + 0.001f), (x2 - 0.001f), y0, (x0), (y1 + 0.001f), (x1 + 0.001f),
//                (y1 + 0.001f), (x2 - 0.001f), (y1 + 0.001f), x3, (y1 + 0.001f), (x4), (y1 + 0.001f), (x0),
//                (y2 - 0.001f), (x1 + 0.001f), (y2 - 0.001f), x2, (y2 - 0.001f), x3, (y2 - 0.001f), (x4),
//                (y2 - 0.001f), (x1 + 0.001f), (y3 - 0.001f), x2, (y3 - 0.001f));
//        cube.getTexCoords().addAll(texCords);
//    }
//
//    public double getWidth() {
//        return WIDTH;
//    }
//
//    public double getHeight() {
//        return HEIGHT;
//    }
//
//    public double getDepth() {
//        return DEPTH;
//    }


    private static Group setHouse() {
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
        houseImport = new Group(oneStoryHouse); //create new group with the house
        return houseImport;
    }

    private static Group setAllSolarPanels(File solar, int pX, int pY, int pZ, int AY, int AX, int AZ) //----Model Helper Method----//
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

    private static Box createSolar(Group group1, double height, double depth, double width, double rax, double raz, double ray) {
        Box box = new Box();
        Bounds cord = group1.getBoundsInLocal();
        box.getTransforms().setAll(new Rotate(ray, Rotate.Y_AXIS), new Rotate(rax, Rotate.X_AXIS), new Rotate(raz, Rotate.Z_AXIS));
        box.setTranslateX(cord.getCenterX());
        box.setTranslateZ(cord.getCenterZ());
        box.setTranslateY(cord.getCenterY());
        box.setHeight(height);
        box.setDepth(depth);
        box.setWidth(width);
        box.setMaterial(clear);
        return box;
    }

    private void setCenters(Rotate r, Group beingRotated) {
        r.setPivotX(beingRotated.getBoundsInLocal().getCenterX());
        r.setPivotY(beingRotated.getBoundsInLocal().getCenterY());
        r.setPivotZ(beingRotated.getBoundsInLocal().getCenterZ());
    }

    private void gPanelOneSelected() {
        oneSelected = true;
        twoSelected = false;
    }

    private void gPanelTwoSelected() {
        oneSelected = false;
        twoSelected = true;
    }

    private void clearSelected() {
        oneSelected = false;
        twoSelected = false;
    }

    static void startParams() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //Formatter
        date = formatter.parse(theDate); //Parse string to create Date object
        cal = Calendar.getInstance(); //Calendar object created
        cal.setTime(date); //Calender object given corresponding date

        location = new Location(latitude.doubleValue(), longitude.doubleValue()); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timeZone); // Creates calculator for sun times

        sunriseTime = calculator.getOfficialSunriseForDate(cal); // Gets sunrise based on date and calculator created
        sunsetTime = calculator.getOfficialSunsetForDate(cal); // Gets sunset based on date and calculator created
    }

    static Group models() {

        int rightSideAngles[] = {-68, -68, 0};

        int panelOneCoordinates[] = {300, -74, 190};
        int panelTwoCoordinates[] = {395, -74, 400};

        int leftSideAngles[] = {-68, -113, 0};
        int panelThreeCoordinates[] = {190, -43, 250};
        int panelFourCoordinates[] = {275, -43, 440};

        int gPanelOneCoordinates[] = {0, 180, 190};
        int gPanelTwoCoordinates[] = {460, 180, 100};
        int gPanelLeftAngles[] = {115, -90, 0};
        int gPanelRightAngles[] = {-65, -90, 0};
//                                105
        //Sets house and panels into scene
        Group houseImport = setHouse();
        Group solarPanelOne = setAllSolarPanels(solarPanel, panelOneCoordinates[0], panelOneCoordinates[1], panelOneCoordinates[2], rightSideAngles[0], rightSideAngles[1], rightSideAngles[2]); //4 roof panels
        Group solarPanelTwo = setAllSolarPanels(solarPanel, panelTwoCoordinates[0], panelTwoCoordinates[1], panelTwoCoordinates[2], rightSideAngles[0], rightSideAngles[1], rightSideAngles[2]);
        Group solarPanelThree = setAllSolarPanels(solarPanel, panelThreeCoordinates[0], panelThreeCoordinates[1], panelThreeCoordinates[2], leftSideAngles[0], leftSideAngles[1], leftSideAngles[2]);
        Group solarPanelFour = setAllSolarPanels(solarPanel, panelFourCoordinates[0], panelFourCoordinates[1], panelFourCoordinates[2], leftSideAngles[0], leftSideAngles[1], leftSideAngles[2]);
        gPanelOne = setAllSolarPanels(groundSolarPanel, gPanelOneCoordinates[0], gPanelOneCoordinates[1], gPanelOneCoordinates[2], gPanelLeftAngles[0], gPanelLeftAngles[1], gPanelLeftAngles[2]); //2 ground panels
        gPanelTwo = setAllSolarPanels(groundSolarPanel, gPanelTwoCoordinates[0], gPanelTwoCoordinates[1], gPanelTwoCoordinates[2], gPanelRightAngles[0], gPanelRightAngles[1], gPanelRightAngles[2]);

        //sets boxes with panels
        Box boxers = createSolar(solarPanelOne, 39, 3.64, 65, rightSideAngles[1], rightSideAngles[2], rightSideAngles[0]);
        Box boxers2 = createSolar(solarPanelTwo, 39, 3.64, 65, rightSideAngles[1], rightSideAngles[2], rightSideAngles[0]);
        Box boxers3 = createSolar(solarPanelThree, 39, 3.64, 65, -rightSideAngles[1], -rightSideAngles[2], rightSideAngles[0]);
        Box boxers4 = createSolar(solarPanelFour, 39, 3.64, 65, -rightSideAngles[1], -rightSideAngles[2], rightSideAngles[0]);
        Box boxers5 = createSolar(gPanelOne, 39, 3.64, 130, 55, 0, -65);
        Box boxers6 = createSolar(gPanelTwo, 39, 3.64, 130, -55, 0, -65);

        //Grouping together solar panel w/ respective box
        solarPanelOnewR = new Group(solarPanelOne, boxers);
        solarPanelTwowR = new Group(solarPanelTwo, boxers2);
        solarPanelThreewR = new Group(solarPanelThree, boxers3);
        solarPanelFourwR = new Group(solarPanelFour, boxers4);
        gPanelOneBox = new Group(gPanelOne, boxers5);
        gPanelTwoBox = new Group(gPanelTwo, boxers6);

        panelsWHouse = new Group(houseImport, solarPanelOnewR, solarPanelTwowR, solarPanelThreewR, solarPanelFourwR, gPanelOneBox, gPanelTwoBox);
        panelsWHouse.setTranslateY(500); // puts house at ground level.. If you comment this it removes models on screen
        panelsWHouse.setTranslateX(400); // puts house at ground level.. If you comment this out it removes models on screen
        return panelsWHouse;
    }

    static void sunCreation() {
        Sphere sphere = new Sphere(80.0f);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.YELLOW);
        sphere.setMaterial(material);

        // create a point light
        PointLight pointlight = new PointLight();

        // create a Group
        sun = new Group(sphere, pointlight);

        sphere.setTranslateX(100);
        sphere.setTranslateY(-200);
        pointlight.setTranslateZ(-1000);
        pointlight.setTranslateX(+1000);
        pointlight.setTranslateY(+10);
        pointlight.setColor(Color.rgb(255, 255, 255));
        pointlight.setLightOn(true);
    }

    static void sunTrajectory(Double sliderValue) {
        double x;
        double y;
        double angle;
        double angleRadians;

        if(sliderValue == 0)
        {
            //Suns start position
            angle = 0;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 0.5)
        {
            angle = 7.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.0)
        {
            angle = 15;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.5)
        {
            angle = 22.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.0)
        {
            angle = 30;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.5) {
            angle = 37.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.0)
        {
            angle = 45;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.5)
        {
            angle = 52.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.0)
        {
            angle = 60;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.5)
        {
            angle = 67.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.0)
        {
            angle = 75;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.5)
        {
            angle = 82.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.0)
        {
            angle = 90;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.5)
        {
            angle = 97.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.0)
        {
            angle = 105;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.5)
        {
            angle = 112.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.0)
        {
            angle = 120;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.5)
        {
            angle = 127.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.0)
        {
            angle = 135;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.5)
        {
            angle = 142.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.0)
        {
            angle = 150;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.5)
        {
            angle = 157.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.0)
        {
            angle = 165;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.5)
        {
            angle = 172.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.0)
        {
            angle = 178;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.5)
        {
            angle = 179;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 13)
        {
            angle = 180;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }

    }

    //helper methods for most optimal
    public static double distancecalc(Box box, Group sun) {
        Point3D point1 = new Point3D(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
        Point3D point2 = new Point3D(sun.getTranslateX(), sun.getTranslateY(), sun.getTranslateZ());
        Double distance = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2) + Math.pow(point1.getZ() - point2.getZ(), 2));
        return distance;
    }

    public static void colorSetOpt() {
        double total = 0.0;
        double averageP1 = 0.0;
        double averageP2 = 0.0;
        double averageP3 = 0.0;
        double averageP4 = 0.0;
        double averageGP1 = 0.0;
        double averageGP2 = 0.0;
        double[] totalHours = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0};

        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelOnewR.getChildren().get(1), sun);
        }
        averageP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelTwowR.getChildren().get(1), sun);
        }
        averageP2 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelThreewR.getChildren().get(1), sun);
        }
        averageP3 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelFourwR.getChildren().get(1), sun);
        }
        averageP4 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) gPanelOneBox.getChildren().get(1), sun);
        }
        averageGP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) gPanelTwoBox.getChildren().get(1), sun);
        }
        averageGP2 = total / 12;

        box1closest = false;
        box2closest = false;
        box3closest = false;
        box4closest = false;
        gbox1closest = false;
        gbox2closest = false;

        if (averageP1 > averageP2 && averageP1 > averageP3 && averageP1 > averageP4) {
            box1closest = true;
            box2closest = false;
            box3closest = false;
            box4closest = false;
        }
        if (averageP2 > averageP1 && averageP2 > averageP3 && averageP2 > averageP4) {
            box1closest = false;
            box2closest = true;
            box3closest = false;
            box4closest = false;
        }
        if (averageP3 > averageP1 && averageP3 > averageP2 && averageP3 > averageP4) {
            box1closest = false;
            box2closest = false;
            box3closest = true;
            box4closest = false;
        }
        if (averageP4 > averageP1 && averageP4 > averageP2 && averageP4 > averageP3) {
            box1closest = false;
            box2closest = false;
            box3closest = false;
            box4closest = true;
        }
        if (averageGP1 > averageGP2) {
            gbox1closest = true;
            gbox2closest = false;
        }
        if (averageGP2 > averageGP1) {
            gbox1closest = false;
            gbox2closest = true;
        }

        if (box1closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box2closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);

        }
        if (box3closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box4closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(optimal);
        }
        if (gbox1closest = true) {
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(optimal);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (gbox2closest = true) {
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(optimal);
        }
    }

    static double calculateLightIntesity(Box box, Group Sun){
        double distance = Math.abs(distancecalc(box, Sun));
        double intesity = 1/((distance)*(distance));
        intesity = intesity*10000000;
        return intesity;
    }

    //skybox loading and blending of the images to make it look seamless (I think)
    private void loadImageViews() {
        for(ImageView imageViews : views)
        {
            imageViews.setSmooth(true);

            imageViews.setScaleX(10);
            imageViews.setScaleY(10);
            imageViews.setScaleZ(10);
            imageViews.setPreserveRatio(true);
        }
        buildSkyboxFromImage();
    }
    //for folding skybox from imported 4x3 image

    //this is what the importing image should look like:
    /*
     *              ____
     *             |top |
     *         ____|____|____ ____
     *        |left|fwd |rght|back|
     *        |____|____|____|____|
     *             |bot |
     *             |____|
     *
     */

    //loadImageViewPorts - builds skybox
    public void buildSkyboxFromImage()
    {
        //layoutViews(); //top, back, left...

        //TODO consider if the below is an overcomplication and if you NEED to use a constructor.
        //note that the box we pass in has x,y,z  already there. add diffuse image and call it a day. Assume size is correct?


        // Box skyboxBox = new Box(10000, 10000, 10000);
        //Sphere skybox = new Sphere(5000);




        //4 x 3 image divided by their own ratio should both = 1, validates size of incoming image, we also check when buildingsk=ybox, but what do you do
//        if(width/4 != height/3)
//            throw new UnsupportedOperationException("Image needs to be a 4x3 image. Sideways cross, see ");
        double width = WIDTH;
        double height = HEIGHT;
        size = width - height;
        recalculateSize(size);

        //setting up grids for
        double
                topx = size, topy =0,
                botx = size, boty = size*2,
                leftx = 0, lefty= size,
                rightx = size * 2, righty = size,
                fwdx = size, fwdy= size,
                backx = size *3, backy = size;

        //add top padding x+, y+, width-, height
        top.setViewport(new Rectangle2D(topx , topy , size, size ));

        //add left padding x, y+, width, height-
        left.setViewport(new Rectangle2D(leftx , lefty , size - 1, size - 1));

        //add front padding x+, y+, width-, height
        back.setViewport(new Rectangle2D(fwdx , fwdy, size , size));

        //add right padding x, y+, width, height-
        right.setViewport(new Rectangle2D(rightx, righty , size , size ));

        //add back padding x, y+, width, height-
        front.setViewport(new Rectangle2D(backx + 1, backy - 1, size - 1, size - 1));

        //add bottom padding x+, y, width-, height-
        bottom.setViewport(new Rectangle2D(botx, boty, size , size));


        for(ImageView view : views)
        {
            view.setImage(skyboxImage);
            // System.out.println(view.getId() + view.getViewport() + size);  //TODO just for testing correct outputs

        }
        final PhongMaterial skyMaterial = new PhongMaterial();
        skyMaterial.setSpecularColor(Color.TRANSPARENT);
        skyMaterial.setDiffuseMap(skyboxImage);
        root.getTransforms().add(affine);
        // views.setMaterial(skyMaterial);
        // views.setCullFace(CullFace.NONE);
        layoutViews();
        root.getChildren().addAll(views);

    }  //ensuring that our cells for our skybox image are appropriately sized before folding
    private void recalculateSize(double size)
    {
        double factor = Math.floor(getSize()/size);
        setSize(size * factor);
    }

    //build the box through translations and folding
    private void layoutViews()
    {
        for(ImageView view : views)
        {
            view.setFitWidth(getSize());
            view.setFitHeight(getSize());
        }


        back.setTranslateX(-0.5 * getSize());
        back.setTranslateY(-0.5 * getSize());
        back.setTranslateZ(-0.5 * getSize());


        front.setTranslateX(-0.5 * getSize());
        front.setTranslateY(-0.5 * getSize());
        front.setTranslateZ(0.5 * getSize());
        front.setRotationAxis(Rotate.Z_AXIS);
        front.setRotate(-180);
        front.getTransforms().add(new Rotate(180,front.getFitHeight() / 2, 0,0, Rotate.X_AXIS));
        front.setTranslateY(front.getTranslateY() - getSize());

        top.setTranslateX(-0.5 * getSize());
        top.setTranslateY(-1 * getSize());
        top.setRotationAxis(Rotate.X_AXIS);
        top.setRotate(-90);

        bottom.setTranslateX(-0.5 * getSize());
        bottom.setTranslateY(0);
        bottom.setRotationAxis(Rotate.X_AXIS);
        bottom.setRotate(90);

        left.setTranslateX(-1 * getSize());
        left.setTranslateY(-0.5 * getSize());
        left.setRotationAxis(Rotate.Y_AXIS);
        left.setRotate(90);

        right.setTranslateX(0);
        right.setTranslateY(-0.5 * getSize());
        right.setRotationAxis(Rotate.Y_AXIS);
        right.setRotate(-90);
    }

    public final double getSize()
    {
        return size;
    }

    public final void setSize(double value)
    {
        size = value;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
