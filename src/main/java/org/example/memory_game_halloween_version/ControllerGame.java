package org.example.memory_game_halloween_version;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.*;

public class ControllerGame {
    private Main mainApp;
    private ControllerHomePage homePageController;
    private static GridDimension dimension;
    private static int b_w;
    private static int b_h;
    private static int i_w;
    private static int i_h;
    private static boolean if_shift = true;
    private static boolean if_default = true;

    private int timer = 20;
    private boolean timeStared = false;
    private boolean isGameOver = false;
    private int count_fliped;
    private int count_full;
    private boolean if_grid_full = false;
    private Timeline countdownTimeline;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Image> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;
    private boolean isAnimating_During_restart = false;

    @FXML
    private ImageView homeButton;
    @FXML
    private Button button_level_1;
    @FXML
    private Button button_level_2;
    @FXML
    private Button button_level_3;
    @FXML
    private ImageView restartButton;
    @FXML
    private Label Timer_Label, Timer;
    @FXML
    private GridPane gameMatrix;

    // Method to set the main application reference
    public void setMainApp(Main mainApp) { this.mainApp = mainApp; }
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

    public void setupGame() {
        setupGameGrid();
        setupCards();
        if (if_default){
            button_level_2.setDisable(true);
            button_level_2.getStyleClass().remove("button-level");
            button_level_2.getStyleClass().add("button-disabled");
        }
        count_full = Main.SIZE * Main.SIZE;
        count_fliped = 0;
        if_grid_full = false;
        if_default = false;
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
        switch (Main.SIZE) {
            case 2:
                timer = 7; // Level 1: 15 seconds
                break;
            case 4:
                timer = 50; // Level 2: 1 minute (60 seconds)
                break;
            case 6:
                timer = 150; // Level 3: 2 minutes (120 seconds)
                break;
            default:
                timer = 50; // Default to 15 seconds if level is undefined
        }
        Timer_Label.setText("");
        Timer.setText("Click 2 cards to start!");
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timer--;
            updateTimerLabel();
            if (count_fliped == count_full){
                Timer_Label.setText("");
                Timer.setText("CONGRATULAIONS! YOU WIN!");
                countdownTimeline.stop();
            }
            else if (timer <= 0) {
                endGame();
                countdownTimeline.stop();
            }
        }));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        if (timeStared){
            countdownTimeline.play();
        }
    }

    private void updateTimerLabel() {
        int minutes = timer / 60;
        int seconds = timer % 60;
        Timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void endGame() {
        // Handle game-over logic, such as displaying a message
        timer = 20;
        timeStared = false;
        isGameOver = true;
        System.out.println("Time's up! Game Over.");
        Timer_Label.setText("Time's up!");
        Timer.setText("Game Over.");
        countdownTimeline.stop();
    }

    private void startTimer() {
        if (!timeStared) {
            timeStared = true;
            countdownTimeline.play();
        }
    }

    @FXML
    private void goToHomePage() {
        if (mainApp != null) {
            mainApp.switchToHomePage();
        } else {
            System.err.println("Main application reference is null!");
        }
    }

    private void setupGameGrid() {
        gameMatrix.getChildren().clear();
        if (Main.SIZE == 2 && if_shift) {
            gameMatrix.setTranslateY(gameMatrix.getLayoutY() + 40);
            if_shift = false;
        }
        else if (Main.SIZE == 4 && if_shift) {
            gameMatrix.setTranslateY(gameMatrix.getLayoutY() + 20);
//            Label_title.setTranslateY(gameMatrix.getTranslateY() + 5);
            if_shift = false;
        }
        else{}

        buttons.clear();

        for (int i = 0; i < Main.SIZE; i++) {
            for (int j = 0; j < Main.SIZE; j++) {
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

        String[] images_name_str = {"/img/pic_1.jpg", "/img/pic_2.jpg", "/img/pic_3.jpg",
                "/img/pic_4.jpg", "/img/pic_5.jpg", "/img/pic_6.jpg",
                "/img/pic_7.jpg", "/img/pic_8.jpg", "/img/pic_9.jpg",
                "/img/pic_10.jpg", "/img/pic_11.jpg", "/img/pic_12.jpg"};

        // pic_1: https://www.freepik.com/free-vector/hand-drawn-skeleton-cartoon-illustration_47684854.htm#fromView=search&page=1&position=9&uuid=273a143e-eb7f-4c4a-9cf9-12bd593730ad
        // pic_2: https://www.freepik.com/free-vector/cute-grim-reaper-dabbing-cartoon-icon-illustration_13149217.htm#fromView=search&page=1&position=2&uuid=a9fb90d5-b076-4c43-bdba-12275dab18e0        // pic_2: https://www.freepik.com/free-ai-image/3d-cartoon-dj-illustration_262764373.htm#fromView=search&page=1&position=0&uuid=2f65d824-4714-45fb-be34-942ca9466737
        // pic_3: https://www.freepik.com/free-vector/hand-drawn-werewolf-illustration_31639439.htm#fromView=image_search_similar&page=1&position=6&uuid=9be89899-49d2-4268-a466-c252e83e790a
        // pic_4: https://www.freepik.com/free-vector/cute-mummy-bring-candy-with-pumpkin-basket-cartoon-vector-icon-illustration-holiday-food-isolated_256104760.htm#fromView=search&page=1&position=2&uuid=9ec03259-0667-4336-b469-6899d036341e
        // pic_5: https://www.freepik.com/free-vector/cute-zombie-rise-from-grave_13716101.htm#fromView=image_search_similar&page=1&position=9&uuid=77c7e8f4-2a83-41de-af8f-46371bd2053e
        // pic_6: https://www.freepik.com/free-vector/colorful-vampire-character-collection-with-flat-design_2926554.htm#fromView=search&page=2&position=46&uuid=0ec17abd-72cb-40f4-a05c-88fe46e3aca5
        // pic_7: https://www.freepik.com/free-vector/scary-set-halloween-ghosts_2993406.htm#fromView=search&page=2&position=6&uuid=16961fde-a1a5-4ac0-b9ed-bd446e4b3804
        // pic_8: https://www.freepik.com/free-vector/hand-drawn-halloween-pumpkin-illustration_17807588.htm#fromView=search&page=1&position=10&uuid=383ad854-fd51-4e9b-9933-9b27e401b963
        // pic_9: https://www.freepik.com/free-vector/cute-astronaut-pirate-captain-with-sword-cartoon-vector-icon-illustration-science-holiday-isolated_57610296.htm#fromView=search&page=1&position=36&uuid=67269361-e559-495a-8c90-f408442f86ea
        // pic_10: https://ac-illust.com/clip-art/25172990?downloader_register=success
        // pic_11: https://yayimages.com/48494164/cute-halloween-black-cat-with-evil-pumpkin-illustration-cartoon-black-cat-and-evil-pumpkin-halloween.html
        // pic_12: https://pixabay.com/vi/vectors/vi%E1%BB%87t-nam-l%C3%A1-c%E1%BB%9D-ti%E1%BA%BFng-vi%E1%BB%87t-qu%E1%BB%91c-gia-26834/

        ArrayList<String> images_name_arrl = new ArrayList<>();
        images_name_arrl.clear();
        for (int count = 0; count < images_name_str.length; count ++)
        {
            images_name_arrl.add(images_name_str[count]);
        }
        Collections.shuffle(images_name_arrl);

        Random random = new Random();
        int index_random = random.nextInt(images_name_str.length);
        for (int count = 0; count < ((Main.SIZE * Main.SIZE) / 2) ; count++)
        {
            Image image = new Image(getClass().getResource(images_name_arrl.get(index_random)).toExternalForm());
            cardValues.add(image);
            cardValues.add(image);
            if (index_random >= (images_name_str.length - 1)){
                index_random = 0;
//                Collections.shuffle(images_name_arrl);
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
                firstCard = null;
                secondCard = null;
                count_fliped += 2;
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
        // Restart Timer
        timer = 20;
        timeStared = false;
        isGameOver = false;
        countdownTimeline.stop();
        setupTimer();

        // Reset the game grid and shuffle cards
        setupGame();

        // Start the animation before restarting the game
        isAnimating_During_restart = true;
        for (Button button : buttons) {
            if (button != null) {
                resetCard_FlipEffect(button);
            }
        }

        // Reset animation state after completion
//        new Thread(() -> {
//            try {
//                Thread.sleep(600); // Wait until animation completes
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Platform.runLater(() -> isAnimating_During_restart = false);
//        }).start();
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
        Main.SIZE = 2;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(Main.SIZE, Main.SCENE_WIDTH);
        setupGame();
        restartGame();
        resetButtonLevel();
        button_level_1.setDisable(true);
        button_level_1.getStyleClass().remove("button-level");
        button_level_1.getStyleClass().add("button-disabled");
    }

    public void restartGamelevel2(ActionEvent actionEvent) {
        Main.SIZE = 4;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(Main.SIZE, Main.SCENE_WIDTH);
        setupGame();
        restartGame();
        resetButtonLevel();
        button_level_2.setDisable(true);
        button_level_2.getStyleClass().remove("button-level");
        button_level_2.getStyleClass().add("button-disabled");
    }

    public void restartGamelevel3(ActionEvent actionEvent) {
        Main.SIZE = 6;
        gameMatrix.setTranslateY(0);
        if_shift = true;
        setDimension(Main.SIZE, Main.SCENE_WIDTH);
        setupGame();
        restartGame();
        resetButtonLevel();
        button_level_3.setDisable(true);
        button_level_3.getStyleClass().remove("button-level");
        button_level_3.getStyleClass().add("button-disabled");
    }

    private void resetButtonLevel(){
        button_level_1.getStyleClass().remove("button-disabled");
        button_level_2.getStyleClass().remove("button-disabled");
        button_level_3.getStyleClass().remove("button-disabled");

        button_level_1.getStyleClass().add("button-level");
        button_level_2.getStyleClass().add("button-level");
        button_level_3.getStyleClass().add("button-level");

        button_level_1.setDisable(false);
        button_level_2.setDisable(false);
        button_level_3.setDisable(false);
    }
}