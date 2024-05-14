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
import java.util.Comparator;
import java.util.List;

public class SecondRound implements GameStrategy {

    private TrucoCard vira;
    private List<TrucoCard> hand;
    private double cardsValueAvg;
    private int opponentRaiseCount = 0;

    public SecondRound(GameIntel intel){
        sortHand(intel);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        if (opponentScore >= 9 && opponentScore < 11) {
            return cardsValueAvg >= MODERATE_CARD_VALUE;
        }
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        int botScore = intel.getScore();

        boolean conservative = false;
        boolean aggressive = true;

        if (wonFirstRound(intel) && numberOfStrongCards() > 0) return aggressive;
        if(numberOfStrongCards() >= 1) return aggressive;

        if (opponentScore == 10) {
            return aggressive;
        }
        if (botScore >= 10 && winning(intel)) {
            return conservative;
        }

        return decideToBluff(intel) ? aggressive : conservative;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(wonFirstRound(intel)){
            return minCardToWin(intel);
        }
        if(intel.getOpponentCard().isEmpty()) {
            return CardToPlay.of(hand.get(0));
        }
        return minCardToWin(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        incrementOpponentRaiseCount();
        if (cardsValueAvg >= HIGH_CARD_VALUE || numberOfManilhas() > 0) return 1 ;
        if (cardsValueAvg >= MODERATE_CARD_VALUE ) return 0;
        return -1;
    }

    private void sortHand(GameIntel intel){
        vira = intel.getVira();
        hand = intel.getCards().stream()
                .sorted(Comparator.comparing(card -> card.relativeValue(vira), Comparator.reverseOrder()))
                .toList();
        cardsValueAvg = hand.stream().mapToInt(card -> card.relativeValue(vira)).average().orElse(0);
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
    public boolean opponentIsAggressive() {
        double raiseRate = opponentRaiseCount;
        return raiseRate > 3;
    }

    public boolean decideToBluff(GameIntel intel) {
        return opponentIsAggressive() && intel.getScore() <= 8 && cardsValueAvg < LOW_CARD_VALUE;
    }

    private long numberOfStrongCards(){
        return hand.stream()
                .filter(card -> card.relativeValue(vira) > 8)
                .count();
    }

    private boolean winning(GameIntel intel){
        return intel.getScore() >= intel.getOpponentScore();
    }

    public void incrementOpponentRaiseCount() {
        opponentRaiseCount++;
    }

    private long numberOfManilhas(){
        return hand.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    private boolean wonFirstRound(GameIntel intel){
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    private TrucoCard lastCard(){
        return hand.get(hand.size()-1);
    }
}
