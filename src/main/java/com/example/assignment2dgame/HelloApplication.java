package com.example.assignment2dgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;




import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

public class HelloApplication extends Application {

    //creating a boolean binding value to listen to the changes in the boolean
    enum MyEnum {
        ONE, TWO
    }

    SimpleObjectProperty<MyEnum> sop = new SimpleObjectProperty<>(MyEnum.ONE);

    //initial score
    int score_total = 0;
    Label score = new Label(String.valueOf(score_total));

    @Override
    public void start(Stage stage) {

        //creating the sound effects
        String moving_plane = Objects.requireNonNull(getClass().getResource("/Plane.mp3")).toExternalForm();
        String game_over = Objects.requireNonNull(getClass().getResource("/GameO.mp3")).toExternalForm();
        String crush = Objects.requireNonNull(getClass().getResource("/Crash.mp3")).toExternalForm();







        //setting the style of the program
       // String style = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();


        //setting the insets for the margin
        Insets close_btn = new Insets(20, 10, 15, 10); //setting the insets for the margin
        Insets mini_btn = new Insets(20, 10, 15, 0); //setting the insets for the margin
        Insets score_label_geo = new Insets(12, 0, 0, 0); //setting the insets for the margin


        //creating the copter
        Rectangle copter = new Rectangle(120, 80);
        copter.setX(0);
        copter.setY(0);
        String copter_url = Objects.requireNonNull(getClass().getResource("/plaine.gif")).toExternalForm();
        Image copter_img = new Image(copter_url);
        copter.setFill(new ImagePattern(copter_img));


        //creating the variables for the clouds
        String cloud_url = Objects.requireNonNull(getClass().getResource("/cloud.png")).toExternalForm();
        Image cloud = new Image(cloud_url);


        //creating close button
        Button close = new Button();
        close.setId("close_btn");
        HBox.setMargin(close, close_btn);
        close.setOnMouseClicked(mouseEvent -> {
            Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(1500), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
            timeline.getKeyFrames().add(key);
            timeline.setOnFinished((ae) -> Platform.exit()); //the system will exit when the timeline is done playing
            timeline.play();
        });



        //creating the minimize button
        Button minimize = new Button();
        minimize.setId("minimise_btn");
        HBox.setMargin(minimize, mini_btn);
        minimize.setOnMouseClicked(e -> stage.setIconified(true));



        //creating the label for score
        Label score_label = new Label("Score: ");
        HBox.setMargin(score_label, score_label_geo);
        score_label.setId("score_label");
        score.setId("score");



        //creating a label to fill the spacing between
        Label filler  = new Label();
        filler.setMinWidth(910);


        //creating the score tab
        HBox score_tab = new HBox();
        score_tab.getChildren().addAll(close, minimize, filler, score_label, score);
        score_tab.setId("score_tab");
        Effect drop_down_shadow = new DropShadow();
        score_tab.setEffect(drop_down_shadow);


        //setting the ground variables
        String ground_url = Objects.requireNonNull(getClass().getResource("/url.jpg")).toExternalForm();
        Image ground_img = new Image(ground_url);
        ImageView ground = new ImageView(ground_img);
        ground.setX(0);
        ground.setY(800);
        ground.setFitWidth(1100);
        ground.setPreserveRatio(true);


        //creating the image for the coin
        String coin_url = Objects.requireNonNull(getClass().getResource("/Coin.png")).toExternalForm();
        Image coin_img = new Image(coin_url);



        //creating the game environment
        Pane container = new Pane();
        container.getChildren().addAll(copter);
        container.setMouseTransparent(true);

        //adding the clouds to the scene
        for(int i = 0; i < 15; i++){ //creates  less than 15 clouds that are iterated with the indefinite cycle animation
            container.getChildren().addAll(cloud_viewer(cloud, (Math.random()) + 0.2, copter));
            // (Math.random() * (upper_size - lower_size)) + lower_size;
        }

        //adding the coins to the scene
        for (int j = 0; j < 7; j++){
            container.getChildren().addAll(coin(coin_img, (Math.random()) + 0.2, copter));
        }

        //adding the ground to the scene
        container.getChildren().addAll(ground);



        //listens to the changes in the boolean value
        BooleanBinding bb = Bindings.equal(MyEnum.TWO, sop);



        //listens to the changes in the boolean value
        bb.addListener((observable, oldValue, newValue) -> {
            //System.out.println("New value=" + newValue);
            if (newValue){
                Timeline timeline = new Timeline();
                KeyFrame key = new KeyFrame(Duration.millis(1500), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
                timeline.getKeyFrames().add(key);
                if (timeline.getCurrentTime().equals(Duration.millis(750))){
                    //sound effects
                    sound_effects(crush);
                }


                timeline.setOnFinished((ae) -> {
                    System.out.println("game over");
                    Stage stage_lose = new Stage();
                    //setting the scene to the stage
                    stage_lose.setScene(endOfGame());

                    //sound effects
                    sound_effects(game_over);
                    stage_lose.show();
                    stage.hide();}); //the system will exit when the timeline is done playing
                timeline.play();

            }
        });





        //creating the root/ parent
        VBox root = new VBox();
        root.getChildren().addAll(score_tab,container);


        //creating the scene of the application
        Scene scene = new Scene(root, 1100, 600);
        //scene.getStylesheets().addAll(style);


        //creating the event handler of the scene to move the copter around when keys are pressed
        scene.setOnKeyPressed(keyEvent -> {

            switch (keyEvent.getCode()){

                case W -> {
                    if (!(copter.getLayoutY() <= 0)){
                        copter.setLayoutY(copter.getLayoutY() - 30);
                    }
                }

                case S -> {
                    if (!(copter.getLayoutY() == scene.getHeight())){
                        copter.setLayoutY(copter.getLayoutY() + 30);
                    }
                }

                case A -> {
                    if (!(copter.getLayoutX() <= 0)) {
                        copter.setLayoutX(copter.getLayoutX() - 30);
                    }
                }

                case D -> {
                    if (!(copter.getLayoutX() >= scene.getWidth())){
                        copter.setLayoutX((copter.getLayoutX() + 30));
                    }

                }
            }
        });




        //setting the parts of the scene
        stage.setTitle("2D Game Javafx");
        stage.setScene(splash_screen());


        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(6500), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((ae) -> {
            stage.setScene(scene);
            sound_effects(moving_plane);
            stage.centerOnScreen();
        });

        timeline.play();


        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
















    //methods definitions start here



    //creating a function to play sound effects
    private void sound_effects( String sound_url) {

        Media sound_effect = new Media(sound_url);
        MediaPlayer sound_player = new MediaPlayer(sound_effect);
        sound_player.setAutoPlay(true);
    }




    //creating a scene that displays at the end of the game
    private Scene endOfGame() {

        Label score_label_end = new Label("Game over, your score: ");
        score_label_end.setId("end_label");

        Label total_score = new Label(String.valueOf(score_total));
        total_score.setId("endsocre_label");

        Button close_button = new Button("Close");
        close_button.setId("close_end");

        close_button.setOnMouseClicked(e -> Platform.exit());

        VBox end_root = new VBox(score_label_end, total_score, close_button);
        VBox.setMargin(score_label_end, new Insets(10, 50, 10, 50));
        VBox.setMargin(total_score, new Insets(0, 200, 10, 180));
        VBox.setMargin(close_button, new Insets(0, 170, 10, 170));

        Scene end_game = new Scene(end_root);

        String style = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        end_game.getStylesheets().addAll(style);

        return end_game;
    }






    //creating the function that creates the clouds and animates them throughout the whole application
    private Rectangle cloud_viewer(Image cloud, Double duration, Rectangle copter) {


        // let's randomize the size of the cloud
        int upper_size = 70;
        int lower_size = 40;
        int number = (int)(Math.random() * (upper_size - lower_size)) + lower_size;


        //imageview for the transition
        Rectangle cloud_rec = new Rectangle();
        cloud_rec.setFill(new ImagePattern(cloud));
        cloud_rec.setHeight(number);
        cloud_rec.setWidth(number * 1.7);
        cloud_rec.setX(1250);
        cloud_rec.setY(Math.random() * 400);
        cloud_rec.setSmooth(true);


        //setting the transition
        TranslateTransition translateCloud = new TranslateTransition();
        translateCloud.setDuration(Duration.minutes(duration)); //randomizing the duration of the transition
        translateCloud.setNode(cloud_rec);
        translateCloud.setByX(-1400);
        translateCloud.setCycleCount(Animation.INDEFINITE);
        translateCloud.setAutoReverse(false);
        translateCloud.play();


        //listening to the changes in the cloud position
        cloud_rec.translateXProperty().addListener((observableValue, number1, t1) -> {
            if (cloud_rec.getBoundsInParent().intersects(copter.getBoundsInParent())){
                sop.set(MyEnum.TWO);
                //cloud_rec.setWidth(cloud_rec.getWidth() / 2);
            }
        });

        return cloud_rec;
    }







    //creating the function that creates the coins in the game and animates them throughout the whole application
    private Circle coin(Image coin_img, Double duration, Rectangle copter) {

        //imageview for the transition
        Circle coin_rec = new Circle();
        coin_rec.setFill(new ImagePattern(coin_img));
        coin_rec.setRadius(30);
        coin_rec.setCenterX(1250);
        coin_rec.setCenterY((Math.random() *(350 - 30)) + 30);
        coin_rec.setSmooth(true);

        //setting the transition
        TranslateTransition translateCloud = new TranslateTransition();
        translateCloud.setDuration(Duration.minutes(duration)); //randomizing the duration of the transition
        translateCloud.setNode(coin_rec);
        translateCloud.setByX(-1300);
        translateCloud.setCycleCount(Animation.INDEFINITE);
        translateCloud.setAutoReverse(false);
        translateCloud.play();


        //listening to the changes in the movement of the coins and setting them such that when they hit the copter, score is increased.
        coin_rec.translateXProperty().addListener((observableValue, number, t1) -> {
            if (coin_rec.getBoundsInParent().intersects(copter.getBoundsInParent())){
                score_total = score_total + 1;
                score.setText(String.valueOf(score_total));
                coin_rec.setLayoutX(1400);
            }

        });

        return coin_rec;
    }






    //creating the splash screen for the application
    private Scene splash_screen(){
        String splash_url = Objects.requireNonNull(getClass().getResource("/Uncertainty.png")).toExternalForm();

        Image splash_img = new Image(splash_url);

        ImageView splash_image = new ImageView(splash_img);
        splash_image.setPreserveRatio(true);

        VBox splash_root = new VBox();
        splash_root.getChildren().addAll(splash_image);

        return new Scene(splash_root);
    }


    public static void main(String[] args) {
        launch();
    }
}
