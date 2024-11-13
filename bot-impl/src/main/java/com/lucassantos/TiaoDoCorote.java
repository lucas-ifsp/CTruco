package com.lucassantos;

/*
 *  Copyright (C) 2023 Lucas Matheus dos Santos - IFSP/SCL
 *  Contact: e <dot> correa <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: marcos <dot> carini <at> aluno <dot> ifsp <dot> edu <dot> br
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

import com.bueno.spi.service.BotServiceProvider;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TiaoDoCorote implements BotServiceProvider {
    private final GameIntel gameIntel;
    private final Random random;

    public TiaoDoCorote(GameIntel gameIntel) {
        this.gameIntel = gameIntel;
        this.random = new Random();
    }

    /**
     * Faz uma jogada baseada no estado do jogo.
     * @return TrucoCard a carta escolhida para jogar.
     */
    @Override
    public TrucoCard makeMove() {
        List<TrucoCard> myCards = gameIntel.getCards();
        Optional<TrucoCard> opponentCard = gameIntel.getOpponentCard();

        // Se o oponente jogou, escolhe a melhor carta para jogar contra a carta dele
        if (opponentCard.isPresent()) {
            return chooseBestCardAgainst(opponentCard.get(), myCards);
        } else {
            // Se o oponente não jogou, joga uma carta aleatória
            return playRandomCard(myCards);
        }
    }

    /**
     * Escolhe a melhor carta contra a carta do oponente.
     * @param oppCard TrucoCard a carta jogada pelo oponente.
     * @param myCards List<TrucoCard> cartas disponíveis do bot.
     * @return TrucoCard a melhor carta para jogar contra o oponente.
     */
    private TrucoCard chooseBestCardAgainst(TrucoCard oppCard, List<TrucoCard> myCards) {
        TrucoCard bestCard = myCards.get(0);

        // Lógica simples: tenta achar uma carta que seja mais forte que a do oponente
        for (TrucoCard myCard : myCards) {
            if (myCard.getValue() > oppCard.getValue()) {
                return myCard;
            }
        }

        // Se não encontrar uma carta mais forte, joga a mais forte que o bot tiver
        for (TrucoCard myCard : myCards) {
            if (myCard.getValue() > bestCard.getValue()) {
                bestCard = myCard;
            }
        }

        return bestCard;
    }

    /**
     * Joga uma carta aleatória das que estão disponíveis.
     * @param myCards List<TrucoCard> cartas disponíveis do bot.
     * @return TrucoCard uma carta escolhida aleatoriamente.
     */
    private TrucoCard playRandomCard(List<TrucoCard> myCards) {
        return myCards.get(random.nextInt(myCards.size()));
    }

    /**
     * Verifica a pontuação e decide se deve pedir truco.
     * @return boolean true se o bot deve pedir truco, false caso contrário.
     */
    @Override
    public boolean shouldAskForTruco() {
        int myScore = gameIntel.getScore();
        int opponentScore = gameIntel.getOpponentScore();
        int handPoints = gameIntel.getHandPoints();

        // Exemplo de lógica para pedir truco: sempre que a pontuação estiver atrás e a mão for fraca, o bot pede truco
        return (myScore < opponentScore && handPoints < 3) || random.nextBoolean();
    }

    /**
     * Decide se deve aceitar um truco pedido pelo oponente.
     * @return boolean true se o bot aceitar o truco, false caso contrário.
     */
    @Override
    public boolean shouldAcceptTruco() {
        int myScore = gameIntel.getScore();
        int opponentScore = gameIntel.getOpponentScore();

        // Exemplo de lógica: aceita truco se estiver com uma pontuação maior ou igual ao oponente
        return myScore >= opponentScore || random.nextBoolean();
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        // Simples decisão para aceitar ou não a "mão de onze"
        return random.nextBoolean();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // Lógica simples para decidir se deve pedir truco
        return random.nextBoolean();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        // Implementação simplificada para escolher uma carta
        return new CardToPlay(playRandomCard(gameIntel.getCards()), false);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // Lógica simples para aceitar ou aumentar o truco
        return random.nextBoolean() ? 1 : 0;
    }

    public static void main(String[] args) {
        // Exemplo de inicialização do bot com dados fictícios de GameIntel
        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), List.of(), new TrucoCard(1, "Espadas"), 2)
                .botInfo(List.of(new TrucoCard(2, "Copas"), new TrucoCard(3, "Espadas")), 8)
                .opponentScore(6)
                .build();

        TiaoDoCorote bot = new TiaoDoCorote(gameIntel);

        // Exemplo de jogadas do bot
        TrucoCard playedCard = bot.makeMove();
        System.out.println("Bot jogou: " + playedCard);

        boolean askForTruco = bot.shouldAskForTruco();
        System.out.println("Bot pediu truco? " + askForTruco);

        boolean acceptTruco = bot.shouldAcceptTruco();
        System.out.println("Bot aceitou truco? " + acceptTruco);
    }
}


