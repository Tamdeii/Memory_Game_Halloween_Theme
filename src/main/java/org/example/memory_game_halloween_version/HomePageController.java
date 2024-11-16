package org.example.memory_game_halloween_version;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePageController {
    private boolean isSoundEnabled = true;
    private HelloApplication mainApp;

    @FXML
    private ImageView logoImageView;
    @FXML
    private Button startButton;
    @FXML
    private ToggleButton soundToggleButton;

    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        setUplogoImageView();
        startButton.setOnAction(event -> startGame());
        setUpsoundToggleButton();
    }

//    private void setUpsoundToggleButton() {
//        soundToggleButton.setOnAction(actionEvent -> {
//            System.out.println("Updating button text");
//            mainApp.toggleSound();
//            isSoundEnabled = !isSoundEnabled;
//            if (isSoundEnabled){
//                soundToggleButton.setText("Sound: On");
//            }else {
//                soundToggleButton.setText("Sound: Off");
//            }
//        });
//    }
    private void setUpsoundToggleButton() {
        soundToggleButton.setOnAction(actionEvent -> {
            mainApp.toggleSound();
            isSoundEnabled = !isSoundEnabled;
            if (isSoundEnabled){
                soundToggleButton.getStyleClass().remove("button-sound-off");
                soundToggleButton.getStyleClass().add("button-sound");
                soundToggleButton.setText("Sound: On");

            }else {
                soundToggleButton.setText("Sound: Off");
                soundToggleButton.getStyleClass().remove("button-sound");
                soundToggleButton.getStyleClass().add("button-sound-off");
//                soundToggleButton.getStyleClass().add("button-sound");  // Ensure this is added without overriding necessary styles
//                soundToggleButton.setStyle("-fx-background-color: #FF0000;"
//                        + "-fx-text-fill: #ffffff;");
//                soundToggleButton.setStyle("-fx-text-fill: #ffffff;");
            }
        });
    }

    private void setUplogoImageView() {
        Image logoImage = new Image(getClass().getResource("/img/Logo_game.jpg").toExternalForm());
        logoImageView.setImage(logoImage);
    }

    private void startGame() {
        if (mainApp != null) {
            mainApp.switchToGameScene();
        } else {
            System.err.println("Main application reference is null!");
        }
    }
}
