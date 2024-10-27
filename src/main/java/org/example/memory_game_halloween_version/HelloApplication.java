package org.example.memory_game_halloween_version;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.IOException;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    private MediaPlayer mediaPlayer;
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 690);

            //getClass().getResource(): Use to link
            //toExternalForm(): convert the link into string can used
            scene.getStylesheets().add(getClass().getResource("mystyle.css").toExternalForm());
            stage.setResizable(false);

            String musicFile = "/mp3/Halloween_Orchestra_Halloween_Night.mp3";
            Media media = new Media(getClass().getResource(musicFile).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            Image icon = new Image(HelloApplication.class.getResourceAsStream("/img/Back_Group5_card.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Memory Game Halloween Version");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}