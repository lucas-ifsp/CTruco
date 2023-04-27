/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

// Authors: Cauê Gastaldi and Isabela Forti

package com.caueisa.destroyerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class DestroyerBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int scoreDifference = getScoreDifference(intel);
        if (intel.getHandPoints() == 3 && intel.getOpponentScore() >= 9)
            return 1;
        if (intel.getHandPoints() == 6 && intel.getOpponentScore() >= 6)
            return 1;
        if (intel.getHandPoints() == 9 && intel.getOpponentScore() >= 3)
            return 0;
        if (scoreDifference > 0) {
            if (scoreDifference <= 3 && getCardsAboveRank(intel, CardRank.SEVEN).size() == intel.getCards().size())
                return 0;
            if (scoreDifference <= 6 && getCardsAboveRank(intel, CardRank.SEVEN).size() == intel.getCards().size())
                return 1;
        }
        if (scoreDifference <= -4 && getManilhas(intel).size() == intel.getCards().size())
            return 0;

        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore() == 11)
            return true;
        if(getCardsAboveRank(intel, CardRank.ACE).size() == 3 && getManilhas(intel).size() > 0 && intel.getOpponentScore() <= 8)
            return true;
        if(intel.getOpponentScore() >= 8 && getCardsAboveRank(intel, CardRank.KING).size() == 3)
            return true;
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getOpponentScore() == 11)
            return true;
        if (intel.getScore() != 11) {
            int roundNumber = getRoundNumber(intel);
            int scoreDifference = getScoreDifference(intel);
            if(intel.getOpponentScore() <= 7){
                return true;
            }else {
                if (roundNumber == 1)
                    if (getCardsAboveRank(intel, CardRank.TWO).size() >= 2)
                        return true;
                if (roundNumber == 2) {
                    if (getCardsAboveRank(intel, CardRank.TWO).size() >= 1)
                        return true;
                    if (isTheFirstToPlay(intel) && getCardsAboveRank(intel, CardRank.ACE).size() == intel.getCards().size())
                        return true;
                }
                if (roundNumber == 3) {
                    if (!getCardsAboveRank(intel, CardRank.ACE).isEmpty())
                        return true;
                }
                if (scoreDifference > 0) {
                    if (scoreDifference <= 3)
                        return true;
                }
                if (scoreDifference <= -6 && hasStrongestManilhas(intel))
                    return true;
                if (scoreDifference >= -3 && getCardsAboveRank(intel, CardRank.ACE).size() == intel.getCards().size())
                    return true;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        int roundNumber = getRoundNumber(intel);

        TrucoCard lowestCard = getLowestCardBetweenAllCardsAvailableToBePlayed(intel);
        Optional<TrucoCard> lowestCardStrongerThanOpponentCard;
        Optional<TrucoCard> cardEqualsToOpponentCard;
        TrucoCard vira = intel.getVira();

        switch (roundNumber) {
            case 1 -> {

                if(isTheFirstToPlay(intel)) {
                    if (hasCardWithRank(intel, CardRank.THREE)) {
                        return CardToPlay.of(getStrongerCardWithRank(intel, CardRank.THREE).get());
                    }
                    if (hasAtLeastTwoManilhas(intel)) {
                        return CardToPlay.of(getLowestRankManilha(intel).get());
                    }
                    if (getManilhas(intel).size() == 1){
                        return CardToPlay.of(getManilhas(intel).get(0));
                    }
                    return CardToPlay.of(getHighestCardBetweenAllCardsAvailableToBePlayed(intel));
                }


                if (!isTheFirstToPlay(intel)) {
                    if (opponentCardIsHidden(intel))
                        return CardToPlay.of(lowestCard);
                    lowestCardStrongerThanOpponentCard = getLowestCardStrongerThanTheOpponentCard(intel);
                    if (lowestCardStrongerThanOpponentCard.isPresent())
                        return CardToPlay.of(lowestCardStrongerThanOpponentCard.orElse(lowestCard));
                }

            }
            case 2 -> {

                if(isTheFirstToPlay(intel)){
                    if (hasAtLeastTwoManilhas(intel)){
                        return CardToPlay.discard(getLowestRankManilha(intel).get());
                    }
                    if (getManilhas(intel).size() == 1){
                        if(getManilhas(intel).get(0).isZap(vira)){
                            return CardToPlay.discard(getLowestCardBetweenAllCardsAvailableToBePlayed(intel));
                        }
                        return CardToPlay.of(getManilhas(intel).get(0));
                    }
                    if (hasCardWithRank(intel, CardRank.THREE)) {
                        return CardToPlay.of(getStrongerCardWithRank(intel, CardRank.THREE).get());
                    }
                    return CardToPlay.of(getHighestCardBetweenAllCardsAvailableToBePlayed(intel));
                }


                if (!isTheFirstToPlay(intel)) {
                    if (opponentCardIsHidden(intel))
                        return CardToPlay.of(lowestCard);
                    lowestCardStrongerThanOpponentCard = getLowestCardStrongerThanTheOpponentCard(intel);
                    cardEqualsToOpponentCard = getCardEqualsToTheOpponentCard(intel);
                    if (lowestCardStrongerThanOpponentCard.isPresent())
                        return CardToPlay.of(lowestCardStrongerThanOpponentCard.orElse(lowestCard));
                    if (cardEqualsToOpponentCard.isPresent())
                        return CardToPlay.of(cardEqualsToOpponentCard.get());
                }
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    private boolean hasAtLeastTwoManilhas(GameIntel intel){
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream().filter(card -> card.isManilha(vira)).count() >= 2;
    }

    private boolean hasStrongestManilhas(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard zap = TrucoCard.of(vira.getRank().next(), CardSuit.CLUBS);
        TrucoCard copas = TrucoCard.of(vira.getRank().next(), CardSuit.HEARTS);
        if(intel.getCards().contains(zap) && intel.getCards().contains(copas)) {
            return true;
        }
        return false;
    }

    private Optional<TrucoCard> getLowestRankManilha(GameIntel intel){
        TrucoCard vira = intel.getVira();
        if(intel.getCards().stream().anyMatch(card-> card.isManilha(vira))){
            return intel.getCards().stream().filter(card-> card.isManilha(vira)).min((card1, card2) -> card1.compareValueTo(card2, vira));
        }
        return Optional.empty();
    }

    private boolean hasCardWithRank(GameIntel intel, CardRank rank){
        return intel.getCards().stream().anyMatch(card -> card.getRank().equals(rank));
    }

    private Optional<TrucoCard> getStrongerCardWithRank(GameIntel intel, CardRank rank) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.getRank().equals(rank))
                .max((card1, card2) -> card1.compareValueTo(card2, vira));
    }

    private boolean isTheFirstToPlay(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    private boolean opponentCardIsHidden(GameIntel intel) {
        return intel.getOpponentCard().get().getSuit().equals(CardSuit.HIDDEN);
    }

    private int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    private int getScoreDifference(GameIntel intel) {
        return intel.getScore() - intel.getOpponentScore();
    }

    private Optional<TrucoCard> getLowestCardStrongerThanTheOpponentCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().get();
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min( (card1, card2) -> card1.compareValueTo(card2, vira));
    }

    private TrucoCard getLowestCardBetweenAllCardsAvailableToBePlayed(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .min( (card1, card2) -> card1.compareValueTo(card2, vira))
                .get();
    }

    private TrucoCard getHighestCardBetweenAllCardsAvailableToBePlayed(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .max( (card1, card2) -> card1.compareValueTo(card2, vira))
                .get();
    }

    private List<TrucoCard> getCardsAboveRank(GameIntel intel, CardRank rank) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.getRank().compareTo(rank) > 0 || card.isManilha(vira))
                .toList();
    }

    private Optional<TrucoCard> getCardEqualsToTheOpponentCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().get();
        return intel.getCards().stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) == 0)
                .findAny();
    }

    private List<TrucoCard> getManilhas(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .toList();
    }

}
