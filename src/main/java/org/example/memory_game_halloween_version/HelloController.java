package org.example.memory_game_halloween_version;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Collections;
//import.java.scene.*

public class HelloController {

    @FXML
    private Label pointsLabel, POINT;

    @FXML
    private GridPane gameMatrix;
    @FXML
    private Button restartButton;
    @FXML
    private ImageView image1, image2, image3, image4,
                      image5, image6, image7, image8,
                      image9, image10, image11, image12,
                      image13, image14, image15, image16;

    private int Points = 0;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Image> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;

    @FXML
    public void initialize() {
        setupGameGrid();
        setupCards();
    }

//    private  void ini_card(){
//        cardValues.clear();
//        image1.setImage(new Image(getClass().getResource("/img/darkness.jpg").toExternalForm()));
//        image2.setImage(new Image(getClass().getResource("/img/darkness.jpg").toExternalForm()));
//        image3.setImage(new Image(getClass().getResource("/img/double.jpg").toExternalForm()));
//        image4.setImage(new Image(getClass().getResource("/img/double.jpg").toExternalForm()));
//        image5.setImage(new Image(getClass().getResource("/img/fairy.jpg").toExternalForm()));
//        image6.setImage(new Image(getClass().getResource("/img/fairy.jpg").toExternalForm()));
//        image7.setImage(new Image(getClass().getResource("/img/fighting.jpg").toExternalForm()));
//        image8.setImage(new Image(getClass().getResource("/img/fighting.jpg").toExternalForm()));
//        image9.setImage(new Image(getClass().getResource("/img/fire.jpg").toExternalForm()));
//        image10.setImage(new Image(getClass().getResource("/img/fire.jpg").toExternalForm()));
//        image11.setImage(new Image(getClass().getResource("/img/grass.jpg").toExternalForm()));
//        image12.setImage(new Image(getClass().getResource("/img/grass.jpg").toExternalForm()));
//        image13.setImage(new Image(getClass().getResource("/img/lightning.jpg").toExternalForm()));
//        image14.setImage(new Image(getClass().getResource("/img/lightning.jpg").toExternalForm()));
//        image15.setImage(new Image(getClass().getResource("/img/metal.jpg").toExternalForm()));
//        image16.setImage(new Image(getClass().getResource("/img/metal.jpg").toExternalForm()));
//    }

    private void setupGameGrid() {
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
        if (firstCard != null && secondCard != null){
            return;
        }


        if(!((ImageView) clickedCard.getGraphic()).getImage().getUrl().contains("/img/Back_Group5_card.png")){
            return;
        }

        int index = buttons.indexOf(clickedCard);
        ImageView imageView = new ImageView(cardValues.get(index));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        //Set Image
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

    private void pauseAndResetCards() {
        new Thread(() -> {
            try {
                Thread.sleep(1450);
            } catch (InterruptedException e) {

            }
            // Flip cards on JavaFX Application Thread
            Platform.runLater(() -> {
                if (firstCard != null && secondCard != null){
                    ImageView backView1_Pause = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                    ImageView backView2_Pause = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                    backView1_Pause.setFitWidth(100);
                    backView1_Pause.setFitHeight(100);
                    backView1_Pause.setPreserveRatio(false);

                    backView2_Pause.setFitWidth(100);
                    backView2_Pause.setFitHeight(100);
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
        Points = 0;
        pointsLabel.setText(String.valueOf(Points));

        //Reset Cards
        setupCards();

        for (Button button : buttons) {
            ImageView backImageView_Restart = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
            backImageView_Restart.setFitWidth(100);
            backImageView_Restart.setFitHeight(100);
            button.setGraphic(backImageView_Restart);
        }
        setupCards();
    }
}