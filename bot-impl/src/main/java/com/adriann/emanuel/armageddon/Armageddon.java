/*
 *  Copyright (C) 2023 Adriann Paranhos - IFSP/SCL and Emanuel José da Silva - IFSP/SCL
 *  Contact: adriann <dot> paranhos <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: emanuel <dot> silva <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.adriann.emanuel.armageddon;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.CardRank.*;

public class Armageddon implements BotServiceProvider {
    private static final int GOOD_HAND_STRENGTH = 25;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> botCards = intel.getCards();
        if (!hasThree(botCards,vira) && !hasManilha(botCards,vira)) return false;

        int goodCard = 0;
        for (TrucoCard card: botCards){
            if (isTwo(vira) && isTwo(card)){
                goodCard++;
            }
            if (card.isManilha(vira) || isThree(card)){
                goodCard++;
            }
        }

        return goodCard >=2;
    }


    // FAZENDO AQUI -----------------------------------------
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> playerHand = intel.getCards();
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : playerHand) {
            if (card.isManilha(vira)) {
                return true;
            }
        }

        int handStrength = handStrength(playerHand, vira);

        if (handStrength >= GOOD_HAND_STRENGTH) {
            return true;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        int rounds = intel.getRoundResults().size();
        TrucoCard vira = intel.getVira();

        Optional<TrucoCard> optOpponentCard = intel.getOpponentCard();
        TrucoCard opponentCard;

        List<TrucoCard> botCards = intel.getCards();
        TrucoCard strongest = strongestCard(botCards,vira);
        TrucoCard weakest = weakestCard(botCards,vira);

        switch (rounds) {
            case 0 -> {
                TrucoCard middle = middleCard(botCards,vira);

                if (optOpponentCard.isEmpty()){
                    if (hasHigherCouple(botCards, vira)) {
                        return CardToPlay.of(weakest);
                    }
                    if (hasManilha(botCards, vira)){
                        return CardToPlay.of(middle);
                    }
                    if (handStrength(botCards, vira) < GOOD_HAND_STRENGTH) {
                        return CardToPlay.of(strongest);
                    }
                    if (handStrength(botCards, vira) > GOOD_HAND_STRENGTH) {
                        return CardToPlay.of(middle);
                    }
                }
                if (optOpponentCard.isPresent()){
                    opponentCard = optOpponentCard.get();

                    if (!canWin(botCards,vira,opponentCard)){
                        return CardToPlay.of(weakest);
                    }

                    if (canDraw(botCards,vira,opponentCard)){
                        Optional<TrucoCard> equalCard = relativelyEqualCard(botCards,vira,opponentCard);
                        if (equalCard.isPresent() && hasManilha(botCards,vira)){
                            if (hasZap(botCards,vira)){
                                return CardToPlay.of(equalCard.get());
                            }
                            return CardToPlay.of(middle);
                        }
                    }

                    if (hasHigherCouple(botCards,vira)){
                        return CardToPlay.of(weakest);
                    }

                    if (hasTwoManilhas(botCards,vira)){
                        if (middle.compareValueTo(opponentCard,vira) > 0){
                            return CardToPlay.of(middle);
                        }
                        return CardToPlay.of(strongest);
                    }

                    if (opponentCard.compareValueTo(middle,vira) < 0){
                        if (opponentCard.compareValueTo(weakest,vira) < 0){
                            return CardToPlay.of(weakest);
                        }
                        return CardToPlay.of(middle);
                    }

                    return CardToPlay.of(strongest);
                }
            }
            case 1 -> {
                if (optOpponentCard.isPresent()){
                    opponentCard = optOpponentCard.get();

                    if (opponentCard.equals(TrucoCard.closed())){
                        return CardToPlay.of(weakest);
                    }
                }
                if (hasZap(botCards,vira)){
                    if (hasTwoManilhas(botCards,vira) || hasThree(botCards,vira)) {
                        return CardToPlay.discard(weakest);
                    }
                    return CardToPlay.of(weakest);
                }
                if (hasTwoManilhas(botCards,vira)){
                    return CardToPlay.of(weakest);
                }
                if (strongest.relativeValue(vira) + weakest.relativeValue(vira) < 12){
                    return CardToPlay.of(strongest);
                }
                return CardToPlay.of(weakest);
            }
        }
        return CardToPlay.of(botCards.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> botCards = intel.getCards();
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (!hasThree(botCards, vira) && !hasManilha(botCards, vira)) {
            return -1;
        }

        boolean wonFirstRound = !roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON;

        if (wonFirstRound && !hasTwoManilhas(botCards, vira) && !hasHigherCouple(botCards, vira)) {
            return -1;
        }

        if (hasTwoManilhas(botCards, vira) && !hasHigherCouple(botCards, vira)) {
            return 1;
        }

        if (hasHigherCouple(botCards, vira)) {
            return 1;
        }

        return 0;
    }




    private boolean hasManilha(List<TrucoCard> cards,TrucoCard vira){
        for (TrucoCard card:cards){
            if (card.isManilha(vira)) return true;
        }
        return false;
    }

    private boolean hasThree(List<TrucoCard> cards,TrucoCard vira){
        for (TrucoCard card:cards){
            if (card.compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), vira) == 0){
                return true;
            }
        }
        return false;
    }

    private boolean isThree(TrucoCard card){
        return card.getRank().equals(THREE);
    }

    private boolean isTwo(TrucoCard card){
        return card.getRank().equals(TWO);
    }

    private boolean hasHigherCouple(List<TrucoCard> cards, TrucoCard vira){
        int score = 0;
        for (TrucoCard card:cards){
            if (card.isCopas(vira)) score++;
            if (card.isZap(vira)) score++;
        }

        return score == 2;
    }

    private boolean hasTwoManilhas(List<TrucoCard> cards, TrucoCard vira){
        int score = 0;
        for (TrucoCard card:cards){
            if (card.isManilha(vira)) score++;
        }

        return score >= 2;
    }

    private boolean hasZap(List<TrucoCard> cards, TrucoCard vira){
        for (TrucoCard card:cards){
            if (card.isZap(vira)) return true;
        }

        return false;
    }

    private TrucoCard weakestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard weakest = cards.get(0);
        for (TrucoCard card:cards){
            if (weakest.compareValueTo(card,vira) > 0) weakest = card;
        }

        return weakest;
    }

    private TrucoCard strongestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard strongest = cards.get(0);
        for (TrucoCard card:cards){
            if (strongest.compareValueTo(card,vira) < 0) strongest = card;
        }

        return strongest;
    }

    private TrucoCard middleCard(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> sortedCarts = cards.stream().
                sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .toList();

        return sortedCarts.get(1);
    }

    private int handStrength(List<TrucoCard> cards, TrucoCard vira){
        int strength = 0;
        for (TrucoCard card:cards){
            strength += card.relativeValue(vira);
        }

        return strength;
    }

    private Optional<TrucoCard> relativelyEqualCard(List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        for (TrucoCard card:cards){
            if (card.relativeValue(vira) == opponentCard.relativeValue(vira)) return Optional.of(card);
        }
        return Optional.empty();
    }
    private boolean canWin(List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        return strongestCard(cards,vira).compareValueTo(opponentCard,vira) > 0;
    }

    private boolean canDraw(List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        return relativelyEqualCard(cards,vira,opponentCard).isPresent();
    }

    // --------------------------------------------------------------------------
    public boolean hasManilhaAndThree(List<TrucoCard> playerHand, TrucoCard vira) {
        boolean hasManilha = false;
        boolean hasThree = false;

        for (TrucoCard card : playerHand) {
            if (card.isManilha(vira)) {
                hasManilha = true;
            }
            if (card.getRank() == CardRank.THREE) {
                hasThree = true;
            }

            if (hasManilha && hasThree) {
                return true;
            }
        }
        return hasManilha && hasThree;
    }

    public boolean shouldRequestTruco(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (!roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON) {
            if (roundResults.size() == 1) {
                return true;
            }
            return true;
        }

        return !roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.DREW;
    }


    public TrucoCard playBestCard(GameIntel intel) {
        List<TrucoCard> playerHand = intel.getCards();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard vira = intel.getVira();

        TrucoCard strongestCard = playerHand.get(0);
        for (TrucoCard card : playerHand) {
            if (card.compareValueTo(strongestCard, vira) > 0) {
                strongestCard = card;
            }
        }

        if (opponentCard.isPresent()) {
            TrucoCard opponent = opponentCard.get();
            if (strongestCard.compareValueTo(opponent, vira) > 0) {
                return strongestCard;
            } else {
                TrucoCard weakestCard = playerHand.get(0);
                for (TrucoCard card : playerHand) {
                    if (card.compareValueTo(weakestCard, vira) < 0) {
                        weakestCard = card;
                    }
                }
                return weakestCard;
            }
        }

        return strongestCard;
    }





}
