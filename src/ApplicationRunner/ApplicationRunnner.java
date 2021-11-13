package ApplicationRunner;

import com.sun.prism.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color.*;
import javafx.scene.layout.Pane.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class ApplicationRunnner extends Application {

    public static Text currentElapsedTime;

    public static void main(String[] args) {
        launch(args);
    }

    //Ensures program and all it's threads are terminated upon closure
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        //create VBox for whole program
        VBox pane = new VBox();

        pane.setStyle("-fx-background-color: #654321");

        //row1 is for daytime clock and elapsed time
        HBox row1 = new HBox(25);
        //row2 is for tmp, humidity & pressure
        HBox row2 = new HBox(38.25);
        //row3 is for light control and medical gases
        HBox row3 = new HBox(38.7);

        //initialise the rows.
        row1 = initialiseFirstRow(row1);
        pane.setMargin(row1, new Insets(15, 15, 15, 15));
//        initaliseSecondRow(row2);
        try {
            row2 = initaliseSecondRow(row2);
        } catch (FileNotFoundException fnfe) { //incase images aren't found
            System.out.print("\nFile Not Found...");
        }
        pane.setMargin(row2, new Insets(15, 15, 15, 15));
//        initialiseThirdRow(row3);
        row3 = initialiseThirdRow(row3);
        pane.setMargin(row3, new Insets(15, 15, 15, 15));

        //add HBox containers to VBox container
        pane.getChildren().addAll(row1, row2, row3);

        Scene scene = new Scene(pane);
        scene.setFill(Paint.valueOf("#654321"));
        primaryStage.setTitle("Surgery Control Panel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.sizeToScene(); //DELETE?????
//        primaryStage.setHeight(1000);
        primaryStage.show();
    }

    public static HBox initialiseFirstRow(HBox row1) {
        //day time clock pane and rectangles and labels
        final Pane dayTimeClock = new Pane();
        final Rectangle rec1 = new Rectangle(0, 0, 367, 160);
        rec1.setArcHeight(7.5);
        rec1.setArcWidth(7.5);
        rec1.setFill(Paint.valueOf("#654321"));
        rec1.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec1Label = new Label("Day Time Clock");
        rec1Label.setFont(Font.font("Verdana"));
        rec1Label.setTranslateX(5);
        rec1Label.setTranslateY(-10);
        rec1Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");
        Text rec1Time = new Text("00" + ":00" + ":00");
        rec1Time.setFont(Font.font("Verdana", 67));
        rec1Time.setFill(javafx.scene.paint.Color.GREEN);
        rec1Time.setLayoutX(22);
        rec1Time.setLayoutY(105);
        LiveTime liveTimeThread = new LiveTime(rec1Time);
        liveTimeThread.start();
        dayTimeClock.getChildren().addAll(rec1, rec1Label, rec1Time);

        //elapsed time box
        final Pane elapsedTime = new Pane();
        final Rectangle rec2 = new Rectangle(0, 0, 482, 160);
        rec2.setFill(Paint.valueOf("#654321"));
        rec2.setArcHeight(7.5);
        rec2.setArcWidth(7.5);
        rec2.setStroke(Paint.valueOf("#FFFFFF"));
        final Label rec2Label = new Label("Elapsed Time");
        rec2Label.setTranslateX(5);
        rec2Label.setTranslateY(-10);
        rec2Label.setFont(Font.font("Verdana"));
        rec2Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");
        Text currentElapsedTime = new Text("00" + ":00" + ":00");
        currentElapsedTime.setFont(Font.font("Verdana", 67));
        currentElapsedTime.setFill(javafx.scene.paint.Color.RED);
        currentElapsedTime.setLayoutX(25);
        currentElapsedTime.setLayoutY(105);

        VBox buttonPane = new VBox(4);
        buttonPane.setLayoutX(370);
        buttonPane.setLayoutY(14);

        //start button
        StackPane startPane = new StackPane();
        Rectangle start = new Rectangle(0, 0, 90, 40);
        start.setFill(Paint.valueOf("BLUE"));
        start.setStroke(Paint.valueOf("#FFFFFF"));
        final Label startLabel = new Label("START");
        start.setArcHeight(7.5);
        start.setArcWidth(7.5);
        startLabel.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 15;"
                + "-fx-font-weight: bold;");
        startPane.getChildren().addAll(start, startLabel);
        ElapsedTime elapsedTimeThread = new ElapsedTime(currentElapsedTime);

        startPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                    if (elapsedTimeThread.countOn == false) {
                        elapsedTimeThread.countOn = true;
                        elapsedTimeThread.resume();
                    } else {
                        elapsedTimeThread.start();
                        elapsedTimeThread.countOn = true;
                    }
                } catch (IllegalThreadStateException itse) {
                    System.out.print("\nElapsed Time is already running!\n");
                }
            }
        });
        //stop button
        StackPane stopPane = new StackPane();
        Rectangle stop = new Rectangle(0, 0, 90, 40);
        stop.setFill(Paint.valueOf("BLUE"));
        stop.setStroke(Paint.valueOf("#FFFFFF"));
        final Label stopLabel = new Label("STOP");
        stop.setArcHeight(7.5);
        stop.setArcWidth(7.5);
        stopLabel.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 15;"
                + "-fx-font-weight: bold;");
        stopPane.getChildren().addAll(stop, stopLabel);
        stopPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (elapsedTimeThread.countOn == true) {
                    elapsedTimeThread.suspend();
                    elapsedTimeThread.countOn = false;
                }
            }
        });

        //reset button
        StackPane resetPane = new StackPane();
        Rectangle reset = new Rectangle(0, 0, 90, 40);
        reset.setFill(Paint.valueOf("BLUE"));
        reset.setStroke(Paint.valueOf("#FFFFFF"));
        final Label resetLabel = new Label("RESET");
        reset.setArcHeight(7.5);
        reset.setArcWidth(7.5);
        resetLabel.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 15;"
                + "-fx-font-weight: bold;");
        resetPane.getChildren().addAll(reset, resetLabel);
        resetPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                elapsedTimeThread.suspend();
                elapsedTimeThread.countOn = false;
                elapsedTimeThread.currentSecond = 0;
                currentElapsedTime.setText("00:00:00");
            }
        });

        //three button panes to the VBox button pane
        buttonPane.getChildren().addAll(startPane, stopPane, resetPane);

        elapsedTime.getChildren().addAll(rec2, rec2Label, currentElapsedTime, buttonPane);

        //two panes to row1
        row1.getChildren().addAll(dayTimeClock, elapsedTime);

        return row1;
    }

    public static HBox initaliseSecondRow(HBox row2) throws FileNotFoundException {
        //Pane to hold control
        final Pane temperatureControl = new Pane();

        //down and up buttons for temperature control
        HBox downAndUpButtonBar = new HBox(18);
        ImageView downImage = new ImageView();
        ImageView upImage = new ImageView();

        downImage.setImage(new Image(new FileInputStream("images/down-icon.png")));
        upImage.setImage(new Image(new FileInputStream("images/up-icon.png")));

        downAndUpButtonBar.getChildren().addAll(upImage, downImage);
        downImage.setFitWidth(43);
        downImage.setFitHeight(43);
        downImage.setLayoutX(37);
        upImage.setFitWidth(43);
        upImage.setFitHeight(43);
        downAndUpButtonBar.setStyle("-fx-padding: -25 0 20 55;");

        //down and up buttons for humdity control
        HBox downAndUpButtonBar2 = new HBox(18);
        ImageView downImage2 = new ImageView();
        ImageView upImage2 = new ImageView();
        downImage2.setImage(new Image(new FileInputStream("images/down-icon.png")));
        upImage2.setImage(new Image(new FileInputStream("images/up-icon.png")));

        downAndUpButtonBar2.getChildren().addAll(upImage2, downImage2);
        downImage2.setFitWidth(43);
        downImage2.setFitHeight(43);
        upImage2.setFitWidth(43);
        upImage2.setFitHeight(43);
        downImage2.setLayoutX(37);
        downAndUpButtonBar2.setStyle("-fx-padding: -25 0 20 70;");

        //down and up buttons for Pressure control
        HBox downAndUpButtonBar3 = new HBox(15);
        ImageView downImage3 = new ImageView();
        ImageView upImage3 = new ImageView();
        downImage3.setImage(new Image(new FileInputStream("images/down-icon.png")));
        upImage3.setImage(new Image(new FileInputStream("images/up-icon.png")));

        downAndUpButtonBar3.getChildren().addAll(upImage3, downImage3);
        downImage3.setFitWidth(43);
        downImage3.setFitHeight(43);
        upImage3.setFitWidth(43);
        upImage3.setFitHeight(43);
        downImage3.setLayoutX(37);
        downAndUpButtonBar3.setStyle("-fx-padding: -25 0 20 65;");

        //*****************************Temperature control content.***************************
        final Rectangle rec1 = new Rectangle(0, 0, 265.7, 160);
        rec1.setArcHeight(7.5);
        rec1.setArcWidth(7.5);
        rec1.setFill(Paint.valueOf("#654321"));
        rec1.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec1Label = new Label("Temperature Control");
        rec1Label.setTranslateX(5);
        rec1Label.setTranslateY(-10);
        rec1Label.setFont(Font.font("Verdana"));
        rec1Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");

        //borderpane for °C control content
        final BorderPane temperatureControlContent = new BorderPane();
        temperatureControlContent.setLayoutX(20);
        temperatureControlContent.setLayoutY(30);

        //border left
        ImageView thermometerImage = new ImageView();
        thermometerImage.setImage(new Image(new FileInputStream("images/thermometer-icon.png")));
        thermometerImage.setFitWidth(45);
        thermometerImage.setFitHeight(60);
        thermometerImage.setStyle("-fx-padding: 20 0 0 0;");
        temperatureControlContent.setLeft(thermometerImage);
        //border center
        final Label temperatureUnits = new Label("20.0");
        temperatureUnits.setStyle("-fx-text-fill: red;"
                + "-fx-font-size: 50;"
                + "-fx-font-family: 'Verdana';"
                + "-fx-padding: -7 0 25 15;");
        temperatureControlContent.setCenter(temperatureUnits);

        Temperature temperatureClick = new Temperature();
        downImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                temperatureClick.clicked(temperatureUnits, false);
            }
        });

        upImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                temperatureClick.clicked(temperatureUnits, true);
            }
        });

        //border right
        final Label temperatureSymbol = new Label("°C");
        temperatureSymbol.setFont(Font.font("Verdana"));
        temperatureSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 30;"
                + "-fx-padding: 10 0 0 20;");
        temperatureControlContent.setRight(temperatureSymbol);

        //border bottom
        temperatureControlContent.setBottom(downAndUpButtonBar);

        //add the background, labels, and content to pane.
        temperatureControl.getChildren().addAll(rec1, rec1Label, temperatureControlContent);

        //**************END OF TEMP CONTROL CONTENT***************************
        //**************HUMDITY CONTROL **************************************
        //Pane to hold control
        final Pane humidityControl = new Pane();

        final Rectangle rec2 = new Rectangle(0, 0, 265.7, 160);
        rec2.setArcHeight(7.5);
        rec2.setArcWidth(7.5);
        rec2.setFill(Paint.valueOf("#654321"));
        rec2.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec2Label = new Label("Humidity Control");
        rec2Label.setTranslateX(5);
        rec2Label.setTranslateY(-10);
        rec2Label.setFont(Font.font("Verdana"));
        rec2Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");

        //borderpane for humidity control content
        final BorderPane humidityControlContent = new BorderPane();
        humidityControlContent.setLayoutX(20);
        humidityControlContent.setLayoutY(30);

        //border left
        ImageView humidityImage = new ImageView();
        humidityImage.setImage(new Image(new FileInputStream("images/humidity-icon.png")));
        humidityImage.setFitWidth(60);
        humidityImage.setFitHeight(60);
        humidityImage.setStyle("-fx-padding: 20 0 0 0;");
        humidityControlContent.setLeft(humidityImage);

        //border center
        final Label humidityUnits = new Label("40");
        humidityUnits.setStyle("-fx-text-fill: red;"
                + "-fx-font-size: 50;"
                + "-fx-font-family: 'Verdana';"
                + "-fx-padding: -7 0 25 35;");
        humidityControlContent.setCenter(humidityUnits);

        Humidity humidityClick = new Humidity();
        downImage2.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                humidityClick.clicked(humidityUnits, false);
            }
        });
        upImage2.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                humidityClick.clicked(humidityUnits, true);
            }
        });

        //border right
        final Label humiditySymbol = new Label("%");
        humiditySymbol.setFont(Font.font("Verdana"));
        humiditySymbol.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 30;"
                + "-fx-padding: 10 0 0 40;");
        humidityControlContent.setRight(humiditySymbol);

        //border bottom
        humidityControlContent.setBottom(downAndUpButtonBar2);

        //add the background, labels, and content to pane.
        humidityControl.getChildren().addAll(rec2, rec2Label, humidityControlContent);
        //************************END OF HUMIDITY CONTENT************************

        //**************************PRESSURE CONTROL ****************************
        //Pane to hold control
        final Pane pressureControl = new Pane();

        final Rectangle rec3 = new Rectangle(0, 0, 265.7, 160);
        rec3.setArcHeight(7.5);
        rec3.setArcWidth(7.5);
        rec3.setFill(Paint.valueOf("#654321"));
        rec3.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec3Label = new Label("Pressure Control");
        rec3Label.setTranslateX(5);
        rec3Label.setTranslateY(-10);
        rec3Label.setFont(Font.font("Verdana"));
        rec3Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");

        //borderpane for pressure control content
        final BorderPane pressureControlContent = new BorderPane();
        pressureControlContent.setLayoutX(20);
        pressureControlContent.setLayoutY(30);

        //border left
        ImageView pressureImage = new ImageView();
        pressureImage.setImage(new Image(new FileInputStream("images/pressure-icon.png")));
        pressureImage.setFitWidth(52);
        pressureImage.setFitHeight(60);
        pressureImage.setStyle("-fx-padding: 20 0 0 0;");
        pressureControlContent.setLeft(pressureImage);

        //border center
        final Label pressureUnits = new Label("70");

        pressureUnits.setStyle("-fx-text-fill: red;"
                + "-fx-font-size: 50;"
                + "-fx-font-family: 'Verdana';"
                + "-fx-padding: -7 0 25 35;");
        pressureControlContent.setCenter(pressureUnits);

        //border right
        final Label pressureSymbol = new Label("kPa");
        pressureSymbol.setFont(Font.font("Verdana"));
        pressureSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 30;"
                + "-fx-padding: 10 0 0 40;");
        pressureControlContent.setRight(pressureSymbol);
        
        Pressure pressureClick = new Pressure();
        downImage3.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                pressureClick.clicked(pressureUnits, false, pressureSymbol);
            }
        });
        upImage3.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                pressureClick.clicked(pressureUnits, true, pressureSymbol);
            }
        });

        

        //border bottom
        pressureControlContent.setBottom(downAndUpButtonBar3);

        //add the background, labels, and content to pane.
        pressureControl.getChildren().addAll(rec3, rec3Label, pressureControlContent);
        //************************END OF PRESSURE CONTENT************************
        //add all to the row for display.
        row2.getChildren().addAll(temperatureControl, humidityControl, pressureControl);
        return row2;
    }

    public static HBox initialiseThirdRow(HBox row3) {
        //**********light control pane and rectangles and light controls*******************
        final Pane lightControlPane = new Pane();
        final Rectangle rec1 = new Rectangle(0, 0, 417.5, 160);
        rec1.setArcHeight(7.5);
        rec1.setArcWidth(7.5);
        rec1.setFill(Paint.valueOf("#654321"));
        rec1.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec1Label = new Label("Light Control");
        rec1Label.setTranslateX(5);
        rec1Label.setTranslateY(-10);
        rec1Label.setFont(Font.font("Verdana"));
        rec1Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");
        lightControlPane.getChildren().addAll(rec1, rec1Label);

        //VBox to hold the 3 light controls
        VBox lightControlVBox = new VBox(3.5);
        lightControlVBox.setLayoutX(10);
        lightControlVBox.setLayoutY(22);

        //HBox to hold light control 1
        HBox lightControl1 = new HBox(2.5);

        //stack pane for minus button
        StackPane lightControl1Minus = new StackPane();
        final Rectangle lightControl1MinusRec = new Rectangle(0, 0, 45, 35);
        lightControl1MinusRec.setArcHeight(7.5);
        lightControl1MinusRec.setArcWidth(7.5);
        lightControl1MinusRec.setFill(Paint.valueOf("#654321"));
        lightControl1MinusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl1MinusSymbol = new Label("-");
        lightControl1MinusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl1Minus.getChildren().addAll(lightControl1MinusRec, lightControl1MinusSymbol);
        
        Rectangle[] lightControl1Setting = new Rectangle[6];
        lightControl1Setting[0] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[0].setTranslateY(9);
        lightControl1Setting[0].setFill(Paint.valueOf("#333300"));

        lightControl1Setting[1] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[1].setTranslateY(9);
        lightControl1Setting[1].setFill(Paint.valueOf("#333300"));

        lightControl1Setting[2] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[2].setTranslateY(9);
        lightControl1Setting[2].setFill(Paint.valueOf("#333300"));

        lightControl1Setting[3] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[3].setTranslateY(9);
        lightControl1Setting[3].setFill(Paint.valueOf("#333300"));

        lightControl1Setting[4] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[4].setTranslateY(9);
        lightControl1Setting[4].setFill(Paint.valueOf("#333300"));

        lightControl1Setting[5] = new Rectangle(0, 0, 47.5, 20);
        lightControl1Setting[5].setTranslateY(9);
        lightControl1Setting[5].setFill(Paint.valueOf("#333300"));

        //stack pane for Plus button
        StackPane lightControl1Plus = new StackPane();
        final Rectangle lightControl1PlusRec = new Rectangle(0, 0, 45, 35);
        lightControl1PlusRec.setArcHeight(7.5);
        lightControl1PlusRec.setArcWidth(7.5);
        lightControl1PlusRec.setFill(Paint.valueOf("#654321"));
        lightControl1PlusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl1PlusSymbol = new Label("+");
        lightControl1PlusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl1Plus.getChildren().addAll(lightControl1PlusRec, lightControl1PlusSymbol);

        //add light controls to HBox
        lightControl1.getChildren().addAll(lightControl1Minus, lightControl1Setting[0], lightControl1Setting[1],
                lightControl1Setting[2], lightControl1Setting[3], lightControl1Setting[4], lightControl1Setting[5], lightControl1Plus);

        LightControl lightControl1Click = new LightControl();
        lightControl1Minus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl1Click.clicked(lightControl1Setting, false, lightControl1Click.counter);
            }
        });
        
        lightControl1Plus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl1Click.clicked(lightControl1Setting, true, lightControl1Click.counter);
            }
        });

        //HBox to hold light control 2
        HBox lightControl2 = new HBox(2.5);

        StackPane lightControl2Minus = new StackPane();
        final Rectangle lightControl2MinusRec = new Rectangle(0, 0, 45, 35);
        lightControl2MinusRec.setArcHeight(7.5);
        lightControl2MinusRec.setArcWidth(7.5);
        lightControl2MinusRec.setFill(Paint.valueOf("#654321"));
        lightControl2MinusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl2MinusSymbol = new Label("-");
        lightControl2MinusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl2Minus.getChildren().addAll(lightControl2MinusRec, lightControl2MinusSymbol);

        Rectangle[] lightControl2Setting = new Rectangle[6];
        lightControl2Setting[0] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[0].setTranslateY(9);
        lightControl2Setting[0].setFill(Paint.valueOf("#333300"));

        lightControl2Setting[1] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[1].setTranslateY(9);
        lightControl2Setting[1].setFill(Paint.valueOf("#333300"));

        lightControl2Setting[2] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[2].setTranslateY(9);
        lightControl2Setting[2].setFill(Paint.valueOf("#333300"));

        lightControl2Setting[3] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[3].setTranslateY(9);
        lightControl2Setting[3].setFill(Paint.valueOf("#333300"));

        lightControl2Setting[4] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[4].setTranslateY(9);
        lightControl2Setting[4].setFill(Paint.valueOf("#333300"));

        lightControl2Setting[5] = new Rectangle(0, 0, 47.5, 20);
        lightControl2Setting[5].setTranslateY(9);
        lightControl2Setting[5].setFill(Paint.valueOf("#333300"));

        //stack pane for Plus button
        StackPane lightControl2Plus = new StackPane();
        final Rectangle lightControl2PlusRec = new Rectangle(0, 0, 45, 35);
        lightControl2PlusRec.setArcHeight(7.5);
        lightControl2PlusRec.setArcWidth(7.5);
        lightControl2PlusRec.setFill(Paint.valueOf("#654321"));
        lightControl2PlusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl2PlusSymbol = new Label("+");
        lightControl2PlusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl2Plus.getChildren().addAll(lightControl2PlusRec, lightControl2PlusSymbol);

        //add light controls to HBox
        lightControl2.getChildren().addAll(lightControl2Minus, lightControl2Setting[0], lightControl2Setting[1],
                lightControl2Setting[2], lightControl2Setting[3], lightControl2Setting[4], lightControl2Setting[5], lightControl2Plus);

        LightControl lightControl2Click = new LightControl();
        lightControl2Minus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl2Click.clicked(lightControl2Setting, false,lightControl2Click.counter);
            }
        });
        
        lightControl2Plus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl2Click.clicked(lightControl2Setting, true, lightControl2Click.counter);
            }
        });

        //HBox to hold light control 3
        HBox lightControl3 = new HBox(2.5);

        StackPane lightControl3Minus = new StackPane();
        final Rectangle lightControl3MinusRec = new Rectangle(0, 0, 45, 35);
        lightControl3MinusRec.setArcHeight(7.5);
        lightControl3MinusRec.setArcWidth(7.5);
        lightControl3MinusRec.setFill(Paint.valueOf("#654321"));
        lightControl3MinusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl3MinusSymbol = new Label("-");
        lightControl3MinusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl3Minus.getChildren().addAll(lightControl3MinusRec, lightControl3MinusSymbol);

        Rectangle[] lightControl3Setting = new Rectangle[6];
        lightControl3Setting[0] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[0].setTranslateY(9);
        lightControl3Setting[0].setFill(Paint.valueOf("#333300"));

        lightControl3Setting[1] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[1].setTranslateY(9);
        lightControl3Setting[1].setFill(Paint.valueOf("#333300"));

        lightControl3Setting[2] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[2].setTranslateY(9);
        lightControl3Setting[2].setFill(Paint.valueOf("#333300"));

        lightControl3Setting[3] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[3].setTranslateY(9);
        lightControl3Setting[3].setFill(Paint.valueOf("#333300"));

        lightControl3Setting[4] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[4].setTranslateY(9);
        lightControl3Setting[4].setFill(Paint.valueOf("#333300"));

        lightControl3Setting[5] = new Rectangle(0, 0, 47.5, 20);
        lightControl3Setting[5].setTranslateY(9);
        lightControl3Setting[5].setFill(Paint.valueOf("#333300"));

        //stack pane for Plus button
        StackPane lightControl3Plus = new StackPane();
        final Rectangle lightControl3PlusRec = new Rectangle(0, 0, 45, 35);
        lightControl3PlusRec.setArcHeight(7.5);
        lightControl3PlusRec.setArcWidth(7.5);
        lightControl3PlusRec.setFill(Paint.valueOf("#654321"));
        lightControl3PlusRec.setStroke(Paint.valueOf("#FFFFFF"));

        final Label lightControl3PlusSymbol = new Label("+");
        lightControl3PlusSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-padding: 0 0 3 0;"
                + "-fx-font-size: 24;");
        lightControl3Plus.getChildren().addAll(lightControl3PlusRec, lightControl3PlusSymbol);

        //add light controls to HBox
        lightControl3.getChildren().addAll(lightControl3Minus, lightControl3Setting[0], lightControl3Setting[1],
                lightControl3Setting[2], lightControl3Setting[3], lightControl3Setting[4], lightControl3Setting[5], lightControl3Plus);

        LightControl lightControl3Click = new LightControl();
        lightControl3Minus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl3Click.clicked(lightControl3Setting, false,lightControl3Click.counter);
            }
        });
        
        lightControl3Plus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                lightControl3Click.clicked(lightControl3Setting, true, lightControl3Click.counter);
            }
        });
        
        lightControlVBox.getChildren().addAll(lightControl1, lightControl2, lightControl3); //lc1, lc2, lc3 lc = Light Control
        lightControlPane.getChildren().add(lightControlVBox);
        //*****************END OF LIGHT CONTROL ******************************

        //********* medical gases pane and rectangles and labels ************
        final Pane medicalGases = new Pane();
        final Rectangle rec2 = new Rectangle(0, 0, 418.5, 160);
        rec2.setArcHeight(7.5);
        rec2.setArcWidth(7.5);
        rec2.setFill(Paint.valueOf("#654321"));
        rec2.setStroke(Paint.valueOf("#FFFFFF"));

        final Label rec2Label = new Label("Medical Gases");
        rec2Label.setTranslateX(5);
        rec2Label.setTranslateY(-10);
        rec1Label.setFont(Font.font("Verdana"));
        rec2Label.setStyle("-fx-background-color: #654321;"
                + "-fx-text-fill: white;"
                + "-fx-padding: 0 4 0 4;"
                + "-fx-font-size: 11;"
                + "-fx-font-weight: bold;");

        //HBox holds the borderpanes list of medical gases
        HBox medicalGasesHBox = new HBox(11);
        medicalGasesHBox.setLayoutX(25);
        medicalGasesHBox.setLayoutY(40);

        BorderPane o2 = new BorderPane();

        //stack pane for medical gas rectangle with label in center
        StackPane o2StackPane = new StackPane();
        final Rectangle o2Rec = new Rectangle(0, 0, 66, 66);
        o2Rec.setFill(Paint.valueOf("#ff4500"));

        final Label o2RecLabel = new Label("Low");
        o2RecLabel.setStyle("-fx-text-fill: black;");
        o2StackPane.getChildren().addAll(o2Rec, o2RecLabel);
        o2.setCenter(o2StackPane);

        final Label o2Label = new Label("O2");
        o2Label.setStyle("-fx-text-fill: yellow;"
                + "-fx-font-size: 14;");
        o2.setTop(o2Label);
        BorderPane.setAlignment(o2Label, Pos.CENTER);
        BorderPane n2o = new BorderPane();

        //stack pane for medical gas rectangle with label in center
        StackPane n2oStackPane = new StackPane();
        final Rectangle n2oRec = new Rectangle(0, 0, 66, 66);
        n2oRec.setFill(Paint.valueOf("#90ee90"));

        final Label n2oRecLabel = new Label("High");
        n2oRecLabel.setStyle("-fx-text-fill: black;");
        n2oStackPane.getChildren().addAll(n2oRec, n2oRecLabel);
        n2o.setCenter(n2oStackPane);

        final Label n2oLabel = new Label("N2O");
        n2oLabel.setStyle("-fx-text-fill: yellow;"
                + "-fx-font-size: 14;");
        n2o.setTop(n2oLabel);
        BorderPane.setAlignment(n2oLabel, Pos.CENTER);

        BorderPane air1 = new BorderPane();

        //stack pane for medical gas rectangle with label in center
        StackPane air1StackPane = new StackPane();
        final Rectangle air1Rec = new Rectangle(0, 0, 66, 66);
        air1Rec.setFill(Paint.valueOf("#90ee90"));

        final Label air1RecLabel = new Label("Norm");
        air1RecLabel.setStyle("-fx-text-fill: black;");
        air1StackPane.getChildren().addAll(air1Rec, air1RecLabel);
        air1.setCenter(air1StackPane);

        final Label air1Label = new Label("AIR1");
        air1Label.setStyle("-fx-text-fill: yellow;"
                + "-fx-font-size: 14;");
        air1.setTop(air1Label);
        BorderPane.setAlignment(air1Label, Pos.CENTER);

        BorderPane co2 = new BorderPane();

        //stack pane for medical gas rectangle with label in center
        StackPane co2StackPane = new StackPane();
        final Rectangle co2Rec = new Rectangle(0, 0, 66, 66);
        co2Rec.setFill(Paint.valueOf("#ff4500"));

        final Label co2RecLabel = new Label("Norm");
        co2RecLabel.setStyle("-fx-text-fill: black;");
        co2StackPane.getChildren().addAll(co2Rec, co2RecLabel);
        co2.setCenter(co2StackPane);

        final Label co2Label = new Label("CO2");
        co2Label.setStyle("-fx-text-fill: yellow;"
                + "-fx-font-size: 14;");
        co2.setTop(co2Label);
        BorderPane.setAlignment(co2Label, Pos.CENTER);

        BorderPane vac = new BorderPane();

        //stack pane for medical gas rectangle with label in center
        StackPane vacStackPane = new StackPane();
        final Rectangle vacRec = new Rectangle(0, 0, 66, 66);
        vacRec.setFill(Paint.valueOf("#ff4500"));

        final Label vacRecLabel = new Label("High");
        vacRecLabel.setStyle("-fx-text-fill: black;");
        vacStackPane.getChildren().addAll(vacRec, vacRecLabel);
        vac.setCenter(vacStackPane);

        final Label vacLabel = new Label("VAC");
        vacLabel.setStyle("-fx-text-fill: yellow;"
                + "-fx-font-size: 14;");
        vac.setTop(vacLabel);
        BorderPane.setAlignment(vacLabel, Pos.CENTER);

        medicalGasesHBox.getChildren().addAll(o2, n2o, air1, co2, vac);

        medicalGases.getChildren().addAll(rec2, rec2Label, medicalGasesHBox);

//        *********************** END OF MEDICAL GASES *************************
        row3.getChildren().addAll(lightControlPane, medicalGases);
        return row3;
    }

}
