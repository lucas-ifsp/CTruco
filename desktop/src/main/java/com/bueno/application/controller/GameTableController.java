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
import com.bueno.application.model.UIPlayer;
import com.bueno.application.repository.InMemoryGameRepository;
import com.bueno.application.utils.TimelineBuilder;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.HandleIntelUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.ScoreProposalUseCase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

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
    @FXML private Label lbMessage;

    @FXML private ImageView cardDeck;
    @FXML private ImageView cardVira;
    @FXML private ImageView cardOpponentCenter;
    @FXML private ImageView cardOpponentRight;
    @FXML private ImageView cardOpponentLeft;
    @FXML private ImageView cardOwnedCenter;
    @FXML private ImageView cardOwnedRight;
    @FXML private ImageView cardOwnedLeft;
    @FXML private ImageView lastCard;
    @FXML private ImageView firstCard;
    @FXML private Button btnAccept;
    @FXML private Button btnQuit;
    @FXML private Button btnRaise;

    private Player user;
    private Player bot;
    private List<ImageView> opponentCardImages;

    private final InMemoryGameRepository repo;
    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final ScoreProposalUseCase scoreProposalUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private final UUID userUUID;
    private final UUID botUUID;
    private List<Card> userCards;
    private List<Card> botCards;
    private int lastUserPlayedCardPosition;
    private final List<Intel> missingIntel;
    private Intel lastIntel;

    //TODO INCLUDE EVENT RESPONSIBLE
    //TODO REFACTOR CREATE GAME TO RETURN INTEL, NOT GAME
    //TODO TEST USER MAO DE ONZE
    //TODO SOLVE GAME OVER BUG
    //TODO TEST OVERALL GAME PLAYING
    public GameTableController() {
        repo = new InMemoryGameRepository();
        gameUseCase = new CreateGameUseCase(repo);
        playCardUseCase = new PlayCardUseCase(repo);
        scoreProposalUseCase = new ScoreProposalUseCase(repo);
        handleIntelUseCase = new HandleIntelUseCase(repo);
        botUUID = UUID.randomUUID();
        userUUID = UUID.randomUUID();
        missingIntel = new ArrayList<>();
        botCards = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        organizeNewHand();
    }

    public void organizeNewHand() {
        opponentCardImages = new ArrayList<>(List.of(cardOpponentLeft, cardOpponentCenter, cardOpponentRight));
        Collections.shuffle(opponentCardImages);
        resetCardImages();
        setRoundLabelsInvisible();
        updateHandScore(null);

        if (lastIntel != null) {
            dealCards();
            configureButtons(lastIntel);
        }
    }

    private void resetCardImages() {
        cardOwnedLeft.setImage(CardImage.ofClosedCard().getImage());
        cardOwnedCenter.setImage(CardImage.ofClosedCard().getImage());
        cardOwnedRight.setImage(CardImage.ofClosedCard().getImage());
        cardOpponentLeft.setImage(CardImage.ofClosedCard().getImage());
        cardOpponentCenter.setImage(CardImage.ofClosedCard().getImage());
        cardOpponentRight.setImage(CardImage.ofClosedCard().getImage());
        cardVira.setImage(CardImage.ofNoCard().getImage());
        cardDeck.setImage(CardImage.ofClosedCard().getImage());
        firstCard.setImage(CardImage.ofNoCard().getImage());
        lastCard.setImage(CardImage.ofNoCard().getImage());
    }

    private void setRoundLabelsInvisible() {
        lb1st.setVisible(false);
        lb1stValue.setVisible(false);
        lb2nd.setVisible(false);
        lb2ndValue.setVisible(false);
        lb3rd.setVisible(false);
        lb3rdValue.setVisible(false);
    }

    public void createGame(String username) {
        user = new UIPlayer(username, userUUID);
        bot = new MineiroBot(repo, botUUID);
        Game game = gameUseCase.create(user, bot);
        lastIntel = game.getIntel();
        missingIntel.add(lastIntel);

        showPlayerNames();
        updateIntel();
        dealCards();
    }

    private void showPlayerNames() {
        lbPlayerNameValue.setText(user.getUsername());
        lbOpponentNameValue.setText(bot.getUsername());
    }

    private void updateIntel() {
        final var newIntel = handleIntelUseCase.findIntelSince(userUUID, lastIntel);
        missingIntel.addAll(newIntel);
        if (missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    private void dealCards() {
        final var card = CardImage.of(lastIntel.vira());
        final var ownedCards = handleIntelUseCase.getOwnedCards(userUUID);
        user.setCards(ownedCards);
        userCards = ownedCards;

        cardVira.setImage(card.getImage());
        cardOwnedLeft.setImage(CardImage.of(userCards.get(0)).getImage());
        cardOwnedCenter.setImage(CardImage.of(userCards.get(1)).getImage());
        cardOwnedRight.setImage(CardImage.of(userCards.get(2)).getImage());
    }

    private void updateView() {
        final var builder = new TimelineBuilder();
        hideMessage();

        while (!missingIntel.isEmpty()) {
            final var intel = missingIntel.remove(0);
            System.out.println(intel);

            botCards = new ArrayList<>(handleIntelUseCase.getOwnedCards(botUUID));

            if (shouldDecideMaoDeOnze(intel))
                builder.append(0.5, () -> showMessage("Sua mão de onze"));

            if (hasRaiseProposalFromBot(intel)) {
                final var value = scoreToString(intel.scoreProposal().orElseThrow());
                builder.append(0.5, () -> showMessage(bot.getUsername() + " pediu " + value + " !"));
            }

            if (hasHandScoreChange(intel))
                builder.append(0.5, () -> updateHandScore(intel));

            if (isPlayingEvent(intel)) {
                if (hasOpponentCardToShow(intel)) builder.append(0.5, () -> showOpponentCard(intel));
                builder.append(0.5, () -> updateRoundResults(intel));
                if (hasCardsToClean(intel)) builder.append(2.5, this::clearPlayedCards);
            }

            if (intel.handWinner().isPresent()) {
                lastUserPlayedCardPosition = 1;
                builder.append(1.0, () -> updateRoundResults(intel));
                builder.append(1.5, this::organizeNewHand);
                builder.append(this::updatePlayerScores);
            }
            builder.append(() -> configureButtons(intel));
        }
        Platform.runLater(builder.build()::play);
    }

    private String scoreToString(int points) {
        return switch (points) {
            case 3 -> "truco";
            case 6 -> "seis";
            case 9 -> "nove";
            case 12 -> "doze";
            default -> "--";
        };
    }
    private void showMessage(String message) {
        lbMessage.setVisible(true);
        lbMessage.setText(message);
    }

    private void hideMessage() {
        lbMessage.setVisible(false);
        lbMessage.setText("");
    }

    private void configureButtons(Intel intel){
        Predicate<String> shouldDisable = a ->
                 !intel.possibleActions().contains(a) || !userUUID.equals(intel.currentPlayerUuid().orElse(null));

        btnAccept.setDisable(shouldDisable.test("ACCEPT"));
        btnQuit.setDisable(shouldDisable.test("QUIT"));
        btnRaise.setDisable(shouldDisable.test("RAISE"));

        final var baseScore = intel.scoreProposal().orElse(intel.handScore());
        if(baseScore != 0 && baseScore != 12)
            btnRaise.setText("Pedir " + scoreToString(baseScore == 1? 3 : baseScore + 3) + "!");
    }

    private boolean shouldDecideMaoDeOnze(Intel intel) {
        return intel.isMaoDeOnze() && intel.handScore() == 1
                && userUUID.equals(intel.currentPlayerUuid().orElse(null));
    }

    private boolean hasRaiseProposalFromBot(Intel intel) {
        return intel.scoreProposal().isPresent() && userUUID.equals(intel.currentPlayerUuid().orElse(null));
    }

    private boolean hasHandScoreChange(Intel intel) {
        final var event = intel.event().orElse("");
        return event.equals("RAISE") || event.equals("ACCEPT");
    }

    private boolean hasOpponentCardToShow(Intel intel) {
        return lastUserPlayedCardPosition < intel.openCards().size();
    }

    private boolean isPlayingEvent(Intel intel) {
        return intel.event().orElse("").equals("PLAY") && !intel.possibleActions().contains("QUIT");
    }

    private boolean hasCardsToClean(Intel intel) {
        final var cardsPlayed = intel.openCards().size();
        final var isSecondCardOfRound = cardsPlayed % 2 == 1;
        return cardsPlayed > 1 && isSecondCardOfRound;
    }

    private void clearPlayedCards() {
        lastCard.setImage(CardImage.ofNoCard().getImage());
        firstCard.setImage(CardImage.ofNoCard().getImage());
    }

    private void showOpponentCard(Intel intel) {
        final var openCards = intel.openCards();
        final var card = openCards.get(openCards.size() - 1);
        final var cardImage = CardImage.of(card).getImage();
        final var randomCardImage = removeRandomOpponentCard();
        randomCardImage.setImage(CardImage.ofNoCard().getImage());
        lastCard.setImage(cardImage);
        botCards.remove(card);
    }

    private synchronized ImageView removeRandomOpponentCard() {
        return opponentCardImages.remove(0);
    }

    private void updateHandScore(Intel intel) {
        final var value = intel != null ? String.valueOf(intel.handScore()) : "1";
        lbHandPointsValue.setText(value);
    }

    private void updateRoundResults(Intel intel) {
        final var roundsPlayed = intel.roundsPlayed();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue, intel);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue, intel);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue, intel);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel, Intel intel) {
        final var roundsPlayed = intel.roundsPlayed();
        final var roundIndex = roundNumber - 1;

        if (roundIndex >= roundsPlayed) return;

        final var resultText = intel.roundWinners().get(roundNumber - 1).orElse("Empate");
        roundResultLabel.setText(resultText);
        roundLabel.setVisible(true);
        roundResultLabel.setVisible(true);
    }

    private void updatePlayerScores() {
        lbOpponentScoreValue.setText(String.valueOf(lastIntel.currentOpponentScore()));
        lbPlayerScoreValue.setText(String.valueOf(lastIntel.currentPlayerScore()));
    }

    public void accept(ActionEvent a) {
        handleScoreChange("ACCEPT", () -> scoreProposalUseCase.accept(userUUID));
    }

    public void quit(ActionEvent a) {
        handleScoreChange("QUIT", () -> scoreProposalUseCase.quit(userUUID));
    }

    public void raise(ActionEvent a) {
        handleScoreChange("RAISE", () -> scoreProposalUseCase.raise(userUUID));
    }

    private void handleScoreChange(String action, Runnable request){
        if(canPerform(action)) request.run();
        updateIntel();
        updateView();
    }

    private boolean canPerform(String action) {
        final var possibleUuid = lastIntel.currentPlayerUuid();
        if (possibleUuid.isEmpty()) return false;
        final var isCurrentPlayer = possibleUuid.get().equals(userUUID);
        final var isPerformingAllowedAction = lastIntel.possibleActions().contains(action);
        return isCurrentPlayer && isPerformingAllowedAction;
    }

    public void pickLeft(MouseEvent mouseEvent) {
        handleCardPlaying(mouseEvent, cardOwnedLeft, 0);
    }

    public void pickCenter(MouseEvent mouseEvent) {
        handleCardPlaying(mouseEvent, cardOwnedCenter, 1);
    }

    public void pickRight(MouseEvent mouseEvent) {
        handleCardPlaying(mouseEvent, cardOwnedRight, 2);
    }

    private void handleCardPlaying(MouseEvent event, ImageView cardImageView, int cardIndex){
        if (canPerform("PLAY") && !CardImage.isMissing(cardImageView.getImage())) {
            final var card = userCards.get(cardIndex);
            if (event.getButton() == MouseButton.PRIMARY) playCard(card, cardImageView);
            else flipCardImage(card, cardImageView);
        }
    }

    private void playCard(Card card, ImageView imageView) {
        if (isClosed(imageView)) playCardUseCase.discard(new PlayCardUseCase.RequestModel(userUUID, card));
        else playCardUseCase.playCard(new PlayCardUseCase.RequestModel(userUUID, card));

        firstCard.setImage(imageView.getImage());
        imageView.setImage(CardImage.ofNoCard().getImage());

        lastUserPlayedCardPosition = lastIntel.openCards().size() + 1;
        updateIntel();
        updateView();
    }

    private void flipCardImage(Card card, ImageView currentCard) {
        if (!isClosed(currentCard)) currentCard.setImage(CardImage.ofClosedCard().getImage());
        else currentCard.setImage(CardImage.of(card).getImage());
    }

    private boolean isClosed(ImageView imageView) {
        return CardImage.isCardClosed(imageView.getImage());
    }
}