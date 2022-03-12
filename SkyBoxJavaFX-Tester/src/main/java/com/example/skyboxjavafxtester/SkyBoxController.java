package com.example.skyboxjavafxtester;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class SkyBoxController{

    @FXML
    private static Pane skyboxPane;
    @FXML
    private Pane entireFrame;
    @FXML
    private AnchorPane uiPane;
    @FXML
    private AnchorPane sliderAndDate;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Slider slider;
    @FXML
    private Label locationLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private TextField theLocationPicker;
    @FXML
    private Button returnOnInvestment;
    @FXML
    private Button optimalPanels;
    @FXML
    private Button quit;
    @FXML
    private Button removeAll;
    @FXML
    private Button intensityLevels;
    @FXML
    private Button sunriseSunsetTimes;
    @FXML
    private Button controls;
    @FXML
    private Button addAll;

    private static String currentTime;


    @FXML
    protected void initialize() throws ParseException, IOException {
        setSkyboxPane();
        setEntireFrame();

        //Initialize Slider Ticks
        LocalTime start = LocalTime.parse(SkyBoxApplication.sunriseTime);
        LocalTime end =  LocalTime.parse(SkyBoxApplication.sunsetTime);
        Long hours = ChronoUnit.HOURS.between(start, end);//# of mins between
        slider.setMax(hours); //Gives 11 ticks.... 11 hours between sunrise and sunset

        //Initialize currentTime
        changeHour(6);
        currentTimeLabel.setText(currentTime);

    }

    @FXML
    private void setEntireFrame() {
        entireFrame.getChildren().addAll(new Pane(skyboxPane), new AnchorPane(uiPane), new AnchorPane(sliderAndDate));
    }


    @FXML
    protected Pane setSkyboxPane() throws ParseException {
        Group skybox = new Group();

        //SkyBoxApplication.modifySkybox();
        Group sun = SkyBoxApplication.sunCreation(); //creating sun
        skyboxPane = new Pane(sun, skybox);
        SkyBoxApplication.startParams(); // Setting start date, location, sunset/sunrise times
        Group panelsWHouse = SkyBoxApplication.models(); //Creating all models for the scene

        skybox.getChildren().addAll(sun, panelsWHouse);
        SkyBoxApplication.constructWorld(skybox); // Construct the empty SkyBox group

        //THIS causes an error: Duplicate children added so i commented it out. Its being out in skyboxPane above
        //skyboxPane.getChildren().add(skybox);


        //Heres where we do the set up camera and background?
        //Could we move it into constructWorld?
        //How to convert the things happening from a Scene to A AnchorPane or how to add a new scene into the Pane?

        /*
        skybox.setFill(new ImagePattern(SkyBoxApplication.skyboxImage));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);

         */


        return skyboxPane;
    }




    //Methods for buttons
    public void getDate(ActionEvent actionEvent) throws ParseException {
        String newDate = datePicker.getValue().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formatter
        SkyBoxApplication.date = formatter.parse(newDate);

        recalculateSunTimes();
    }

    public void getLocation(ActionEvent actionEvent) {
        SkyBoxApplication.theLocation = theLocationPicker.getText();
        String[] coords = SkyBoxApplication.theLocation.split(",");
        SkyBoxApplication.latitude = Double.parseDouble(coords[0]);
        SkyBoxApplication.longitude = Double.parseDouble(coords[1]);
        System.out.println(SkyBoxApplication.latitude);
        System.out.println(SkyBoxApplication.longitude);

        recalculateSunTimes();
    }

    public void showROI(ActionEvent actionEvent) {
    }

    public void removeAllPanels(ActionEvent actionEvent) {

        if(SkyBoxApplication.panelsWHouse.getChildren().contains(SkyBoxApplication.solarPanelThreewR)) {
            SkyBoxApplication.panelsWHouse.getChildren().remove(6);
            SkyBoxApplication.panelsWHouse.getChildren().remove(5);
            SkyBoxApplication.panelsWHouse.getChildren().remove(4);
            SkyBoxApplication.panelsWHouse.getChildren().remove(3);
            SkyBoxApplication.panelsWHouse.getChildren().remove(2);
            SkyBoxApplication.panelsWHouse.getChildren().remove(1);
        }
    }

    public void showIntensityLevels(ActionEvent actionEvent) {
    }

    public void showSunTimes(ActionEvent actionEvent) {

        String sunTimes =
                "On " + SkyBoxApplication.date.toString() + "\n"
                        + "at latitude: " + SkyBoxApplication.latitude + ", and longitude: " + SkyBoxApplication.longitude + ":\n"
                        + "\n"
                        + "Sunrise: " + SkyBoxApplication.sunriseTime + "\n"
                        + "Sunset: " + SkyBoxApplication.sunsetTime;

        JOptionPane.showMessageDialog(null, sunTimes, "Sunrise/Sunset Times", JOptionPane.PLAIN_MESSAGE);
    }

    public void showControls(ActionEvent actionEvent) {
        String controls =
                "Controls:\n"
                        + "\n"
                        + "Rotate Camera: click and drag\n"
                        + "Move Camera Up: w\n"
                        + "Move Camera Left: a\n"
                        + "Move Camera Down: s\n"
                        + "Move Camera Right: d\n"
                        + "Zoom In: ,\n"
                        + "Zoom out: .\n"
                        + "Rotate Model Right: m\n"
                        + "Rotate Model Left: n\n"
                        + "\n"
                        + "Select Solar Panel 1: 1\n"
                        + "Select Solar Panel 2: 2\n"
                        + "Clear Selection: 0\n"
                        + "\n"
                        + "If Panel Is Selected:\n"
                        + "Move Backward: Up Arrow\n"
                        + "Move Forawrd: Down Arrow\n"
                        + "Move Left: Right Arrow\n"
                        + "Move Right: Left Arrow\n"
                        + "Rotate 45 degrees: Spacebar";

        JOptionPane.showMessageDialog(null, controls, "Controls", JOptionPane.PLAIN_MESSAGE);
    }

    public void addAllPanels(ActionEvent actionEvent) {

        if(SkyBoxApplication.panelsWHouse.getChildren().size() < 2) {
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelOnewR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelTwowR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelThreewR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelFourwR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelOneBox);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelTwoBox);
        }
    }

    public void exitProgram(ActionEvent aE) {
        Stage stage = (Stage) uiPane.getScene().getWindow();
        stage.close();
    }

    public void highlightOptimalPanels(ActionEvent actionEvent) {
    }

    public void getTimeZone(ActionEvent actionEvent) {
        SkyBoxApplication.timeZone = actionEvent.getTarget().toString();
        recalculateSunTimes();
    }

    //Helper Methods

    private void recalculateSunTimes() {

        SkyBoxApplication.cal.setTime(SkyBoxApplication.date); //Calender object given corresponding date

        SkyBoxApplication.location = new Location(SkyBoxApplication.latitude, SkyBoxApplication.longitude); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(SkyBoxApplication.location, SkyBoxApplication.timeZone); // Creates calculator for sun times

        SkyBoxApplication.sunriseTime = calculator.getOfficialSunriseForDate(SkyBoxApplication.cal); // Gets sunrise based on date and calculator created
        SkyBoxApplication.sunsetTime = calculator.getOfficialSunsetForDate(SkyBoxApplication.cal); // Gets sunset based on date and calculator created

        LocalTime start = LocalTime.parse(SkyBoxApplication.sunriseTime);
        LocalTime end =  LocalTime.parse(SkyBoxApplication.sunsetTime);
        Long hours = ChronoUnit.HOURS.between(start, end);
        slider.setMax(hours);
    }

    public void sunMovement() {
        float sliderValue = (float) slider.getValue();
        if(sliderValue == .5)
        {
            changeMinute(SkyBoxApplication.sunriseTime, 30);
            //call method to move sun here
        }
        else if(sliderValue >= 0 && sliderValue < 1)
        {
            //No time changes for hour 0
            changeHour(0);

            //Sun Stuff:
            //Starting point for sun
        }
        else if(sliderValue == 1.5)
        {
            //change time
            changeMinute(currentTime,30);

            //change sun
            //sun.translateXY blah blah blah
        }
        else if(sliderValue >= 1 && sliderValue < 2)
        {
            changeHour(1);
        }
        else if(sliderValue == 2.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 2 && sliderValue < 3)
        {
            changeHour(2);
        }
        else if(sliderValue == 3.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 3 && sliderValue < 4)
        {
            changeHour(3);
        }
        else if(sliderValue == 4.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 4 && sliderValue < 5)
        {
            changeHour(4);
        }
        else if(sliderValue == 5.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 5 && sliderValue < 6)
        {
            changeHour(5);
        }
        else if(sliderValue == 6.5)
        {
            changeMinute(currentTime, 30);
        }
        else if(sliderValue >= 6 && sliderValue < 7)
        {
            changeHour(6);
        }
        else if(sliderValue == 7.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 7 && sliderValue < 8)
        {
            changeHour(7);
        }
        else if(sliderValue == 8.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 8 && sliderValue < 9)
        {
            changeHour(8);
        }
        else if(sliderValue == 9.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 9 && sliderValue < 10)
        {
            changeHour(9);
        }
        else if(sliderValue == 10.5)
        {
            changeMinute(currentTime,30);
        }
        else if(sliderValue >= 10 && sliderValue < 11)
        {
            changeHour(10);

        }
        else if(sliderValue == 11)
        {
            changeHour(11);
        }
        System.out.println(sliderValue + " ");

    }

    public void changeHour(int n){
        String startTime = SkyBoxApplication.sunriseTime;
        String[] wholeTime = startTime.split(":");
        int hour = Integer.parseInt(wholeTime[0]);
        int current = hour + n;
        StringBuilder timeMaker = new StringBuilder(current + ":" + wholeTime[1]);
        currentTime = timeMaker.toString();
        currentTimeLabel.setText(currentTime);
        System.out.println(currentTime);
    }

    public void changeMinute(String currentTime, int n){
        String[] wholeTime = currentTime.split(":");
        int mins = Integer.parseInt(wholeTime[1]);
        int current = mins + n;
        StringBuilder timeMaker = new StringBuilder(wholeTime[0] + ":" + current);
        currentTime = timeMaker.toString();
        currentTimeLabel.setText(currentTime);
        System.out.println(currentTime);
    }

}