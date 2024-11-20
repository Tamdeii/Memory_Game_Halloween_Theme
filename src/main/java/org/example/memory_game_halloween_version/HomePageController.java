package org.example.memory_game_halloween_version;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePageController {
    private boolean isSoundEnabled = true;
    private Application mainApp;

    @FXML
    private ImageView logoImageView;
    @FXML
    private Button startButton;
    @FXML
    private ToggleButton soundToggleButton;

    public void setMainApp(Application mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        setUplogoImageView();
        startButton.setOnAction(event -> startGame());
        setUpsoundToggleButton();
    }

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
