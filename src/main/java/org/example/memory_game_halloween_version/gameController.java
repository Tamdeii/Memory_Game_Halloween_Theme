package org.example.memory_game_halloween_version;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class gameController {
    private HelloApplication mainApp;
    private HomePageController homePageController;
    private Stage stage;
    private static GridDimension dimension;
    private static int b_w;
    private static int b_h;
    private static int i_w;
    private static int i_h;

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
    public void setStage(Stage stage) { this.stage = stage;}
    public void setDimension(int size, int scene_width) {
        this.dimension = new GridDimension(size, scene_width);
        b_w = dimension.button_width;
        b_h = dimension.button_height;
        i_w = dimension.image_height;
        i_h = dimension.image_height;
    }

    @FXML
    public void initialize() {
        setupHomeLogo();
//        setupGameGrid();
//        setupCards();
    }

    public void setupGame() {
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
        if (HelloApplication.SIZE == 2)
            gameMatrix.setTranslateY(gameMatrix.getTranslateY() + 40);
        else if (HelloApplication.SIZE == 4)
            gameMatrix.setTranslateY(gameMatrix.getTranslateY() + 20);
        else{}

        buttons.clear();

        for (int i = 0; i < HelloApplication.SIZE; i++) {
            for (int j = 0; j < HelloApplication.SIZE; j++) {
                Button button = new Button();
                button.setMinSize(b_w, b_h);
                ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                backImageView.setFitWidth(i_w);
                backImageView.setFitHeight(i_h);
                button.setGraphic(backImageView);
                button.setOnAction(e -> CardClick(button));
                gameMatrix.add(button, j, i);
                buttons.add(button);
            }
        }
    }

    private Map<String, Image> imageCache = new HashMap<>();

    private Image loadImage(String path) {
        return imageCache.computeIfAbsent(path, p -> new Image(getClass().getResource(p).toExternalForm()));
    }

    private void setupCards() {
        cardValues.clear();

        String[] images_name_str = {"/img/1_skeleton.jpg", "/img/2_grimreaper.jpg", "/img/3_werewolf.jpg",
                "/img/4_mummy.jpg", "/img/5_frankenstein.jpg", "/img/6_dracula.jpg",
                "/img/7_ghoul.jpg", "/img/8_pumpkin.jpg", "/img/9_pirate.png",
                "/img/10_batman.png", "/img/11_catpumpkin.jpg", "/img/12_courage.jpg"};

        ArrayList<String> images_name_arrl = new ArrayList<>();
        images_name_arrl.clear();
        for (int count = 0; count < images_name_str.length; count ++)
        {
            images_name_arrl.add(images_name_str[count]);
        }
        Collections.shuffle(images_name_arrl);

        int index_random = 0;
        for (int count = 0; count < ((HelloApplication.SIZE * HelloApplication.SIZE) / 2) ; count++)
        {
            Image image = new Image(getClass().getResource(images_name_arrl.get(index_random)).toExternalForm());
            cardValues.add(image);
            cardValues.add(image);
            if (index_random >= (images_name_str.length - 1)){
                Random random = new Random();
                index_random = random.nextInt(images_name_str.length);
                Collections.shuffle(images_name_arrl);
            }
            else {
                index_random++;
            }
        }

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

        imageView.setFitWidth(i_w);
        imageView.setFitHeight(i_h);
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
            frontImageView.setFitWidth(i_w);
            frontImageView.setFitHeight(i_h);
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
                    backView1_Pause.setFitWidth(i_w);
                    backView1_Pause.setFitHeight(i_h);
                    backView2_Pause.setFitWidth(i_w);
                    backView2_Pause.setFitHeight(i_h);
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
        backImageView.setFitWidth(i_w);
        backImageView.setFitHeight(i_h);

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
