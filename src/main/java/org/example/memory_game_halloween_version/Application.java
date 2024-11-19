package org.example.memory_game_halloween_version;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;

public class Application extends javafx.application.Application {

    public static int SIZE = 2;
    public static int SCENE_WIDTH = 700;
    public static int SCENE_HEIGHT = 745;

    private MediaPlayer mediaPlayer;
    private Scene sceneRootGame;
    private Scene sceneHomePage;
    private Stage primaryStage; // Store reference to primary stage.
    private boolean isSoundEnabled = true;
    private GameController gameControllerInstance;
    private  HomePageController homePageController;

    @Override
    public void start(Stage stage) {

        try {
            this.primaryStage = stage; // Initialize primary stage reference

            // Load the game scene
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("main_view.fxml"));
            Parent gameRoot = gameLoader.load();
            sceneRootGame = new Scene(gameRoot, SCENE_WIDTH, SCENE_HEIGHT);
            sceneRootGame.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

            // Load the home page scene
            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("home_view.fxml"));
            Parent homeRoot = homeLoader.load();
            sceneHomePage = new Scene(homeRoot, SCENE_WIDTH, SCENE_HEIGHT);
            sceneHomePage.getStylesheets().add(getClass().getResource("homePage.css").toExternalForm());

            // Retrieve controllers and set main application reference
            GameController gameController = gameLoader.getController();
            gameController.setMainApp(this); // Ensure method name matches
            gameController.setDimension(Application.SIZE, Application.SCENE_WIDTH);
            gameController.setupGame();

            HomePageController homeController = homeLoader.getController();
            homeController.setMainApp(this); // Ensure method name matches

            // Set up background music
            URL musicFileUrl = getClass().getResource("/mp3/Halloween_Orchestra_Halloween_Night.mp3");
            if (musicFileUrl != null) {
                Media media = new Media(musicFileUrl.toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            }

            // Set up application icon and title
            Image icon = new Image(getClass().getResourceAsStream("/img/Logo_game.jpg"));
            stage.getIcons().add(icon);
            stage.setTitle("Memory Game Halloween Version");
            stage.setResizable(true);

            // Start the application with the home page scene
            stage.setScene(sceneHomePage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToGameScene() {
        primaryStage.setScene(sceneRootGame);
    }

    public void switchToHomePage() {
        primaryStage.setScene(sceneHomePage);
    }

    public void toggleSound(){
        isSoundEnabled = !isSoundEnabled;
        if (isSoundEnabled) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
