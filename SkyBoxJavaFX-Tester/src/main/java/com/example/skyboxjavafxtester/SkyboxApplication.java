package com.example.skyboxjavafxtester;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
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
import java.io.IOException;
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
    private final Group sceneRoot = new Group();
    private final Group uiSceneRoot = new Group();
    private AnimationTimer timer;
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
    private Image skyboxImage;
    private double width;
    private double height;
    private double size;


//TODO remove the below constructor, it was becoming an issue so I removed it
//    //skybox constructor called SkyboxApplication to match name of class.
//    public SkyboxApplication(Image skyboxImage, double size, PerspectiveCamera camera)
//    {
//        super();
//        this.skyboxImage = skyboxImage;
//        this.size.set(size);
//        this.camera = camera;
//
////
////        this.cube = new TriangleMesh();
//        sceneRoot.getTransforms().add(affine);
//        loadImageViews();
//        sceneRoot.getChildren().addAll(views);
//
//        startTimer(); //TODO not positive we need this... might just be for coordinating the viwes for single image imports
//
//    }

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
        if(width/4 != height/3)
            throw new UnsupportedOperationException("Image needs to be a 4x3 image. Sideways cross, see ");

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
        sceneRoot.getTransforms().add(affine);
        // views.setMaterial(skyMaterial);
        // views.setCullFace(CullFace.NONE);
        layoutViews();
        sceneRoot.getChildren().addAll(views);

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

    /*
          Properties
      */
//    private final DoubleProperty size = new SimpleDoubleProperty(){
//        @Override
//        protected void invalidated()
//        {
//            layoutViews();
//        }
//
//    };

    private void constructWorld() {
        AmbientLight light = new AmbientLight(Color.rgb(200, 200, 200));
        PointLight pl = new PointLight();
        pl.setTranslateX(100);
        pl.setTranslateY(-100);
        pl.setTranslateZ(-100);
        sceneRoot.getChildren().add(pl);
        sceneRoot.getChildren().add(light);

    }

    private void constructUI(){

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
        int[] panelOneCoordinates = {300, -74, 190, rAY, rAX, rAZ};
        int[] panelTwoCoordinates = {395, -74, 400, rAY, rAX, rAZ};

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
        Date date = formatter.parse(theDate); //Parse string to create Date object
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

    private void createSun() {

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

    private void startTimer(){
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Transform ct = (camera != null) ? camera.getLocalToSceneTransform() : null;
                if(ct != null){
                    affine.setTx(ct.getTx());
                    affine.setTy(ct.getTy());
                    affine.setTz(ct.getTz());
                }
            }
        };
        timer.start();
    }



    //TODO separate scenes -> sceneRoot = groundobjects,     maybe     sceneSun = sun + movement + light effects
    //kind of started doing this very roughly
    //sceneRoot = objects and environment
    //      scene = transformed sceneRoot with skybox and camera in environment


    @Override
    public void start(Stage stage) throws ParseException, IOException {
        //Loads in UI

        //initiates the scene, environment and camera


        //TODO a few things to consider about tying FXML and java together:
        /*
        1. confirm we are loading in the FXML correctly
        2. construct a frame to house everything being displayed:
            I think this is an anchorPane, but confirm we shouldn't be using window
                or even a regular pane for the display of the skybox

        3. setup the entire frame with the UI, get that displaying
        4. determine how to get the scene or stage to output to the entireframe.
        5. I BELIEVE that stage is for the whole thing, so we will need a separate stage for
         scene    - entire frame
            stage: ui       - just ui
            stage: skybox   - just skybox

      NOTE ABOUT ABOVE: could also set them each in their own panes? not sure....



         */


        // Scene displayUI = entireFrame.getScene();


        //TODO BELOW: we will likely use the fxmlLoader to call the view once we have more pieces impelmented within our view.
        // right now we are just setting up skybox

        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxExample.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //TODO add FXML call and window with which to build skybox in.
        //TODO


        //  width = skyboxImage.getWidth();
        // height = skyboxImage.getHeight();

        constructWorld();


        //final PhongMaterial skyMaterial = new PhongMaterial();

        // skyMaterial.setDiffuseMap(skyboxImage);
        // skyMaterial.setSpecularColor(Color.TRANSPARENT);   //TODO this MIGHT be how you can make the box transparent and allow light through, might need to do mesh.
        //TriangleMesh cube = new TriangleMesh();
        // skyMaterial.setDiffuseColor(Color.TRANSPARENT); //uncomment this line if you want to have the background one dissapear for now. can delete once skybox is fixed.
        //MeshView meshView = new MeshView();
        //cube = new TriangleMesh(skyboxImage)
        //TODO may change from box to a mesh cube since we can easily make that transparent
        // Box skybox = new Box(99999, 99999, 99999);
        //skybox.setMaterial(skyMaterial);

        //skybox.setCullFace(CullFace.NONE);
        // sceneRoot.getChildren().add(skybox);

        //   sceneRoot.getTransforms().add(affine); //TODO I think can delete once skybox works... just want to ensure it is covered in other method.

        ///sceneRoot.getChildren().addAll(views);  //comment this line out, handled in a different method


        createSun();
        sunriseSunset();
        //TODO make a group and call for animations of the sun.
        //  Group sunOrbit = new Group(second, path, sun);
        // createPathTransition(second, path, node);

        //sceneRoot.getTransforms().add(sunOrbit);

        //TODO methodize out scene and camera controls
        //-------------Scene and Camera set up----------------------------//
        //values for size of window
        double sceneWidth = 400;
        //TODO change to 768;
        double sceneHeight = 400;

        //whole program's size, UI will adjust to this
        double uiFrameWidth = 1600;
        double uiFrameHeight = 900;

        //we display our skybox in a Pane that's within the AnchorPane of the UI


        //Scene uiFrame = new Scene(uiSceneRoot, uiFrameWidth, uiFrameHeight, true);
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);


        // entireFrame.getChildren().addAll(skyboxPane, skyboxUIPane);
        // skyboxPane.getChildren().addAll((Collection<? extends Node>) uiFrame);


        //   sceneRoot.getChildren().addAll(views);
        scene.setFill(Color.DARKCYAN);    //Change this to change the background color. TODO leaving this in to explore setting the scene's background entirely as the skybox
        //
        Group panelsWHouse = addSolarPanel(sceneRoot);

        cameraAndControls(sceneRoot, panelsWHouse, scene);
        loadImageViews();
        //TODO I think this will be set in FXML, but maybe not
        sceneRoot.setVisible(true);
        //stage is the whole shebang
        scene.setRoot(sceneRoot);


        //initializeProgram();
        //  entireScene =  initializeProgram();
        stage.setTitle("Skybox");
        //stage.setWidth(600);
        // stage.setHeight(400);


        //TODO notes; only get one stage, but multiple scenes, can update and apply changews to scenes relatively easily.
        //Right now, working on getting windows displaying.

        // stage.setScene(entireFrame); ***TODO need to extract entireFrame into a scene to display both. may want to do this within the controllers.
        //   stage.setScene(SkyboxController.uiSceneRoot);
        //  skyboxPane.setScaleShape(true); //I think that this will ensure that when we resize the skybox pane it shoulds tay consistant in size


        stage.setScene(scene);
        // stage.setScene(displayUI);
        //Window window = uiFrame.getWindow();
        //window.isShowing();
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