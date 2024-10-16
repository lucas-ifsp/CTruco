/*
 *  Copyright (C) 2024 Pedro H.Belini and Lucas Luciano
 *  Contact: Pedro <dot> Belini <at> ifsp <dot> edu <dot> br
 *  Contact: Lucas <dot> Luciano <at> ifsp <dot> edu <dot> br
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

package com.belini.luciano.matapatobot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MataPatoBot implements BotServiceProvider{
    private TrucoCard highCard;
    private TrucoCard lowCard;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();
        long playableCards;
        if(opponentScore <= 3){
            return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isManilha(vira));
        }
        if(opponentScore < 6){
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) >= 7)
                    .count();

            return playableCards >= 2;

        }
        if (opponentScore <= 8) {
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) >= 8)
                    .count();

            return playableCards >= 2;

        }
        if (opponentScore < 11) {
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) > 9)
                    .count();

            return playableCards >= 1 && intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6);
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        if(intel.getCards().size() == 3){
            if (handValue(intel) == 27 ) {
                return true;
            }
        if (intel.getCards().size() == 2){
            if (intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW) ){
                if(intel.getCards().stream().anyMatch(card -> card.isCopas(vira) ||  card.isZap(vira) ))
                return true;
            }
        }

        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard bestCard = setHighCard(intel);
        TrucoCard averageCard = setMidCard(intel);
        TrucoCard worstCard = setLowCard(intel);
        boolean torna = intel.getOpponentCard().isPresent();
        boolean hasZap = intel.getCards().stream().anyMatch(card -> card.isZap(vira));
        boolean hasCopas = intel.getCards().stream().anyMatch(card -> card.isCopas(vira));
        long countManilha = intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .count();


        if (intel.getCards().size() == 3) {
            if (hasZap && hasCopas) return CardToPlay.of(worstCard);

            if (torna) {
                if (drewFirst(intel) && (hasZap || hasCopas)) {
                    if (worstCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0) {
                        return CardToPlay.of(worstCard);
                    }
                    if (averageCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0) {
                        return CardToPlay.of(averageCard);
                    }
                }

                if (worstCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(worstCard);
                if (averageCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0)
                    return CardToPlay.of(averageCard);
                if (bestCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(bestCard);
                return CardToPlay.of(worstCard);
            }
            if (countManilha >= 1 && intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) == 9)) {
                return CardToPlay.of(averageCard);
            }
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getCards().size() == 3) {
            if (countManilha(intel) >= 2 ){
                return 1;
            }
            if (countManilha(intel) >= 1 && handValue(intel) >= 24) {
                return 0;
            }
        }
        else if (intel.getCards().size() == 2) {
            if (countManilha(intel) == 2) {
                return 1;
            }
            if (countManilha(intel) >= 1 || handValue(intel) >= 17) {
                return 0;
            }
        }
        else if (intel.getCards().size() == 1){
            if (handValue(intel) >= 12){
                return 1;
            }
            if (handValue(intel) >= 9){
                return 0;
            }
        }

        return -1;
    }


    public Boolean checkFirstPlay (Optional<TrucoCard> opponentCard){
        return opponentCard.isPresent();
    }

    public TrucoCard KillingOpponentCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard cardToPlay = null;

        if (!opponentCard.isPresent()) {
            return null;
        }

        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(lowestCard, vira) < 0) {
                lowestCard = card;
            }
            if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) {
                    cardToPlay = card;
                }
            }
        }

        return cardToPlay != null ? cardToPlay : lowestCard;
    }
    public TrucoCard shouldPlayStrongCard(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard strongestCard = null;
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        boolean opponentPlayedManilha = opponentCard != null && opponentCard.isManilha(vira);

        for (TrucoCard card : hand) {
            if (!card.isZap(vira) && !card.isCopas(vira) && !card.isEspadilha(vira)) {
                if (strongestCard == null || card.relativeValue(vira) > strongestCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }

            if (opponentPlayedManilha && card.isManilha(vira)) {
                if (strongestCard == null || card.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }
        }

        return strongestCard;
    }

    public String roundCheck(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        int cardCount = cards.size();

        return switch (cardCount) {
            case 3 -> "Round 1";
            case 2 -> "Round 2";
            case 1 -> "Round 3";
            default -> "No cards";
        };
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        List <TrucoCard> cards = intel.getCards();
        return cards.size();
    }

    public int handValue(GameIntel intel){
        int handSValue = 0;
        for (TrucoCard card : intel.getCards()){
            handSValue += card.relativeValue(intel.getVira());
        }
        return handSValue;
    }
    public long countManilha (GameIntel intel) {
        long count = intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
        return count;
    }
    private TrucoCard setHighCard(GameIntel intel){
        TrucoCard vira = intel.getVira();
        this.highCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) > highCard.relativeValue(vira)) highCard = card;
        }

        return highCard;
    }
    private TrucoCard setLowCard(GameIntel intel){
        TrucoCard vira = intel.getVira();
        this.lowCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) < lowCard.relativeValue(vira)) lowCard = card;
        }

        return lowCard;
    }

    private TrucoCard setMidCard(GameIntel intel){
        TrucoCard vira = intel.getVira();
        TrucoCard midCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(!Objects.equals(highCard, card) && !Objects.equals(lowCard, card)) midCard = card;
        }

        return midCard;
    }
    private boolean drewFirst(GameIntel intel){
        TrucoCard vira =intel.getVira();
        if(intel.getOpponentCard().isPresent()){
            return intel.getCards().stream().anyMatch(card -> card.compareValueTo(intel.getOpponentCard().get(), vira) == 0);
        }
        return  false;
    }
}
