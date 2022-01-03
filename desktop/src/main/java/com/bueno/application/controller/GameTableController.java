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
import com.bueno.application.view.WindowMaoDeOnzeResponse;
import com.bueno.application.view.WindowTrucoResponse;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleAction;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.BetUseCase;
import com.bueno.domain.usecases.hand.HandleIntelUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.*;

import static com.bueno.domain.entities.game.PossibleAction.*;

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
    @FXML private ImageView lastCard;
    @FXML private ImageView firstCard;
    @FXML private Button btnAction;

    private Player player;
    private Player bot;
    private Game game;
    private List<ImageView> opponentCardImages;

    private final InMemoryGameRepository repo;
    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final BetUseCase betUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private final UUID botUUID;
    private final UUID playerUUID;
    private final List<Intel> missingIntel;
    private Intel lastIntel;
    private List<Card> opponentCards;
    private int lastUserPlayedCardIndex;

    //TODO Resolver bug do bot estar jogando carta repetida (Quando empata ele joga a mesma carta mais alta) Arrumar o mineirobot e também um usecase para pegar carta repetida
    //TODO Resolver o bug de não mostrar última carta jogada
    public GameTableController() {
        repo = new InMemoryGameRepository();
        gameUseCase = new CreateGameUseCase(repo);
        playCardUseCase = new PlayCardUseCase(repo);
        betUseCase = new BetUseCase(repo);
        handleIntelUseCase = new HandleIntelUseCase(repo);
        botUUID = UUID.randomUUID();
        playerUUID = UUID.randomUUID();
        missingIntel = new ArrayList<>();
        opponentCards = new ArrayList<>();
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
        updateHandPointsInfo(null);

        if (lastIntel != null) dealCards();
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
        player = new UIPlayer(username, playerUUID);
        bot = new MineiroBot(repo, botUUID);
        game = gameUseCase.create(player, bot);
        lastIntel = game.getIntel();
        missingIntel.add(lastIntel);

        showPlayerNames();
        updateIntel();
        dealCards();
    }

    private void showPlayerNames() {
        lbPlayerNameValue.setText(player.getUsername());
        lbOpponentNameValue.setText(bot.getUsername());
    }

    private void updateIntel() {
        List<Intel> newIntel = handleIntelUseCase.findIntelSince(playerUUID, lastIntel);
        missingIntel.addAll(newIntel);
        if (missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    private void dealCards() {
        final CardImage card = CardImage.of(lastIntel.vira());
        player.setCards(handleIntelUseCase.getOwnedCards(playerUUID));

        cardVira.setImage(card.getImage());
        cardOwnedLeft.setImage(CardImage.of(player.getCards().get(0)).getImage());
        cardOwnedCenter.setImage(CardImage.of(player.getCards().get(1)).getImage());
        cardOwnedRight.setImage(CardImage.of(player.getCards().get(2)).getImage());

    }

    private void updateView() {
        final TimelineBuilder timelineBuilder = new TimelineBuilder();

        while (!missingIntel.isEmpty()) {
            final Intel intel = missingIntel.remove(0);
            opponentCards = new ArrayList<>(handleIntelUseCase.getOwnedCards(botUUID));
            System.out.println(opponentCards);

            if (intel.scoreProposal().isPresent()) {
                System.out.println("Score proposal " + intel);
                requestTrucoResponse(intel);
            }

            if(hasHandScoreChange(intel)){
                timelineBuilder.append(() -> updateHandPointsInfo(intel));
            }

            if (isPlayingEvent(intel)) {
                System.out.println("Playing event " + intel);

                if (hasOpponentCardToShow(intel)) {
                    System.out.println("Opponent Card: " + intel.openCards().get(intel.openCards().size() - 1));
                    //openCards = new ArrayList<>(intel.openCards());
                    timelineBuilder.append(0.5, () -> showOpponentCard(intel));
                }

                timelineBuilder.append(0.5, () -> updateRoundResults(intel));

                if (hasCardsToClean(intel)) {
                    System.out.println("Need to clean");
                    timelineBuilder.append(2.5, this::clearPlayedCards);
                }
            }

            if (intel.handResult().isPresent()) {
                System.out.println("Hand conclusion ");
                lastUserPlayedCardIndex = 1;
                //openCards.clear();

                timelineBuilder.append(1.0, () -> updateRoundResults(intel));
                timelineBuilder.append(this::organizeNewHand);
                timelineBuilder.append(this::updatePlayerScores);
            }
        }
        Platform.runLater(timelineBuilder.build()::play);
    }

    private boolean hasHandScoreChange(Intel intel) {
        final PossibleAction event = intel.event().orElse(null);
        return event == RAISE || ACCEPT == event;
    }

    private boolean hasOpponentCardToShow(Intel intel) {
        //if (intel.openCards().size() == 1) return false;
        //return openCards.size() < intel.openCards().size();
        return lastUserPlayedCardIndex < intel.openCards().size();
    }

    private boolean isPlayingEvent(Intel intel) {
        return intel.event().orElse(null) == PLAY;
    }

    private boolean hasCardsToClean(Intel intel) {
        final int cardsPlayed = intel.openCards().size();
        return cardsPlayed > 1 && cardsPlayed % 2 == 1;
    }

    private void clearPlayedCards() {
        lastCard.setImage(CardImage.ofNoCard().getImage());
        firstCard.setImage(CardImage.ofNoCard().getImage());
    }

    private void showOpponentCard(Intel intel) {
        final List<Card> openCards = intel.openCards();
        final Card card = openCards.get(openCards.size() - 1);
        final Image cardImage = CardImage.of(card).getImage();
        final ImageView randomCardImage = removeRandomOpponentCard();
        randomCardImage.setImage(CardImage.ofNoCard().getImage());
        lastCard.setImage(cardImage);
        opponentCards.remove(card);
    }

    private synchronized ImageView removeRandomOpponentCard() {
        return opponentCardImages.remove(0);
    }

    private boolean isOpponentCard(Intel intel) {
        final List<Card> cards = intel.openCards();
        final Card card = cards.get(cards.size() - 1);
        return card.equals(Card.closed()) || opponentCards.contains(card);
    }

    private void updateHandPointsInfo(Intel intel) {
        final String value = intel != null ? String.valueOf(intel.handScore().get()) : "1";
        lbHandPointsValue.setText(value);
    }

    private void updateActionButton(Intel intel) {
        if (intel.isGameDone()) {
            btnAction.setDisable(false);
            btnAction.setText("Fechar");
            return;
        }
        if (intel.possibleActions().contains(RAISE)) {
            btnAction.setDisable(false);
            btnAction.setText("Pedir truco");
            return;
        }
        btnAction.setDisable(true);
    }

    private void updateRoundResults(Intel intel) {
        final int roundsPlayed = intel.roundsPlayed();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue, intel);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue, intel);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue, intel);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel, Intel intel) {
        final int roundsPlayed = intel.roundsPlayed();
        final int roundIndex = roundNumber - 1;

        if (roundIndex >= roundsPlayed) return;

        final String resultText = intel.roundWinners().get(roundNumber - 1).orElse("Empate");
        roundResultLabel.setText(resultText);
        roundLabel.setVisible(true);
        roundResultLabel.setVisible(true);
    }

    public void pickLeft(MouseEvent mouseEvent) {
        if (canPerform(PLAY) && !CardImage.isMissing(cardOwnedLeft.getImage())) {
            final Card card = player.getCards().get(0);
            handleCardMouseEvents(mouseEvent, card, cardOwnedLeft);
        }
    }

    public void pickCenter(MouseEvent mouseEvent) {
        if (canPerform(PLAY) && !CardImage.isMissing(cardOwnedCenter.getImage())) {
            final Card card = player.getCards().get(1);
            handleCardMouseEvents(mouseEvent, card, cardOwnedCenter);
        }
    }

    public void pickRight(MouseEvent mouseEvent) {
        if (canPerform(PLAY) && !CardImage.isMissing(cardOwnedRight.getImage())) {
            final Card card = player.getCards().get(2);
            handleCardMouseEvents(mouseEvent, card, cardOwnedRight);
        }
    }

    private void handleCardMouseEvents(MouseEvent e, Card card, ImageView cardImageView) {
        if (e.getButton() == MouseButton.PRIMARY) playCard(card, cardImageView);
        else flipCardImage(card, cardImageView);
    }

    private void playCard(Card card, ImageView imageView) {
        if (isClosed(imageView)) playCardUseCase.discard(new PlayCardUseCase.RequestModel(playerUUID, card));
        else playCardUseCase.playCard(new PlayCardUseCase.RequestModel(playerUUID, card));

        firstCard.setImage(imageView.getImage());
        imageView.setImage(CardImage.ofNoCard().getImage());

        lastUserPlayedCardIndex = lastIntel.openCards().size() + 1;

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

    public void callForPointsRise(ActionEvent actionEvent) {
        if (lastIntel.isGameDone()) closeWindow();
        if (canPerform(RAISE)) betUseCase.raiseBet(playerUUID);
    }

    private void closeWindow() {
        final Stage window = (Stage) btnAction.getScene().getWindow();
        window.close();
    }

    private boolean canPerform(PossibleAction action) {
        final Optional<UUID> possibleUuid = lastIntel.currentPlayerUuid();
        if (possibleUuid.isEmpty()) return false;
        final boolean isCurrentPlayer = possibleUuid.get().equals(playerUUID);
        final boolean isPerformingAllowedAction = lastIntel.possibleActions().contains(action);
        return isCurrentPlayer && isPerformingAllowedAction;
    }

    private void updatePlayerScores() {
        lbOpponentScoreValue.setText(String.valueOf(lastIntel.currentOpponentScore()));
        lbPlayerScoreValue.setText(String.valueOf(lastIntel.currentPlayerScore()));
    }

    public void requestTrucoResponse(Intel intel) {
        final HandScore scoreProposal = intel.scoreProposal().orElseThrow();
        final boolean canRaise = intel.possibleActions().contains(RAISE);

        final WindowTrucoResponse dialog = new WindowTrucoResponse();
        final Integer decision = dialog.showAndWait(bot.getUsername(), scoreProposal.get(), canRaise);

        switch (decision) {
            case -1 -> betUseCase.quit(playerUUID);
            case 0 -> betUseCase.accept(playerUUID);
            case 1 -> betUseCase.raiseBet(playerUUID);
        }
        updateIntel();
    }

    public void requestMaoDeOnzeResponse() {
        updateView();

        final WindowMaoDeOnzeResponse dialog = new WindowMaoDeOnzeResponse();
        final boolean decision = dialog.showAndWait();

        //player.setMaoDeOnzeResponseDecision(decision);
    }
}
