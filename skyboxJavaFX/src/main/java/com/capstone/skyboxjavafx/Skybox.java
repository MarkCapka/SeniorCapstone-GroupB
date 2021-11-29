package com.capstone.skyboxjavafx;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

//example heavily inspired by https://www.programcreek.com/java-api-examples/?code=Birdasaur%2FFXyzLib%2FFXyzLib-master%2Fsrc%2Forg%2Ffxyz%2Fextras%2FSkybox.java


public class Skybox extends Group{
    public enum SkyboxImageType{
        SINGLE, MULTIPLE //number of images that are used to build the skybox (?)
    }
    private final PerspectiveCamera camera; //inside skybox to give perception of being in world
    private AnimationTimer timer;
    private final SkyboxImageType imageType;

    private final Affine affine = new Affine(); //transformation that preserves the state of the lines (parallel, straightness)

    //skybox constructor for single images
    public Skybox(Image singleImage, double size, PerspectiveCamera camera) {
        super();

        //single image face extraction
        this.imageType = SkyboxImageType.SINGLE;
        this.singleImage = singleImage;
        this.size.set(size);
        this.camera = camera;

        getTransforms().add(affine); //initializes affine transformations

        loadImageViews();

        getChildren().addAll(views); //
    } //end skybox constructor.

    //skybox import for multiple image import
    public Skybox(Image topImage, Image bottomImage, Image leftImage, Image rightImage, Image frontImage, Image backImage, double size, PerspectiveCamera camera) {
        super();
        this.imageType = SkyboxImageType.MULTIPLE;

        this.topImage = topImage;
        this.bottomImage = bottomImage;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.size.set(size);
        this.camera = camera;

        loadImageViews();

        getTransforms().add(affine);

        getChildren().addAll(views);


    } //end skybox multiple image constructor
    
    //loader for image views
    private void loadImageViews(){
        for(ImageView iv : views){
            iv.setSmooth(true);
            iv.setPreserveRatio(true);
        }
        validateImageType();
    }

    //image validator:
        //single image: creates viewports and sets all views(image) to singleImage
    private void validateImageType() {
        switch (imageType) {
            case SINGLE -> loadSingleImageViewPorts();
            case MULTIPLE -> setMultipleImages();
            default -> throw new IllegalStateException("validateImageType(): Unexpected value: " + imageType);
        } //end switch
    } //end validateImageType()


    private void setMultipleImages() {

            layoutViews();

            back.setImage(frontImage);
            front.setImage(backImage);
            top.setImage(topImage);
            bottom.setImage(bottomImage);
            left.setImage(leftImage);
            right.setImage(rightImage);
        }




    //see below model for breakdown of skybox image import
    /**
     * below layout of the viewports to this style pattern
     *              _______
     *             |top    |
     *         ____|_______|_____ ____
     *        |left|forward|right|back|
     *        |____|_______|_____|____|
     *             |bottom |
     *             |_______|
     *
     *
     */

    private void loadSingleImageViewPorts() {
        layoutViews();

        double width = singleImage.getWidth(),
                height = singleImage.getHeight();

        //confirming cells are square from image: 4:3
            //TODO confirm that this type of exception is acceptable
        if(width/4 != height/3)
            throw new IllegalStateException("Image does not comply with size constraints: \n Image should be compatible with width/4 and height/3");


        double cellSize = singleImage.getWidth() - singleImage.getHeight();
        
        recalculateSize(cellSize);

        double
                topX = cellSize, topY = 0,

                bottomX = cellSize, bottomY = cellSize * 2,

                leftX = 0, leftY = cellSize,

                rightX = cellSize * 2, rightY = cellSize,

                forwardX = cellSize, forwardY = cellSize,

                backX = cellSize * 3, backY = cellSize;

        //add top padding x+, y+, width-, height
        top.setViewport(new Rectangle2D(topX , topY , cellSize, cellSize ));

        //add left padding x, y+, width, height-
        left.setViewport(new Rectangle2D(leftX , leftY , cellSize - 1, cellSize - 1));

        //add front padding x+, y+, width-, height
        back.setViewport(new Rectangle2D(forwardX , forwardY, cellSize , cellSize));

        //add right padding x, y+, width, height-
        right.setViewport(new Rectangle2D(rightX, rightY , cellSize , cellSize ));

        //add back padding x, y+, width, height-
        front.setViewport(new Rectangle2D(backX + 1, backY - 1, cellSize - 1, cellSize - 1));

        //add bottom padding x+, y, width-, height-
        bottom.setViewport(new Rectangle2D(bottomX, bottomY, cellSize , cellSize));

        for(ImageView view : views){
            view.setImage(singleImage);
            //System.out.println(v.getId() + v.getViewport() + cellSize);
        }
    } //end loadSingleImageViewPorts()

    //TODO understand what this is doing with recalculating size.
        //should be recalculating the size of the photo to ensure it fits within the 4:3 size
    private void recalculateSize(double cell) {
        double factor = Math.floor(getSize() / cell);
        setSize(cell * factor);
    }



    //representing each of the faces of the cube to paste an image to
    private final ImageView
        top = new ImageView(),
        bottom = new ImageView(),
        left = new ImageView(),
        right = new ImageView(),
        back = new ImageView(),
        front = new ImageView();

    {
        top.setId("top ");
        bottom.setId("bottom ");
        left.setId("left ");
        right.setId("right ");
        back.setId("back ");
        front.setId("front ");

    }
        //setting up array of ImageView faces for each of the faces
    private final ImageView[] views = new ImageView[]{
            top,
            left,
            back,
            right,
            front,
            bottom
    };
        //declaring our images that we will import to associate with each face.
        //also adds in singleImage if all 6 are combined into one image, instead of having 6 individual faces.
    private Image topImage, bottomImage, leftImage, rightImage, frontImage, backImage, singleImage;

    private WritableImage convertedImage;



    //setting views with translations
        //TODO play with these value to see how it effects our cube
    private void layoutViews() {
        for(ImageView view : views){
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


    //size declaration for image import
    private final DoubleProperty size = new SimpleDoubleProperty(){
        @Override
        protected void invalidated(){
            switch(imageType){
                case SINGLE:
                    layoutViews();
                    break;

                case MULTIPLE:
                    break;
            }
        }

    }; //end size

    //timer for movement of camera and correlating tranlations
    private void startTimer(){
        timer = new AnimationTimer(){
            @Override
            public void handle(long now){
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





    //getters and setters:
    public final double getSize(){
        return size.get();
    } //end getSize()

    public final void setSize(double value){
        size.set(value);
    } //end setSize(value)

    public DoubleProperty sizeProperty(){
        return size;
    }






}
