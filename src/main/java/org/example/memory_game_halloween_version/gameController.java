package org.example.memory_game_halloween_version;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class gameController {
    private HelloApplication mainApp;
    private HomePageController homePageController;

    public int getPoints() {
        return Points;
    }

    private int Points = 0;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Image> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;
    private boolean isAnimating_During_restart = false;

    @FXML
    private ImageView homeButton;
    @FXML
    private Label pointsLabel, POINT;
    @FXML
    private GridPane gameMatrix;

    // Method to set the main application reference
    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        setupHomeLogo();
        setupGameGrid();
        setupCards();
    }

    private void setupHomeLogo() {
        Image homeImage = new Image(getClass().getResource("/img/homeLogo.png").toExternalForm());
        homeButton.setImage(homeImage);
        homeButton.setOnMouseClicked(event -> goToHomePage());
    }

    private void goToHomePage() {
        if (mainApp != null) {
            mainApp.switchToHomePage();
        } else {
            System.err.println("Main application reference is null!");
        }
    }

    private void setupGameGrid() {
        gameMatrix.getChildren().clear();
        buttons.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button button = new Button();
                button.setMinSize(110, 110);
                ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                backImageView.setFitWidth(100);
                backImageView.setFitHeight(100);
                button.setGraphic(backImageView);
                button.setOnAction(e -> CardClick(button));
                gameMatrix.add(button, j, i);
                buttons.add(button);
            }
        }
    }

    private void setupCards() {
        cardValues.clear();
        cardValues.add(new Image(getClass().getResource("/img/darkness.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/darkness.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/double.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/double.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fairy.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fairy.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fighting.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fighting.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fire.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/fire.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/grass.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/grass.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/lightning.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/lightning.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/metal.jpg").toExternalForm()));
        cardValues.add(new Image(getClass().getResource("/img/metal.jpg").toExternalForm()));
        Collections.shuffle(cardValues);
    }

    private void CardClick(Button clickedCard) {
        // Prevent clicking if two cards are already selected
        if (firstCard != null && secondCard != null) {
            return;
        }

        if (!((ImageView) clickedCard.getGraphic()).getImage().getUrl().contains("/img/Back_Group5_card.png")) {
            return;
        }

        // === SET IMAGE AND INDEX ===
        int index = buttons.indexOf(clickedCard);
        ImageView imageView = new ImageView(cardValues.get(index));

        Image frontImage = cardValues.get(index);
        flipCard(clickedCard, frontImage);  //Flip card animation

        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        clickedCard.setGraphic(imageView);

        // Check if this is the first or second card clicked
        if (firstCard == null) {
            firstCard = clickedCard;
        } else if (secondCard == null) {
            secondCard = clickedCard;
            // Check whether they are the same
            if (((ImageView) firstCard.getGraphic()).getImage().getUrl().equals(((ImageView) secondCard.getGraphic()).getImage().getUrl())) {
                Points+=10;
                if (Points == 80){
                    pointsLabel.setText("YOU WIN");
                    POINT.setText("");
                } else if (Points < 80) {
                    pointsLabel.setText(String.valueOf(Points));
                    System.out.println("MATCHED: Point: " + Points);
                }
                firstCard = null;
                secondCard = null;
            } else {
                pauseAndResetCards();
            }
        }
    }

    private void flipCard(Button card, Image frontImage) {
        ImageView backImageView = (ImageView) card.getGraphic();

        // Create the scale-out animation
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(10), backImageView);
        scaleOut.setFromX(1);
        scaleOut.setToX(0);

        // Set front image halfway through the animation
        scaleOut.setOnFinished(actionEvent -> {
            ImageView frontImageView = new ImageView(frontImage);
            frontImageView.setFitWidth(100);
            frontImageView.setFitHeight(100);
            card.setGraphic(frontImageView);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), frontImageView);
            scaleIn.setFromX(0);
            scaleIn.setToX(1);
            scaleIn.play();
        });
        scaleOut.play();
    }

    private void pauseAndResetCards() {
        new Thread(() -> {
            try {
                Thread.sleep(1450);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Flip cards on JavaFX Application Thread
            Platform.runLater(() -> {
                if (firstCard != null && secondCard != null) {
                    ImageView backView1_Pause = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                    ImageView backView2_Pause = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                    backView1_Pause.setFitWidth(100);
                    backView1_Pause.setFitHeight(100);
                    backView2_Pause.setFitWidth(100);
                    backView2_Pause.setFitHeight(100);
                    backView1_Pause.setPreserveRatio(false);
                    backView2_Pause.setPreserveRatio(false);

                    firstCard.setGraphic(backView1_Pause);
                    secondCard.setGraphic(backView2_Pause);
                }
                firstCard = null;
                secondCard = null;
            });
        }).start();
    }

    @FXML
    private void restartButton() {
        // Prevent until Animation done
        if (isAnimating_During_restart) {
            return; // Do nothing
        }

        // Restart Points
        Points = 0;
        pointsLabel.setText(String.valueOf(Points));
        POINT.setText("Points");

        // Reset the game grid and shuffle cards
        setupGameGrid();
        setupCards();

        // Start the animation before restarting the game
        isAnimating_During_restart = true;
        for (Button button : buttons) {
            if (button != null) {
                resetCard_FlipEffect(button);
            }
        }

        // Reset animation state after completion
        new Thread(() -> {
            try {
                Thread.sleep(600); // Wait until animation completes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> isAnimating_During_restart = false);
        }).start();
    }

    private void resetCard_FlipEffect(Button card_reset) {
        ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
        backImageView.setFitWidth(100);
        backImageView.setFitHeight(100);

        // scaleOut: front --> scaleIn: back
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), (ImageView) card_reset.getGraphic());
        scaleOut.setFromX(1);
        scaleOut.setToX(0);

        scaleOut.setOnFinished(actionEvent -> {
            card_reset.setGraphic(backImageView);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), backImageView);
            scaleIn.setFromX(0);
            scaleIn.setToX(1);
            scaleIn.play();
        });
        scaleOut.play();
    }
}
