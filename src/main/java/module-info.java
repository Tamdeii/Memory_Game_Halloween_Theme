module org.example.memory_game_halloween_version {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens org.example.memory_game_halloween_version to javafx.fxml;
    exports org.example.memory_game_halloween_version;
}