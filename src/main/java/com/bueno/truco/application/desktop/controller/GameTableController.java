package com.bueno.truco.application.desktop.controller;

import com.bueno.truco.application.desktop.model.CardImage;
import com.bueno.truco.application.desktop.model.UserPlayer;
import com.bueno.truco.application.desktop.view.WindowMaoDeOnzeResponse;
import com.bueno.truco.application.desktop.view.WindowTrucoResponse;
import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.GameIntel;
import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.game.PlayGameUseCase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTableController {

    @FXML private Label lbPlayerNameValue;
    @FXML private Label lbPlayerScoreValue;
    @FXML private Label lbOpponentNameValue;
    @FXML private Label lbOpponentScoreValue;
    @FXML private Label lbHandPointsValue;
    @FXML private Label lb1st;
    @FXML private Label lb1stValue;
    @FXML private Label lb2nd;
    @FXML private Label lb2ndValue;
    @FXML private Label lb3rd;
    @FXML private Label lb3rdValue;

    @FXML private ImageView cardDeck;
    @FXML private ImageView cardVira;
    @FXML private ImageView cardOpponentCenter;
    @FXML private ImageView cardOpponentRight;
    @FXML private ImageView cardOpponentLeft;
    @FXML private ImageView cardOwnedCenter;
    @FXML private ImageView cardOwnedRight;
    @FXML private ImageView cardOwnedLeft;
    @FXML private ImageView cardPlayedFirst;
    @FXML private ImageView cardPlayedLast;
    @FXML private Button btnAction;

    private UserPlayer player;
    private Player bot;
    private PlayGameUseCase gameUseCase;
    private List<ImageView> opponentCardImages;
    private boolean playerTurn;
    private boolean lastToIncreaseScore;
    private boolean handIsBeginning;
    private boolean gameOver;

    @FXML
    private void initialize() {
        prepareNewHand();
    }

    public void setPlayers(String playerName, Player bot) {
        player = new UserPlayer(this, playerName);
        this.bot = bot;
        Platform.runLater(() -> {
            lbPlayerNameValue.setText(player.getNickname());
            lbOpponentNameValue.setText(bot.getNickname());
        });
    }

    public void startGame() {
        gameUseCase = new PlayGameUseCase(player, bot);
        gameOver = false;
        while (true) {
            prepareNewHand();
            GameIntel gameIntel = gameUseCase.playNewHand();
            updatePlayerScores();
            if (gameIntel == null)
                break;
        }
        gameOver = true;
        updateActionButton();
    }

    public void prepareNewHand() {
        opponentCardImages = new ArrayList<>(List.of(cardOpponentLeft, cardOpponentCenter, cardOpponentRight));
        Collections.shuffle(opponentCardImages);
        resetCardImages();
        setRoundLabelsInvisible();
        handIsBeginning = true;
        lastToIncreaseScore = false;
    }

    private void resetCardImages() {
        Platform.runLater(() -> {
            cardOwnedLeft.setImage(CardImage.ofClosedCard().getImage());
            cardOwnedCenter.setImage(CardImage.ofClosedCard().getImage());
            cardOwnedRight.setImage(CardImage.ofClosedCard().getImage());
            cardOpponentLeft.setImage(CardImage.ofClosedCard().getImage());
            cardOpponentCenter.setImage(CardImage.ofClosedCard().getImage());
            cardOpponentRight.setImage(CardImage.ofClosedCard().getImage());
            cardVira.setImage(CardImage.ofNoCard().getImage());
            cardDeck.setImage(CardImage.ofClosedCard().getImage());
            cardPlayedLast.setImage(CardImage.ofNoCard().getImage());
            cardPlayedFirst.setImage(CardImage.ofNoCard().getImage());
        });
    }

    private void setRoundLabelsInvisible() {
        Platform.runLater(() -> {
            lb1st.setVisible(false);
            lb1stValue.setVisible(false);
            lb2nd.setVisible(false);
            lb2ndValue.setVisible(false);
            lb3rd.setVisible(false);
            lb3rdValue.setVisible(false);
        });
    }

    public void callForPointsRise(ActionEvent actionEvent) {
        if (gameOver)
            closeWindow();
        lastToIncreaseScore = true;
        updateActionButton();
        player.setTrucoRequestDecision(true);
    }

    private void closeWindow() {
        final Stage window = (Stage) btnAction.getScene().getWindow();
        window.close();
    }

    public void pickLeft(MouseEvent mouseEvent) {
        final Card card = player.getReceivedCards().get(0);
        handleCardMouseEvents(mouseEvent, card, cardOwnedLeft);
    }

    public void pickCenter(MouseEvent mouseEvent) {
        final Card card = player.getReceivedCards().get(1);
        handleCardMouseEvents(mouseEvent, card, cardOwnedCenter);
    }

    public void pickRight(MouseEvent mouseEvent) {
        final Card card = player.getReceivedCards().get(2);
        handleCardMouseEvents(mouseEvent, card, cardOwnedRight);
    }

    private void handleCardMouseEvents(MouseEvent e, Card card, ImageView cardImageView) {
        if (!canPlay(cardImageView)) return;
        if (e.getButton() == MouseButton.SECONDARY) flipCardImage(card, cardImageView);
        if (e.getButton() == MouseButton.PRIMARY) playCard(card, cardImageView);
    }

    private boolean canPlay(ImageView imageView) {
        return playerTurn && !CardImage.isCardMissing(imageView.getImage());
    }

    private void flipCardImage(Card card, ImageView currentCard) {
        if (!CardImage.isCardClosed(currentCard.getImage())) {
            Platform.runLater(() -> currentCard.setImage(CardImage.ofClosedCard().getImage()));
            return;
        }
        Platform.runLater(() -> currentCard.setImage(CardImage.of(card).getImage()));
    }

    private void playCard(Card card, ImageView imageView) {
        Card cardToPlay = CardImage.isCardClosed(imageView.getImage()) ? Card.getClosedCard() : card;
        showUserTurn(imageView);
        player.setCardToPlay(cardToPlay);
        player.setTrucoRequestDecision(false);
        updateView();
        playerTurn = false;
    }

    private void showUserTurn(ImageView cardImage) {
        Platform.runLater(() -> {
            cardPlayedLast.setImage(cardImage.getImage());
            cardImage.setImage(CardImage.ofNoCard().getImage());
        });
    }

    private void updateView() {
        updateHandPointsInfo();
        updateActionButton();
        updateRoundResults();
        if (handIsBeginning)
            dealCards();
    }

    private void updateHandPointsInfo() {
        Platform.runLater(() -> lbHandPointsValue.setText(String.valueOf(player.getIntel().getCurrentHandPoints())));
    }

    private void updateActionButton() {
        if (gameOver) {
            Platform.runLater(() -> {
                btnAction.setDisable(false);
                btnAction.setText("Fechar");
            });
        } else {
            final String newText = switch (player.getIntel().getCurrentHandPoints()) {
                case 1 -> "Pedir Truco!";
                case 3 -> "Pedir Seis!";
                case 6 -> "Pedir Nove!";
                case 9 -> "Pedir Doze!";
                default -> "";
            };
            Platform.runLater(() -> {
                btnAction.setDisable(!canIncreaseScore());
                btnAction.setText(newText);
            });
        }
    }

    private boolean canIncreaseScore() {
        return playerTurn && !lastToIncreaseScore;
    }

    private void updateRoundResults() {
        final int roundsPlayed = player.getIntel().getRoundsPlayed().size();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel) {
        GameIntel intel = player.getIntel();
        final int roundsPlayed = intel.getRoundsPlayed().size();
        final int roundIndex = roundNumber - 1;

        if (roundIndex >= roundsPlayed)
            return;

        final Round round = intel.getRoundsPlayed().get(roundNumber - 1);
        final String resultText = round.getWinner().map(Player::getNickname).orElse("Empate");

        Platform.runLater(() -> {
            roundResultLabel.setText(resultText);
            roundLabel.setVisible(true);
            roundResultLabel.setVisible(true);
        });
    }

    private void dealCards() {
        final CardImage card = CardImage.of(player.getIntel().getCurrentVira());
        Platform.runLater(() -> {
            cardVira.setImage(card.getImage());
            cardOwnedLeft.setImage(CardImage.of(player.getReceivedCards().get(0)).getImage());
            cardOwnedCenter.setImage(CardImage.of(player.getReceivedCards().get(1)).getImage());
            cardOwnedRight.setImage(CardImage.of(player.getReceivedCards().get(2)).getImage());
        });
        handIsBeginning = false;
    }

    private void updatePlayerScores() {
        Platform.runLater(() -> {
            lbOpponentScoreValue.setText(String.valueOf(player.getIntel().getOpponentScore(player)));
            lbPlayerScoreValue.setText(String.valueOf(player.getScore()));
        });
    }

    public void startPlayerTurn() {
        playerTurn = true;
        updateView();
    }

    public void showOpponentTurn() {
        playerTurn = false;
        final List<Card> openCards = player.getIntel().getOpenCards();
        final int lastCardPlayedIndex = openCards.size() - 1;
        final Card lastCardPlayed = openCards.get(lastCardPlayedIndex);
        final ImageView randomCardImage = opponentCardImages.remove(0);

        sleepFor(1500);
        Platform.runLater(() -> {
            randomCardImage.setImage(CardImage.ofNoCard().getImage());
            cardPlayedFirst.setImage(CardImage.of(lastCardPlayed).getImage());
        });
        updateView();
    }

    public void clearPlayedCards() {
        sleepFor(2500);
        Platform.runLater(() -> {
            cardPlayedFirst.setImage(CardImage.ofNoCard().getImage());
            cardPlayedLast.setImage(CardImage.ofNoCard().getImage());
        });
    }

    public void requestTrucoResponse() {
        playerTurn = false;
        final int handPoints = player.getIntel().getCurrentHandPoints();
        final int pointsRequested = handPoints == 1 ? 3 : handPoints + 3;

        updateView();
        sleepFor(1000);

        final WindowTrucoResponse dialog = new WindowTrucoResponse();
        final Integer decision = dialog.showAndWait(bot.getNickname(), pointsRequested);

        lastToIncreaseScore = decision == 1;
        player.setTrucoResponseDecision(decision);
    }

    public void requestMaoDeOnzeResponse() {
        playerTurn = true;

        updateView();
        sleepFor(1000);

        final WindowMaoDeOnzeResponse dialog = new WindowMaoDeOnzeResponse();
        final boolean decision = dialog.showAndWait();

        player.setMaoDeOnzeResponseDecision(decision);
    }

    private void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
