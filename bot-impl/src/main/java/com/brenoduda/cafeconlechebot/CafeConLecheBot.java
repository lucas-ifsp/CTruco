/*
 *  Copyright (C) 2023 Breno Nascimento Lopes, Maria Eduarda Santos - IFSP/SCL
 *  Contact: breno <dot> lopes <at> aluno <dot> ifsp <dot> edu <dot> br, santos <dot> maria2 <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.brenoduda.cafeconlechebot;

import com.bueno.spi.model.*;

import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class CafeConLecheBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        if(intel.getCards().stream().filter(card -> card.isManilha(vira)).count() >= 2){
            return false;
        }

        if(intel.getCards().stream().filter(card -> card.isManilha(vira)).count() == 1 &&
                intel.getCards().stream().anyMatch(card -> card.getRank().equals(CardRank.THREE))
        ){
            return false;
        }

        return true;

    }

    private boolean hasStrongCards(GameIntel intel){
        int counter=
                howManyEquals(intel,CardRank.THREE )+
                        howManyEquals(intel,CardRank.TWO)+
                        howManyEquals(intel,CardRank.ACE)+
                        howManyEquals(intel,intel.getVira().getRank());
        if(counter>=2){
            return true;
        }
        return false;
    }

    private int howManyEquals(GameIntel intel, CardRank rank){
        return (int )intel.getCards().stream().filter(card -> card.getRank().equals(rank)).count();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(botCards.stream().filter(card -> card.isManilha(vira)).toList().size() == 3) {
            return true;
        }

        if(botCards.stream().filter(card -> card.getSuit().equals(CardSuit.HEARTS)).toList().size() == 1
        && botCards.stream().filter(card -> card.getSuit().equals(CardSuit.CLUBS)).toList().size() == 1) {
            return true;
        }

        if(botCards.stream().filter(card -> card.isManilha(vira)).toList().size() == 2 &&
                botCards.stream().filter(card -> card.getRank().equals(CardRank.THREE)).toList().size() == 1) {
            return true;
        }

        if(botCards.stream().filter(card -> card.getRank().equals(CardRank.THREE)).toList().size() >= 2 &&
                botCards.stream().filter(card -> card.getSuit().equals(CardSuit.CLUBS) || card.getSuit().equals(CardSuit.HEARTS)).toList().size() >= 1) {
            return true;
        }

        if(botCards.stream().filter(card -> card.getSuit().equals(CardSuit.CLUBS) || card.getSuit().equals(CardSuit.HEARTS)).toList().size() >= 1 &&
                intel.getRoundResults().size() > 0) {
            if (intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();

        if(intel.getOpponentCard().isEmpty() &&
                intel.getRoundResults().isEmpty() &&
                botCards.stream().filter(card -> card.getSuit().equals(CardSuit.DIAMONDS)).toList().size() >= 1) {
            return CardToPlay.of(botCards.stream().filter(card -> card.getSuit().equals(CardSuit.DIAMONDS)).findFirst().get());
        }

        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(botCards.stream().filter(card -> card.isManilha(vira)).toList().size() == 1 &&
                botCards.stream().filter(card -> card.getRank().equals(CardRank.THREE)).toList().size() == 0) {
            return -1;
        }

        return 0;
    }
}
