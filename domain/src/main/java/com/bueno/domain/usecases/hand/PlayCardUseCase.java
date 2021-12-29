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
import com.bueno.domain.usecases.hand.validators.PlayCardValidator;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.Objects;
import java.util.UUID;

public class PlayCardUseCase {

    private Game game;
    private Hand hand;
    private GameRepository repo;

    public PlayCardUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    public Intel playCard(RequestModel requestModel) {
        return playCard(requestModel, false);
    }

    public Intel discard(RequestModel requestModel) {
        return playCard(requestModel, true);
    }

    private Intel playCard(RequestModel request, boolean discard){
        final Validator<RequestModel> validator = new PlayCardValidator(repo, PossibleActions.PLAY);
        final Notification notification = validator.validate(request);

        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(request.requester).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();
        final Card playingCard = discard ? player.discard(request.card) : player.play(request.card);

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

    public record RequestModel(UUID requester, Card card){}

    public PlayCardUseCase(Game game) {
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

        //hand.checkForWinnerAfterSecondRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        //hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private void handleMaoDeOnze() {
        Player playerInMaoDeOnze = game.getPlayer1().getScore() == 11? game.getPlayer1() : game.getPlayer2();
        Player otherPlayer = game.getPlayer1().getScore() == 11? game.getPlayer2() : game.getPlayer1();

        if(playerInMaoDeOnze.getMaoDeOnzeResponse());// hand.setScore(HandScore.THREE);
        else ;//hand.setResult(new HandResult(otherPlayer, HandScore.ONE));
    }

    private void playRound() {
        //hand.playNewRound();
    }


}
