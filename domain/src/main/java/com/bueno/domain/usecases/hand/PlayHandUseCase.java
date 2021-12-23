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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.*;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

//TODO Split this class with different use cases (PlayCardUseCase, AcceptBetUseCase...)
//TODO Write test cases for uncovered code (use cases and entities)
public class PlayHandUseCase {

    private Game game;
    private Hand hand;
    private GameRepository repo;

    public PlayHandUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    public Intel playCard(UUID usedUuid, Card card) {
        return playCard(usedUuid, card, false);
    }

    public Intel discard(UUID usedUuid, Card card) {
        return playCard(usedUuid, card, true);
    }

    private Intel playCard(UUID usedUuid, Card card, boolean discard){
        final Validator<UUID> validator = new ActionRequestValidator(repo, PossibleActions.PLAY);
        final Notification notification = validator.validate(usedUuid);

        if(card == null) notification.addError("Card is null");
        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();
        final Card playingCard = discard ? player.discard(card) : player.play(card);

        if(hand.getCardToPlayAgainst().isEmpty()) hand.playFirstCard(player, playingCard);
        else hand.playSecondCard(player, playingCard);
        hand.getResult().ifPresent(unused -> updateGameStatus(game));

        if(game.isDone()) return game.getIntel();
        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());
        return game.getIntel();
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if(!game.isDone()) game.prepareNewHand();
    }

    public Intel raiseBet(UUID usedUuid){
        final Validator<UUID> validator = new ActionRequestValidator(repo, PossibleActions.RAISE);
        final Notification notification = validator.validate(usedUuid);

        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.raiseBet(player);

        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());
        return game.getIntel();
    }

    public Intel accept(UUID usedUuid){
        final Validator<UUID> validator = new ActionRequestValidator(repo, PossibleActions.ACCEPT);
        final Notification notification = validator.validate(usedUuid);

        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.accept(player);

        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());
        return game.getIntel();
    }

    public Intel quit(UUID usedUuid){
        final Validator<UUID> validator = new ActionRequestValidator(repo, PossibleActions.QUIT);
        final Notification notification = validator.validate(usedUuid);

        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.quit(player);
        hand.getResult().ifPresent(unused -> updateGameStatus(game));

        if(game.isDone()) return game.getIntel();
        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());

        return game.getIntel();
    }

    public List<Intel> findIntelSince(UUID usedUuid, Intel lastIntel){
        final Validator<UUID> validator = new QueryRequestValidator(repo);
        final Notification notification = validator.validate(usedUuid);
        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());
        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        return game.getIntelSince(lastIntel);
    }

    public List<Card> getOwnedCards(UUID usedUuid){
        final Validator<UUID> validator = new QueryRequestValidator(repo);
        final Notification notification = validator.validate(usedUuid);
        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());
        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Player player = game.getPlayer1().getUuid().equals(usedUuid) ? game.getPlayer1() : game.getPlayer2();
        return List.copyOf(player.getCards());
    }









    public PlayHandUseCase(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    public Hand play() {
        hand = game.prepareNewHand();

        if(game.isMaoDeOnze()){
            handleMaoDeOnze();
            if (hand.hasWinner()) return hand;
        }

        playRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterSecondRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private void handleMaoDeOnze() {
        Player playerInMaoDeOnze = game.getPlayer1().getScore() == 11? game.getPlayer1() : game.getPlayer2();
        Player otherPlayer = game.getPlayer1().getScore() == 11? game.getPlayer2() : game.getPlayer1();

        if(playerInMaoDeOnze.getMaoDeOnzeResponse()) hand.setScore(HandScore.THREE);
        else hand.setResult(new HandResult(otherPlayer, HandScore.ONE));
    }

    private void playRound() {
        hand.playNewRound();
    }


}
