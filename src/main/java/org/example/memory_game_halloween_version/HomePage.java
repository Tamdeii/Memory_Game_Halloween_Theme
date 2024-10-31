package org.example.memory_game_halloween_version;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage {
    private  boolean isSoundEnabled = true;
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        primaryStage.setTitle("Halloween   Memory Game");

        ToggleButton soundToggleButton = (ToggleButton) root.lookup("#soundToggleButton");

        soundToggleButton.setSelected(isSoundEnabled);  // Set initial toggle state

        soundToggleButton.setOnAction(event -> {
            isSoundEnabled = !isSoundEnabled;
            soundToggleButton.setText(isSoundEnabled ? "Sound On" : "Sound Off");

            // Implement sound mute/unmute logic here (using your audio system)
            if (isSoundEnabled) {
                // Unmute sounds (replace with your specific sound handling)
                System.out.println("Sounds unmuted"); // Placeholder for actual implementation
            } else {
                // Mute sounds (replace with your specific sound handling)
                System.out.println("Sounds muted"); // Placeholder for actual implementation
            }
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
