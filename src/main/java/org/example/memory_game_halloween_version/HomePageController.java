package org.example.memory_game_halloween_version;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private void setUpsoundToggleButton() {
        soundToggleButton.setOnAction(actionEvent -> {
            System.out.println("Updating button text");
            mainApp.toggleSound();
            isSoundEnabled = !isSoundEnabled;
            if (isSoundEnabled){
                soundToggleButton.setText("Sound: On");
            }else {
                soundToggleButton.setText("Sound: Off");
            }
        });
    }

    private void setUplogoImageView() {
        Image logoImage = new Image(getClass().getResource("/img/Logo.png").toExternalForm());
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
