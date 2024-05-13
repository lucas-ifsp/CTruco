/*
 *  Copyright (C) 2024 Alan Andrade Vasconi de Souza - IFSP/SCL and Ian de Oliveira Fernandes - IFSP/SCL
 *  Contact: alan<dot>vasconi<at>aluno<dot>ifsp<dot>edu<dot>br
 *  Contact: ian<dot>f<at>aluno<dot>ifsp<dot>edu<dot>br
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

package com.alanIan.casinhadecabloco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class CasinhaDeCabloco implements BotServiceProvider {
    private TrucoCard vira;
    private List<TrucoCard> hand;
    private int handValue;
    private double cardsValueAvg;
    private int opponentRaiseCount = 0; // MODIFICAÇÃO 2.3

    private static final double HIGH_CARD_VALUE = 8;
    private static final double MODERATE_CARD_VALUE = 6.5;
    private static final double LOW_CARD_VALUE = 3.3;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        sortHand(intel);
        int opponentScore = intel.getOpponentScore();
        if (opponentScore >= 9 && opponentScore < 11) {
            return cardsValueAvg >= MODERATE_CARD_VALUE;
        }
        return true;
    }
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        sortHand(intel);
        int opponentScore = intel.getOpponentScore();
        int BotScore = intel.getScore();

        boolean conservative = false;
        boolean aggressive = true;


        if (BotScore >= 9 || opponentScore >= 9) {
            if (handValue >= MODERATE_CARD_VALUE) return aggressive;
        }

        if (getNumberOfRounds(intel) == 2) {
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                if (handValue >= HIGH_CARD_VALUE) return aggressive;
                return conservative;
            }
            if (handValue >= MODERATE_CARD_VALUE + 2) return aggressive;
        }

        if (BotScore < opponentScore) {
            if (opponentScore == 10) return aggressive;
            if (handValue >= HIGH_CARD_VALUE) return aggressive;
        }

        if (BotScore > opponentScore) {
            if (BotScore >= 10) return conservative;
        }


        if (intel.getOpponentCard().isPresent()) {

            if (hasHighValueCards() >= 2) {
                return aggressive;
            }
        }
        return conservative;
    }

    private int getNumberOfRounds(GameIntel intel){
        return intel.getRoundResults().size();
    }

    private long hasHighValueCards() {
        return hand.stream().filter(card -> card.relativeValue(vira) >= 8).count();
    }


    public boolean opponentIsAggressive() {
        double raiseRate = opponentRaiseCount;
        return raiseRate > 3;
    }

    public boolean decideToBluff(GameIntel intel) {
        if (opponentIsAggressive() && intel.getScore() <= 8 && cardsValueAvg < LOW_CARD_VALUE) {
            return true;
        }
        return false;
    }
    @Override
    public CardToPlay chooseCard(GameIntel intel){
        sortHand(intel);
        return switch (intel.getRoundResults().size()) {
            case 0 -> cardFirstRound(intel);
            case 1 -> cardSecondRound(intel);
            default -> CardToPlay.of(hand.get(0));
        };
    }

    private void sortHand(GameIntel intel){
        vira = intel.getVira();
        hand = intel.getCards().stream()
                .sorted(Comparator.comparing(card -> card.relativeValue(vira), Comparator.reverseOrder()))
                .toList();
        cardsValueAvg = hand.stream().mapToInt(card -> card.relativeValue(vira)).average().orElse(0);
        handValue = hand.stream().mapToInt(card -> card.relativeValue(vira)).sum();
    }

    private CardToPlay cardFirstRound(GameIntel intel){
        if(getManilhaCount() > 1) return CardToPlay.of(hand.get(0));
        if( intel.getOpponentCard().isEmpty()) return CardToPlay.of(hand.get(1));
        return minCardToWin(intel);
    }

    private CardToPlay cardSecondRound(GameIntel intel){
        if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)){
            return minCardToWin(intel);
        }
        if(intel.getOpponentCard().isEmpty()) {
            return CardToPlay.of(hand.get(0));
        }
        return minCardToWin(intel);
    }

    private CardToPlay minCardToWin(GameIntel intel) {
        TrucoCard minCard = lastCard();
        if(intel.getOpponentCard().isPresent()){
            TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

            minCard = hand.stream()
                    .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                    .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(cardToDraw(intel));
        }
        return CardToPlay.of(minCard);
    }

    private TrucoCard cardToDraw(GameIntel intel){
        TrucoCard drawCard = lastCard();
        if(intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().orElse(null);
            drawCard = hand.stream()
                    .filter(card -> card.compareValueTo(opponentCard, vira) == 0)
                    .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(drawCard);
        }
        return drawCard;
    }

    private TrucoCard lastCard(){
        return hand.get(hand.size()-1);
    }

    private long getManilhaCount(){
        return hand.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        sortHand(intel);
        incrementOpponentRaiseCount();
        if (cardsValueAvg >= HIGH_CARD_VALUE || getManilhaCount() > 0) return 1 ;
        if (cardsValueAvg >= MODERATE_CARD_VALUE ) return 0;
        return -1;
    }

    public String getName(){
        return "Casinha de Caboclo";
    }

    public void incrementOpponentRaiseCount() { // MODIFICAÇÃO 2.4
        opponentRaiseCount++;
    }
}
