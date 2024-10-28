package org.example.memory_game_halloween_version;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
//import.java.scene.*

public class HelloController {

    @FXML
    private Label pointsLabel, POINT;
    @FXML
    private GridPane gameMatrix;

    private int Points = 0;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Image> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;
    private boolean isAnimating_During_restart = false;

    @FXML
    public void initialize() {
        setupGameGrid();
        setupCards();
    }

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

        Image frontImage = cardValues.get(index);
        flipCard(clickedCard, frontImage);

        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
//        //Set Image
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

    private void flipCard(Button card, Image frontImage){
        ImageView backImageView = (ImageView) card.getGraphic();

        //Create the scale-out animation
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(10), backImageView);
        scaleOut.setFromX(1);
        scaleOut.setToX(0);

        //Set front image halfway through the animation
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

    private void resetCard_FlipEffection(Button card_reset){
        ImageView backImageView = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
        backImageView.setFitWidth(100);
        backImageView.setFitHeight(100);

        // scaleOut: front --> scaleIn: back
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), card_reset.getGraphic());
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

    @FXML
    private void restartButton() {
        // Nếu có animation đang chạy, không cho phép nhấn nút restart
        if (isAnimating_During_restart) {
            return; // Không làm gì nếu đang hoạt động
        }

        // Đặt lại điểm số
        Points = 0;
        pointsLabel.setText(String.valueOf(Points));

        // Thiết lập lại trạng thái của các thẻ
        for (Button button : buttons) {
            if (button != null) { // Kiểm tra null
                ImageView backImageView_Restart = new ImageView(new Image(getClass().getResource("/img/Back_Group5_card.png").toExternalForm()));
                backImageView_Restart.setFitWidth(100);
                backImageView_Restart.setFitHeight(100);
                button.setGraphic(backImageView_Restart); // Đặt lại hình ảnh thẻ
            }
        }

        // Thiết lập lại các thẻ và trộn lại giá trị
        setupCards(); // Thiết lập lại các giá trị của thẻ

        // Bắt đầu animation cho tất cả các thẻ (nếu cần)
        isAnimating_During_restart = true; // Đánh dấu là đang trong quá trình khởi động lại
        for (Button button : buttons) {
            if (button != null) { // Kiểm tra null
                resetCard_FlipEffection(button);
            }
        }

        // Đặt lại trạng thái animation sau khi hoàn tất
        new Thread(() -> {
            try {
                Thread.sleep(600); // Chờ cho đến khi animation hoàn tất
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> isAnimating_During_restart = false); // Đặt lại trạng thái sau khi animation hoàn tất
        }).start();
    }

}

