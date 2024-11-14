/*
 *  Copyright (C) 2024 Giullia Cuerva - IFSP/SCL and Márcio Lucas Dantas - IFSP/SCL
 *  Contact: giullia <dot> cuerva <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: d <dot> marcio <at> aluno <dot> ifsp <dot> edu <dot> br
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


package com.giullia.marcio.trucomante;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class Trucomante implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return isHandStrong(intel) ||
                containsOneManilha(intel) && isHandStrong(intel) ||
                (!containsOneManilha(intel) && intel.getOpponentScore() < 8 && getPowerfulCardsQuantity(intel) > 0);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if(intel.getRoundResults().size() == 1){
            if(isHandStrong(intel)) return true;
            if(containsTwoManilhasOrMore(intel)) return true;
            if(containsOneManilha(intel)) return true;
            if(roundResults.get(0) == GameIntel.RoundResult.WON){
                if(containsZap(intel))
                    return true;
                if(containsOneManilha(intel))
                    return true;
                if(containsThree(intel) || containsTwo(intel) || containsAce(intel))
                    return true;
            }
            if(roundResults.get(0) == GameIntel.RoundResult.LOST){
                if(isHandStrong(intel)) return true;
            }
        }

        if(intel.getRoundResults().size() == 2){
            if(containsZap(intel))
                return true;
            if(containsOneManilha(intel))
                return true;
            return containsThree(intel);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        switch (intel.getRoundResults().size()){
            case 0 -> {
                if(intel.getOpponentCard().isEmpty()){
                    return startFirstRound(intel);
                }
                return firstRoundResponse(intel);
            }
            case 1 -> {
                if(intel.getOpponentCard().isEmpty()){
                    return startSecondRound(intel);
                }
                return secondRoundResponse(intel);
            }
            case 2 -> {
                return lastRoundDiscard(intel);
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if(roundResults.isEmpty()){
            if(containsBiggestCouple(intel)){
                return 1;
            }
            if(containsTwoManilhasOrMore(intel)){
                return 1;
            }
            if(isHandStrong(intel))
                return 0;
        }
        if(roundResults.size() == 1){
            if(containsBiggestCouple(intel))
                return 1;
            if(containsTwoManilhasOrMore(intel) && (roundResults.get(0) == GameIntel.RoundResult.WON
                    || roundResults.get(0) == GameIntel.RoundResult.DREW))
                return 1;
            if(containsTwoManilhasOrMore(intel) && roundResults.get(0) == GameIntel.RoundResult.LOST)
                return 0;
            if(containsOneManilha(intel))
                return 0;
            if(isHandStrong(intel))
                return 0;
            if(containsThree(intel) || containsTwo(intel))
                return 0;

        }
        if(roundResults.size() == 2){
            if(containsZap(intel))
                return 1;
            if(containsOneManilha(intel) || containsThree(intel))
                return 1;
            if(containsTwo(intel) || containsAce(intel))
                return 0;
        }
        return -1;
    }

    public CardToPlay startFirstRound(GameIntel intel){
        List<TrucoCard> myCards = sortHandByRelativeValue(intel.getCards(), intel.getVira());

        TrucoCard weakest = myCards.get(0);
        TrucoCard middle = myCards.get(1);
        TrucoCard strongest = myCards.get(2);

        if(isHandStrong(intel)){
            if(containsBiggestCouple(intel))
                return CardToPlay.of(weakest);
            if(containsTwoManilhasOrMore(intel))
                return CardToPlay.of(middle);
            if(containsOneManilha(intel))
                return CardToPlay.of(middle);
            return CardToPlay.of(strongest);
        }
        return CardToPlay.of(strongest);
    }

    private boolean isHandStrong(GameIntel intel) {
        return getPowerfulCardsQuantity(intel) > 1;
    }

    private int getPowerfulCardsQuantity(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        int qtManilhas = 0;

        if(!isManilhaThree(intel) && !isManilhaTwo(intel)){
            qtManilhas = (int) cards.stream().filter(card -> card.isManilha(vira)).count();
        }

        int qtThree = (int) cards.stream().filter(card -> card.getRank() == CardRank.THREE).count();

        int qtTwo = (int) cards.stream().filter(card -> card.getRank() == CardRank.TWO).count();

        return  qtTwo + qtThree + qtManilhas;
    }

    private boolean isManilhaThree(GameIntel intel) {
        return intel.getVira().getRank() == CardRank.THREE;
    }

    private boolean isManilhaTwo(GameIntel intel) {
        return intel.getVira().getRank() == CardRank.TWO;
    }


    public CardToPlay startSecondRound(GameIntel intel){
        List<TrucoCard> myCards = sortHandByRelativeValue(intel.getCards(), intel.getVira());
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        if(isFirstRoundDrew(intel))
            return CardToPlay.of(myCards.get(1));
        if(roundResults.get(0) == GameIntel.RoundResult.WON){
            return CardToPlay.of(myCards.get(0));
        }
        return CardToPlay.of(myCards.get(1));
    }

    public CardToPlay secondRoundResponse(GameIntel intel){
        List<TrucoCard> myCards = sortHandByRelativeValue(intel.getCards(), intel.getVira());
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard vira = intel.getVira();

        TrucoCard weakest = myCards.get(0);
        TrucoCard strongest = myCards.get(1);

        if(isFirstRoundDrew(intel))
            return CardToPlay.of(strongest);

        if(opponentCard.isPresent()){
            if(opponentCard.get().relativeValue(vira) > strongest.relativeValue(vira))
                return CardToPlay.of(weakest);
            if(opponentCard.get().relativeValue(vira) < weakest.relativeValue(vira))
                return CardToPlay.of(weakest);
        }
        return CardToPlay.of(strongest);
    }

    private boolean isFirstRoundDrew(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        return roundResults.get(0) == GameIntel.RoundResult.DREW;
    }

    public CardToPlay firstRoundResponse(GameIntel intel){
        List<TrucoCard> myCards = sortHandByRelativeValue(intel.getCards(), intel.getVira());
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCards = intel.getOpponentCard();

        TrucoCard weakest = myCards.get(0);
        TrucoCard middle = myCards.get(1);
        TrucoCard strongest = myCards.get(2);

        if(opponentCards.isPresent()){
            if(opponentCards.get().relativeValue(vira) < weakest.relativeValue(vira))
                return CardToPlay.of(weakest);
            if(opponentCards.get().relativeValue(vira) < middle.relativeValue(vira))
                return CardToPlay.of(middle);
            if(opponentCards.get().relativeValue(vira) < strongest.relativeValue(vira))
                return CardToPlay.of(strongest);
        }
        return CardToPlay.of(weakest);
    }

    public CardToPlay lastRoundDiscard(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }

    private List<TrucoCard> sortHandByRelativeValue(List<TrucoCard> cards, TrucoCard vira) {
        List<TrucoCard> myCards = cards;
        myCards = myCards.stream()
                .sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .toList();
        return myCards;
    }



    private boolean containsTwoManilhasOrMore(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        
        int numberOfManilhas =(int) cards.stream().filter(c -> c.isManilha(vira)).count();

        return numberOfManilhas > 1;
    }

    private boolean containsOneManilha(GameIntel intel) {
        return intel.getCards().stream().filter(c -> c.isManilha(intel.getVira())).count() == 1;
    }

    private boolean containsThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(c -> c.relativeValue(intel.getVira()) == 10);
    }

    private boolean containsTwo(GameIntel intel) {
        return intel.getCards().stream().anyMatch(c -> c.relativeValue(intel.getVira()) == 9);
    }

    private boolean containsAce(GameIntel intel) {
        return intel.getCards().stream().anyMatch(c -> c.relativeValue(intel.getVira()) == 8);
    }

    private boolean containsZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(c -> c.isZap(intel.getVira()));
    }

    private boolean containsBiggestCouple(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        int containsBiggestCouple = (int) cards.stream().filter(c -> c.isZap(vira) || c.isCopas(vira)).count();

        return containsBiggestCouple == 2;
    }

}