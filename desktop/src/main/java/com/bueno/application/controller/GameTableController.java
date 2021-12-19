/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.application.controller;

import com.bueno.application.model.CardImage;
import com.bueno.application.model.UserPlayer;
import com.bueno.application.view.WindowMaoDeOnzeResponse;
import com.bueno.application.view.WindowTrucoResponse;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.PlayGameUseCase;
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
            lbPlayerNameValue.setText(player.getUsername());
            lbOpponentNameValue.setText(bot.getUsername());
        });
    }

    public void startGame() {
        PlayGameUseCase gameUseCase = new PlayGameUseCase(player, bot);
        gameOver = false;
        while (true) {
            prepareNewHand();
            Intel intel = gameUseCase.playNewHand();
            updatePlayerScores();
            if (intel == null)
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
        Card cardToPlay = CardImage.isCardClosed(imageView.getImage()) ? Card.closed() : card;
        showUserTurn(imageView);
        player.setCardToPlayDecision(cardToPlay);
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
        Platform.runLater(() -> lbHandPointsValue.setText(
                String.valueOf(player.getIntel().getHandScore().get())));
    }

    private void updateActionButton() {
        if (gameOver) {
            Platform.runLater(() -> {
                btnAction.setDisable(false);
                btnAction.setText("Fechar");
            });
        } else {
            final String newText = switch (player.getIntel().getHandScore().get()) {
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
        final Intel intel = player.getIntel();
        final boolean forbiddenToRequestIncrement = player.getScore() == 11 || intel.getOpponentScore(player.getUuid()) == 11;
        return playerTurn && !lastToIncreaseScore && !forbiddenToRequestIncrement;
    }

    private void updateRoundResults() {
        final int roundsPlayed = player.getIntel().roundsPlayed();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel) {
        Intel intel = player.getIntel();
        final int roundsPlayed = intel.roundsPlayed();
        final int roundIndex = roundNumber - 1;

        if (roundIndex >= roundsPlayed)
            return;

        final String resultText = intel.roundWinners().get(roundNumber - 1).orElse("Empate");

        Platform.runLater(() -> {
            roundResultLabel.setText(resultText);
            roundLabel.setVisible(true);
            roundResultLabel.setVisible(true);
        });
    }

    private void dealCards() {
        final CardImage card = CardImage.of(player.getIntel().vira());
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
            lbOpponentScoreValue.setText(String.valueOf(player.getIntel().getOpponentScore(player.getUuid())));
            lbPlayerScoreValue.setText(String.valueOf(player.getScore()));
        });
    }

    public void startPlayerTurn() {
        playerTurn = true;
        updateView();
    }

    public void showOpponentTurn() {
        playerTurn = false;
        final List<Card> openCards = player.getIntel().openCards();
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
        final HandScore handPoints = player.getIntel().getHandScore();
        final int pointsRequested = handPoints.increase().get();

        updateView();
        sleepFor(1000);

        final WindowTrucoResponse dialog = new WindowTrucoResponse();
        final Integer decision = dialog.showAndWait(bot.getUsername(), pointsRequested);

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
