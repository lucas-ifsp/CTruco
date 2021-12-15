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
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.*;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;

import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

public class PlayHandUseCase {

    private Game game;
    private Hand hand;
    private GameRepository repo;

    public PlayHandUseCase(Game game) {
        this.game = Objects.requireNonNull(game);
        hand = this.game.prepareNewHand();
    }

    public PlayHandUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
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

    public Intel playCard(UUID usedID, Card card){
        Objects.requireNonNull(card);
        Objects.requireNonNull(usedID);
        final Game game = loadGameIfRequestIsValid(usedID);
        final Hand hand = loadHandIfRequestIsValid(usedID, game, PossibleActions.PLAY);
        final Player player = hand.getCurrentPlayer();

        hand.getCardToPlayAgainst().ifPresentOrElse(unused ->
                hand.playSecondCard(player, card), () -> hand.playFirstCard(player, card));

        hand.getResult().ifPresent(unused -> updateGameStatus(game));
        return new Intel(game.getCurrentHand());
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if(game.isDone()) return;
        game.prepareNewHand();
    }

    private Game loadGameIfRequestIsValid(UUID usedID) {
        final Game game = repo.findByUserUuid(usedID).orElseThrow(
                () -> new UnsupportedGameRequestException("User with UUID " + usedID + " is not in an active game."));

        if(game.isDone()) throw new UnsupportedGameRequestException("Game is over. Start a new game.");
        return game;
    }

    private Hand loadHandIfRequestIsValid(UUID usedID, Game game, PossibleActions action){
        final Player requester = game.getPlayer1().getUuid().equals(usedID) ? game.getPlayer1() : game.getPlayer2();
        final Hand hand = game.getCurrentHand();
        //final Hand effectiveCurrentHand = needsNewHand(hand) ? game.prepareNewHand() : hand;

        if(!hand.getCurrentPlayer().equals(requester))
            throw new UnsupportedGameRequestException("User with UUID " + usedID + " is not in not the current player.");

        final EnumSet<PossibleActions> possibleActions = hand.getPossibleActions();
        if(!possibleActions.contains(action))
            throw new UnsupportedGameRequestException("Invalid action for hand state. Valid actions: " + possibleActions);

        return hand;
    }

    public Intel raiseBet(UUID usedID){
        Objects.requireNonNull(usedID);
        final Game game = loadGameIfRequestIsValid(usedID);
        final Hand hand = loadHandIfRequestIsValid(usedID, game, PossibleActions.RAISE);
        final Player player = hand.getCurrentPlayer();
        hand.raiseBet(player);
        return new Intel(hand);
    }

    public Intel accept(UUID usedID){
        Objects.requireNonNull(usedID);
        final Game game = loadGameIfRequestIsValid(usedID);
        final Hand hand = loadHandIfRequestIsValid(usedID, game, PossibleActions.ACCEPT);
        final Player player = hand.getCurrentPlayer();
        hand.accept(player);
        return new Intel(hand);
    }

    public Intel quit(UUID usedID){
        Objects.requireNonNull(usedID);
        final Game game = loadGameIfRequestIsValid(usedID);
        final Hand hand = loadHandIfRequestIsValid(usedID, game, PossibleActions.QUIT);
        final Player player = hand.getCurrentPlayer();
        hand.quit(player);

        hand.getResult().ifPresent(unused -> updateGameStatus(game));
        return new Intel(game.getCurrentHand());
    }
}
