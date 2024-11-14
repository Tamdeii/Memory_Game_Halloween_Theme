package org.example.memory_game_halloween_version;

import javafx.stage.Stage;

public class GridDimension {
    public final int SPACE_SIDE = 120;

    public int space_button;

    public int button_width;
    public int button_height;
    public int image_width;
    public int image_height;

    public int stage_width;
    public int button_range;

    private Stage stage;

    // Constructors
    public GridDimension() {
    }

    public GridDimension(int SIZE, int scene_width){
        this.stage = stage;
        stage_width = scene_width;
        button_range = stage_width - (SPACE_SIDE * 2);
        button_height = (button_range - ((SIZE - 1) * space_button)) / SIZE;
        button_width = button_height * 110 / 100;
        image_height = button_height;
        image_width = button_height;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Method to get the window width from the Stage
    public double getWindowWidth() {
        if (stage != null) {
            return stage.getWidth();
        } else {
            System.err.println("Stage reference not set!");
            return 0;
        }
    }
}

