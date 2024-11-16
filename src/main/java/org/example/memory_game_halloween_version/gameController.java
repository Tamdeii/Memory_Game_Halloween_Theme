package org.example.memory_game_halloween_version;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.util.*;

public class gameController {
    private HelloApplication mainApp;
    private HomePageController homePageController;
    private static GridDimension dimension;
    private static int b_w;
    private static int b_h;
    private static int i_w;
    private static int i_h;
    private static boolean if_shift = true;

    private int timer = 10;
    private boolean timeStared = false;
    private boolean isGameOver = false;
    private Timeline countdownTimeline;
    private int matchedPairs = 0;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Image> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;
    private boolean isAnimating_During_restart = false;

    @FXML
    private ImageView homeButton;
    @FXML
    private ImageView restartButton;
    @FXML
    private Label Timer_Label, Timer;
    @FXML
    private GridPane gameMatrix;

    // Method to set the main application reference
    public void setMainApp(HelloApplication mainApp) { this.mainApp = mainApp; }
    //    public void setStage(Stage stage) { this.stage = stage;}
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
        setupTimer();
    }
    private void setupHomeLogo() {
        Image homeImage = new Image(getClass().getResource("/img/Logo_button_home.jpg").toExternalForm());
        homeButton.setImage(homeImage);
        homeButton.setOnMouseClicked(event -> goToHomePage());

        Image restart_image = new Image(getClass().getResource("/img/Logo_button_restart.jpg").toExternalForm());
        restartButton.setImage(restart_image);
        restartButton.setOnMouseClicked(event -> restartGame());
        homeButton.toFront();
        restartButton.toFront();
    }
    private void setupTimer() {
        Timer_Label.setText("");
        Timer.setText("Let's start");
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timer--;
            updateTimerLabel();
            if (timer <= 0) {
                endGame();
                countdownTimeline.stop();
            }
        }));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        if (timeStared == true){
            countdownTimeline.play();
        }
    }

    private void updateTimerLabel() {
        int minutes = timer / 60;
        int seconds = timer % 60;
        Timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void endGame() {
        countdownTimeline.stop();
        if (matchedPairs == (HelloApplication.SIZE * HelloApplication.SIZE) / 2) {
            Timer_Label.setText("Congratulations!");
            Timer.setText("You Win!");
        } else {
            Timer_Label.setText("Time's up!");
            Timer.setText("Game Over.");
        }
        resetTimer();
        timeStared = false;
        isGameOver = true;
    }

    public void resetTimer(){
        if (HelloApplication.SIZE == 2){
            timer = 10;
        } else if (HelloApplication.SIZE == 4) {
            timer = 80;
        } else if (HelloApplication.SIZE == 6) {
            timer = 160;
        } else {
            timer = 40;
        }
    }

    public void setupGame() {
        setupGameGrid();
        setupCards();
    }

    private void startTimer() {
        if (!timeStared) {
            timeStared = true;
            countdownTimeline.play();
        }
    }

    @FXML
    private void goToHomePage() {
        System.out.println("HELLLLL");
        if (mainApp != null) {
            mainApp.switchToHomePage();
        } else {
            System.err.println("Main application reference is null!");
        }
    }

    private void setupGameGrid() {
        gameMatrix.getChildren().clear();
        if (HelloApplication.SIZE == 2 && if_shift) {
            gameMatrix.setTranslateY(gameMatrix.getLayoutY() + 40);
            if_shift = false;
        }
        else if (HelloApplication.SIZE == 4 && if_shift) {
            gameMatrix.setTranslateY(gameMatrix.getLayoutY() + 20);
//            Label_title.setTranslateY(gameMatrix.getTranslateY() + 5);
            if_shift = false;
        }
        else{}

        buttons.clear();

        for (int i = 0; i < HelloApplication.SIZE; i++) {
            for (int j = 0; j < HelloApplication.SIZE; j++) {
                Button button = new Button();
                button.setMinSize(b_w, b_h);
                ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Logo_Back_Group5_card.jpg").toExternalForm()));
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
                "/img/7_ghoul.jpg", "/img/8_pumpkin.jpg", "/img/9_pirate.jpg",
                "/img/10_batman.jpg", "/img/11_catpumpkin.jpg", "/img/12_courage.jpg"};

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
        if (isGameOver) {
            return;
        }
        startTimer();
        // Prevent clicking if two cards are already selected
        if (firstCard != null && secondCard != null) {
            return;
        }

        if (!((ImageView) clickedCard.getGraphic()).getImage().getUrl().contains("/img/Logo_Back_Group5_card.jpg")) {
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
              matchedPairs++;
                if (matchedPairs == (HelloApplication.SIZE * HelloApplication.SIZE) / 2) {
                    endGame();
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
                    ImageView backView1_Pause = new ImageView(new Image(getClass().getResource("/img/Logo_Back_Group5_card.jpg").toExternalForm()));
                    ImageView backView2_Pause = new ImageView(new Image(getClass().getResource("/img/Logo_Back_Group5_card.jpg").toExternalForm()));
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
    private void restartGame() {
        // Prevent until Animation done
        if (isAnimating_During_restart) return; // Do nothing
        //
        matchedPairs = 0;
        resetTimer();
        timeStared = false;
        isGameOver = false;
        countdownTimeline.stop();
        setupTimer();
        setupGame();

        // Start the animation before restarting the game
        isAnimating_During_restart = true;
        for (Button button : buttons) {
            if (button != null) {
                resetCard_FlipEffect(button);
            }
        }

        new Timeline(new KeyFrame(Duration.seconds(0.6), event -> isAnimating_During_restart = false)).play();
    }

    private void resetCard_FlipEffect(Button card_reset) {
        ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Logo_Back_Group5_card.jpg").toExternalForm()));
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

    public void restartGamelevel1(ActionEvent actionEvent) {
        HelloApplication.SIZE = 2;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(HelloApplication.SIZE, HelloApplication.SCENE_WIDTH);
        setupGame();
        restartGame();
    }

    public void restartGamelevel2(ActionEvent actionEvent) {
        HelloApplication.SIZE = 4;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(HelloApplication.SIZE, HelloApplication.SCENE_WIDTH);
        setupGame();
        restartGame();
    }

    public void restartGamelevel3(ActionEvent actionEvent) {
        HelloApplication.SIZE = 6;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(HelloApplication.SIZE, HelloApplication.SCENE_WIDTH);
        setupGame();
        restartGame();
    }
}