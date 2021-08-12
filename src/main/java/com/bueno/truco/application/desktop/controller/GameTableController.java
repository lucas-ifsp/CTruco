package com.bueno.truco.application.desktop.controller;

import com.bueno.truco.application.desktop.model.CardImage;
import com.bueno.truco.application.desktop.model.UserPlayer;
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

    private GameIntel intel;
    private UserPlayer player;
    private Player bot;
    private PlayGameUseCase gameUseCase;
    private List<ImageView> opponentCardImages;
    private boolean isPlayerTurn;
    private boolean lastToIncreaseScore;
    private boolean handIsBeginning;
    private boolean gameOver;

    //TODO Implement mão de onze
    //TODO Refactor Player to become an interface

    @FXML
    private void initialize() {
        prepareNewHand();
    }

    public void setPlayers(String playerName, Player bot) {
        player = new UserPlayer(this, playerName);
        this.bot = bot;
        lbPlayerNameValue.setText(player.getId());
        lbOpponentNameValue.setText(bot.getId());
    }

    public void startGame() {
        gameUseCase = new PlayGameUseCase(player, bot);
        gameOver = false;
        while (true) {
            prepareNewHand();
            GameIntel gameIntel = gameUseCase.playNewHand();
            updateView();
            if (gameIntel == null)
                break;
        }
        gameOver = true;
    }

    public void prepareNewHand() {
        opponentCardImages = new ArrayList<>(List.of(cardOpponentLeft, cardOpponentCenter, cardOpponentRight));
        Collections.shuffle(opponentCardImages);

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

        setRoundLabelsInvisible();
        handIsBeginning = true;
    }

    private void setRoundLabelsInvisible() {
        lb1st.setVisible(false);
        lb1stValue.setVisible(false);
        lb2nd.setVisible(false);
        lb2ndValue.setVisible(false);
        lb3rd.setVisible(false);
        lb3rdValue.setVisible(false);
    }

    public void callForPointsRise(ActionEvent actionEvent) {
        if(gameOver)
            closeWindow();
        lastToIncreaseScore = true;
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

    private void handleCardMouseEvents(MouseEvent e, Card card, ImageView cardImageView){
        if(!canPlay(cardImageView)) return;
        if(e.getButton() == MouseButton.SECONDARY) flipCardImage(card, cardImageView);
        if(e.getButton() == MouseButton.PRIMARY) playCard(card, cardImageView);
    }

    private boolean canPlay(ImageView imageView) {
        return isPlayerTurn && !CardImage.isCardMissing(imageView.getImage());
    }

    private void flipCardImage(Card card, ImageView currentCard) {
        if(!CardImage.isCardClosed(currentCard.getImage())) {
            currentCard.setImage(CardImage.ofClosedCard().getImage());
            return;
        }
        currentCard.setImage(CardImage.of(card).getImage());
    }

    private void playCard(Card card, ImageView imageView){
        Card cardToPlay = CardImage.isCardClosed(imageView.getImage())? Card.getClosedCard(): card;
        showUserTurn(imageView);
        player.setCardToPlay(cardToPlay);
        player.setTrucoRequestDecision(false);
        updateView();
        isPlayerTurn = false;
    }

    private void showUserTurn(ImageView cardImage) {
        cardPlayedLast.setImage(cardImage.getImage());
        cardImage.setImage(CardImage.ofNoCard().getImage());
    }

    private void updateView() {
        Platform.runLater(() -> {
            intel = player.getIntel();
            lbHandPointsValue.setText(String.valueOf(intel.getCurrentHandPoints()));
            updateActionButton();
            updateRoundResults();

            if(handIsBeginning) {
                dealCards();
                updatePlayerScores();
            }
        });
    }

    private void updateActionButton() {
        if(gameOver){
            btnAction.setDisable(false);
            btnAction.setText("Fechar");
            return;
        }
        btnAction.setDisable(!canIncreaseScore());
        btnAction.setText(switch (intel.getCurrentHandPoints()) {
            case 1 -> "Pedir Truco!";
            case 3 -> "Pedir Seis!";
            case 6 -> "Pedir Nove!";
            case 9 -> "Pedir Doze!";
            default -> "";
        });
    }

    private boolean canIncreaseScore() {
        return isPlayerTurn && !lastToIncreaseScore;
    }

    private void updateRoundResults() {
        final int roundsPlayed = intel.getRoundsPlayed().size();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel) {
        final Round round = intel.getRoundsPlayed().get(roundNumber - 1);
        final String resultText = round.getWinner().map(winner -> winner.getId()).orElse("Empate");
        roundResultLabel.setText(resultText);
        roundLabel.setVisible(true);
        roundResultLabel.setVisible(true);
    }

    private void dealCards() {
        cardVira.setImage(CardImage.of(intel.getCurrentVira()).getImage());
        cardOwnedLeft.setImage(CardImage.of(player.getReceivedCards().get(0)).getImage());
        cardOwnedCenter.setImage(CardImage.of(player.getReceivedCards().get(1)).getImage());
        cardOwnedRight.setImage(CardImage.of(player.getReceivedCards().get(2)).getImage());
        handIsBeginning = false;
    }

    private void updatePlayerScores() {
        lbOpponentScoreValue.setText(String.valueOf(intel.getOpponentScore(player)));
        lbPlayerScoreValue.setText(String.valueOf(player.getScore()));
    }

    public void startPlayerTurn() {
        isPlayerTurn = true;
        intel = player.getIntel();
        updateView();
    }

    public void showOpponentTurn() {
        isPlayerTurn = false;
        final int lastCardPlayedIndex = intel.getOpenCards().size() - 1;
        final Card lastCardPlayed = intel.getOpenCards().get(lastCardPlayedIndex);
        final ImageView randomCardImage = opponentCardImages.remove(0);

        sleepFor(1500);
        Platform.runLater(() -> {
            randomCardImage.setImage(CardImage.ofNoCard().getImage());
            cardPlayedFirst.setImage(CardImage.of(lastCardPlayed).getImage());
            updateView();
        });
    }

    public void clearPlayedCards() {
        sleepFor(2500);
        Platform.runLater(() -> {
            cardPlayedFirst.setImage(CardImage.ofNoCard().getImage());
            cardPlayedLast.setImage(CardImage.ofNoCard().getImage());
        });
    }

    public void requestTrucoResponse() {
        intel = player.getIntel();
        isPlayerTurn = false;
        final Integer pointsRequested = intel.getCurrentHandPoints() == 1 ? 3 : intel.getCurrentHandPoints() + 3;

        updateView();
        sleepFor(1000);

        final WindowTrucoResponse dialog = new WindowTrucoResponse();
        final Integer decision = dialog.showAndWait(bot.getId(), pointsRequested);

        lastToIncreaseScore = decision == 1;
        player.setTrucoResponseDecision(decision);
    }

    private void sleepFor(int millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
