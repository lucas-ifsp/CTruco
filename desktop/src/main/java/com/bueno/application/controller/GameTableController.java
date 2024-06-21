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
import com.bueno.application.utils.TimelineBuilder;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.game.usecase.CreateGameUseCase;
import com.bueno.domain.usecases.game.dtos.CreateDetachedDto;
import com.bueno.domain.usecases.game.dtos.PlayerDto;
import com.bueno.domain.usecases.game.repos.GameRepositoryInMemoryImpl;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PointsProposalUseCase;
import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.bueno.persistence.repositories.UserRepositoryImpl;
import com.remote.RemoteBotApiAdapter;
import javafx.animation.Timeline;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class GameTableController {

    @FXML
    private Label lbPlayerNameValue;
    @FXML
    private Label lbPlayerScoreValue;
    @FXML
    private Label lbOpponentNameValue;
    @FXML
    private Label lbOpponentScoreValue;
    @FXML
    private Label lbHandPointsValue;
    @FXML
    private Label lb1st;
    @FXML
    private Label lb1stValue;
    @FXML
    private Label lb2nd;
    @FXML
    private Label lb2ndValue;
    @FXML
    private Label lb3rd;
    @FXML
    private Label lb3rdValue;
    @FXML
    private Label lbMessage;

    @FXML
    private ImageView cardDeck;
    @FXML
    private ImageView cardVira;
    @FXML
    private ImageView cardOpponentCenter;
    @FXML
    private ImageView cardOpponentRight;
    @FXML
    private ImageView cardOpponentLeft;
    @FXML
    private ImageView cardOwnedCenter;
    @FXML
    private ImageView cardOwnedRight;
    @FXML
    private ImageView cardOwnedLeft;
    @FXML
    private ImageView lastCard;
    @FXML
    private ImageView firstCard;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnQuit;
    @FXML
    private Button btnRaise;

    private String username;
    private String botName;
    private List<ImageView> opponentCardImages;

    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final PointsProposalUseCase pointsProposalUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private UUID userUUID;
    private UUID botUUID;
    private List<CardDto> userCards;
    private int lastUserPlayedCardPosition;
    private final List<IntelDto> missingIntel;
    private IntelDto lastIntel;
    private AtomicBoolean isAnimating;

    public GameTableController() {
        final var gameRepo = new GameRepositoryInMemoryImpl();
        final var botRepo = new RemoteBotRepositoryImpl();
        final var botApi = new RemoteBotApiAdapter();
        final var botManagerService = new BotManagerService(botRepo, botApi);
        gameUseCase = new CreateGameUseCase(gameRepo, botRepo, botApi, botManagerService);
        playCardUseCase = new PlayCardUseCase(gameRepo, botRepo, botApi, botManagerService);
        pointsProposalUseCase = new PointsProposalUseCase(gameRepo, botRepo, botApi, botManagerService);
        handleIntelUseCase = new HandleIntelUseCase(gameRepo);
        missingIntel = new ArrayList<>();
        isAnimating = new AtomicBoolean(false);
    }

    @FXML
    private void initialize() {
        organizeNewHand(null);
    }

    public void organizeNewHand(IntelDto intel) {
        opponentCardImages = new ArrayList<>(List.of(cardOpponentLeft, cardOpponentCenter, cardOpponentRight));
        Collections.shuffle(opponentCardImages);
        resetCardImages();
        setRoundLabelsInvisible();
        updateHandScore(null);

        if (intel != null) {
            dealCards(intel);
            configureButtons(intel);
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

    private void updateHandScore(IntelDto intel) {
        final var value = intel != null ? String.valueOf(intel.handPoints()) : "1";
        lbHandPointsValue.setText(value);
    }

    private void dealCards(IntelDto intel) {
        final var card = CardImage.of(intel.vira());
        userCards = getPlayerCards(intel, userUUID);

        if (userCards.isEmpty()) return;

        cardVira.setImage(card.getImage());
        cardOwnedLeft.setImage(CardImage.of(userCards.get(0)).getImage());
        cardOwnedCenter.setImage(CardImage.of(userCards.get(1)).getImage());
        cardOwnedRight.setImage(CardImage.of(userCards.get(2)).getImage());
    }

    private List<CardDto> getPlayerCards(IntelDto intel, UUID playerUUID) {
        return intel.players().stream()
                .filter(p -> p.uuid().equals(playerUUID))
                .map(PlayerDto::cards)
                .findAny().orElse(null);
    }

    private void configureButtons(IntelDto intel) {
        final Predicate<String> shouldDisable = a -> !intel.possibleActions().contains(a) || !isUserNextPlayer(intel);
        final var baseScore = intel.handPointsProposal() == null ?
                intel.handPoints() : intel.handPointsProposal();
        if (baseScore != 0 && baseScore != 12)
            btnRaise.setText("Pedir " + scoreToString(baseScore == 1 ? 3 : baseScore + 3) + "!");

        btnAccept.setDisable(shouldDisable.test("ACCEPT"));
        btnQuit.setDisable(shouldDisable.test("QUIT"));
        btnRaise.setDisable(shouldDisable.test("RAISE"));
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

    public void createGame(String username, String botName) {
        this.username = username;
        this.botName = botName;
        userUUID = UUID.randomUUID();

        final var request = new CreateDetachedDto(userUUID, username, this.botName);
        lastIntel = gameUseCase.createDetached(request);

        missingIntel.add(lastIntel);

        this.botUUID = lastIntel.players().stream()
                .map(PlayerDto::uuid)
                .filter(uuid -> !uuid.equals(userUUID))
                .findAny().orElseThrow();

        showPlayerNames();
        updateIntel();
        dealCards(lastIntel);
    }

    private void showPlayerNames() {
        lbPlayerNameValue.setText(username);
        lbOpponentNameValue.setText(botName);
    }

    private void updateIntel() {
        final var responseModel = handleIntelUseCase.findIntelSince(userUUID, lastIntel.timestamp());
        missingIntel.addAll(responseModel.intelSinceBaseTimestamp());
        if (missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
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

    private void handleCardPlaying(MouseEvent event, ImageView cardImageView, int cardIndex) {
        if (canPerform("PLAY") && !CardImage.isMissing(cardImageView.getImage())) {
            final var card = userCards.get(cardIndex);
            if (event.getButton() == MouseButton.PRIMARY) playCard(card, cardImageView);
            else flipCardImage(card, cardImageView);
        }
    }

    private boolean canPerform(String action) {
        final var possibleUuid = lastIntel.currentPlayerUuid();
        if (possibleUuid == null) return false;
        final var isCurrentPlayer = possibleUuid.equals(userUUID);
        final var isPerformingAllowedAction = lastIntel.possibleActions().contains(action);
        return !isAnimating.get() && isCurrentPlayer && isPerformingAllowedAction;
    }

    private void playCard(CardDto card, ImageView imageView) {
        final var requestModel = new PlayCardDto(userUUID, card);
        if (isClosed(imageView)) playCardUseCase.discard(requestModel);
        else playCardUseCase.playCard(requestModel);

        firstCard.setImage(imageView.getImage());
        imageView.setImage(CardImage.ofNoCard().getImage());

        lastUserPlayedCardPosition = lastIntel.openCards().size() + 1;
        updateIntel();
        updateView();
    }

    private void flipCardImage(CardDto card, ImageView currentCard) {
        if (lastIntel.roundsPlayed() == 0) return;
        if (!isClosed(currentCard)) currentCard.setImage(CardImage.ofClosedCard().getImage());
        else currentCard.setImage(CardImage.of(card).getImage());
    }

    private boolean isClosed(ImageView imageView) {
        return CardImage.isCardClosed(imageView.getImage());
    }

    private void updateView() {
        final var builder = new TimelineBuilder();
        hideMessage();
        isAnimating = new AtomicBoolean(true);
        while (!missingIntel.isEmpty()) {
            final var intel = missingIntel.remove(0);
            final var event = intel.event() != null ? intel.event() : "";
            //System.out.println(intel);

            if (hasHandScoreChange(intel)) builder.append(0.5, () -> updateHandScore(intel));
            if (isBotEvent(intel)) addBotAnimation(builder, intel, event);
            addSupportingAnimation(builder, intel);
            if (isUserNextPlayer(intel)) addNotificationToUser(builder, intel);
        }
        final Timeline timeline = builder.build();
        timeline.setOnFinished(e -> isAnimating = new AtomicBoolean(false));
        Platform.runLater(timeline::play);
    }

    private void hideMessage() {
        lbMessage.setVisible(false);
        lbMessage.setText("");
    }

    private void showMessage(String message) {
        lbMessage.setVisible(true);
        lbMessage.setText(message);
    }

    private boolean hasHandScoreChange(IntelDto intel) {
        final var event = intel.event();
        if (event == null) return false;
        return event.equals("RAISE") || event.equals("ACCEPT") || event.equals("ACCEPT_HAND");
    }

    private boolean isBotEvent(IntelDto intel) {
        return botUUID.equals(intel.eventPlayerUuid());
    }

    private void addBotAnimation(TimelineBuilder builder, IntelDto intel, String event) {
        switch (event) {
            case "PLAY" -> {
                if (hasOpponentCardToShow(intel)) builder.append(0.5, () -> showOpponentCard(intel));
                builder.append(0.5, () -> updateRoundResults(intel));
            }
            case "QUIT" -> {
                builder.append(0.5, () -> showMessage(botName + " correu!"));
                builder.append(1.5, this::hideMessage);
            }
            case "QUIT_HAND" -> {
                builder.append(1.0, () -> showMessage(botName + " não aceitou a mão!"));
                builder.append(1.5, this::hideMessage);
            }
            case "ACCEPT" -> {
                builder.append(0.5, () -> showMessage(botName + " aceitou!"));
                builder.append(1.5, this::hideMessage);
            }
        }
    }

    private boolean hasOpponentCardToShow(IntelDto intel) {
        return lastUserPlayedCardPosition < intel.openCards().size();
    }

    private void showOpponentCard(IntelDto intel) {
        final var openCards = intel.openCards();
        final var card = openCards.get(openCards.size() - 1);
        final var cardImage = CardImage.of(card).getImage();
        final var randomCardImage = removeRandomOpponentCard();
        randomCardImage.setImage(CardImage.ofNoCard().getImage());
        lastCard.setImage(cardImage);
    }

    private synchronized ImageView removeRandomOpponentCard() {
        return opponentCardImages.remove(0);
    }

    private void updateRoundResults(IntelDto intel) {
        final var roundsPlayed = intel.roundsPlayed();
        if (roundsPlayed == 0) setRoundLabelsInvisible();
        if (roundsPlayed >= 1) showRoundResult(1, lb1st, lb1stValue, intel);
        if (roundsPlayed >= 2) showRoundResult(2, lb2nd, lb2ndValue, intel);
        if (roundsPlayed >= 3) showRoundResult(3, lb3rd, lb3rdValue, intel);
    }

    private void showRoundResult(int roundNumber, Label roundLabel, Label roundResultLabel, IntelDto intel) {
        final var roundsPlayed = intel.roundsPlayed();
        final var roundIndex = roundNumber - 1;

        if (roundIndex >= roundsPlayed) return;

        final var resultText = intel.roundWinnersUsernames().get(roundNumber - 1).orElse("Empate");
        roundResultLabel.setText(resultText);
        roundLabel.setVisible(true);
        roundResultLabel.setVisible(true);
    }

    private void addSupportingAnimation(TimelineBuilder builder, IntelDto intel) {
        builder.append(1.0, () -> updateRoundResults(intel));
        if (hasCardsToClean(intel)) {
            builder.append(0.5, () -> updateRoundResults(intel));
            builder.append(2.5, this::clearPlayedCards);
        }
        if (isNewRound(intel)) {
            lastUserPlayedCardPosition = 1;
            builder.append(1.5, () -> organizeNewHand(intel));
        }
        if (intel.isGameDone()) {
            final String message = "Game Over - Você " + (getPlayerScore(intel, userUUID) == 12 ? "Venceu!" : "Perdeu.");
            builder.append(0.5, this::resetCardImages);
            builder.append(0.25, () -> showMessage(message));
            builder.append(this::setRoundLabelsInvisible);
            builder.append(() -> updateHandScore(null));
        }
        builder.append(() -> updatePlayerScores(intel));
        builder.append(() -> configureButtons(intel));
    }

    private boolean hasCardsToClean(IntelDto intel) {
        final var cardsPlayed = intel.openCards().size();
        final var isSecondCardOfRound = cardsPlayed % 2 == 1;
        final var event = intel.event();
        if (event == null) return false;
        return event.equals("PLAY") && cardsPlayed > 1 && isSecondCardOfRound;
    }

    private void clearPlayedCards() {
        lastCard.setImage(CardImage.ofNoCard().getImage());
        firstCard.setImage(CardImage.ofNoCard().getImage());
    }

    private boolean isNewRound(IntelDto intel) {
        final var isFirstHand = getPlayerScore(intel, userUUID) == 0 && getPlayerScore(intel, botUUID) == 0;
        final var event = intel.event();
        if (event == null) return false;
        return event.equals("HAND_START") && !isFirstHand;
    }

    private int getPlayerScore(IntelDto intel, UUID playerUUID) {
        return intel.players().stream()
                .filter(p -> p.uuid().equals(playerUUID))
                .mapToInt(PlayerDto::score)
                .findAny().orElse(0);
    }

    private void updatePlayerScores(IntelDto intel) {
        lbPlayerScoreValue.setText(String.valueOf(getPlayerScore(intel, userUUID)));
        lbOpponentScoreValue.setText(String.valueOf(getPlayerScore(intel, botUUID)));
    }

    private boolean isUserNextPlayer(IntelDto intel) {
        return userUUID.equals(intel.currentPlayerUuid());
    }

    private void addNotificationToUser(TimelineBuilder builder, IntelDto intel) {
        if (shouldDecideMaoDeOnze(intel)) {
            builder.append(0.5, () -> showMessage("Sua mão de onze"));
        }
        if (hasRaiseProposal(intel)) {
            final var value = scoreToString(intel.handPointsProposal());
            builder.append(0.25, () -> showMessage(botName + " pediu " + value + " !"));
        }
    }

    private boolean shouldDecideMaoDeOnze(IntelDto intel) {
        return intel.isMaoDeOnze() && intel.handPoints() == 1;
    }

    private boolean hasRaiseProposal(IntelDto intel) {
        return intel.handPointsProposal() != null;
    }

    public void accept(ActionEvent a) {
        handleScoreChange("ACCEPT", () -> pointsProposalUseCase.accept(userUUID));
    }

    public void quit(ActionEvent a) {
        handleScoreChange("QUIT", () -> pointsProposalUseCase.quit(userUUID));
    }

    public void raise(ActionEvent a) {
        handleScoreChange("RAISE", () -> pointsProposalUseCase.raise(userUUID));
    }

    private void handleScoreChange(String action, Runnable request) {
        if (canPerform(action)) {
            request.run();
            updateIntel();
            updateView();
        }
    }
}