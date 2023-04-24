/*
 *  Copyright (C) 2023 Ingrid Nery and Diego Pagotto
 *  Contact: ingrid <dot> nery <at> ifsp <dot> edu <dot> br
 *  Contact: diego <dot> pagotto <at> ifsp <dot> edu <dot> br
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
package com.indi.impl.addthenewsoul;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddTheNewSoul implements BotServiceProvider {
    private final List<CardRank> attackCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // verificaçao para nao causar exception de pedir 15
        if(intel.getHandPoints() == 12) return -1;

        // Se perder significar fim de jogo
        if(intel.getHandPoints() + intel.getOpponentScore() >= 12)
            if(hasCasal(intel)) return 1;
            else if (hasAttackCard(intel)) return 0;
            else return -1;

        // Se todas as manilhas ja foram jogadas e tem ataque, 6 marreco!
        if(allManilhasPlayed(intel) && hasAttackCard(intel)) return 0;


        // Se ta ganhando com folga, 6 marreco
        if(handIsStrong(intel) && intel.getScore() - intel.getOpponentScore() > 3) return 1;


        // se tiver casal, 6 marreco
        if(hasCasal(intel)) return 1;


        // Se ganhou a primeira ou a segunda rodada, e tiver manilha ou ataque, 6 marreco
        if(!intel.getRoundResults().isEmpty() && (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON || (intel.getRoundResults().size() >= 2 && intel.getRoundResults().get(1) == GameIntel.RoundResult.WON))){
            if(hasZap(intel) && hasAttackCard(intel)) return 1;
            if(hasManilha(intel) || hasAttackCard(intel)) return 0;
        }



        if(intel.getScore() - intel.getOpponentScore() > 2 && !handAboveAverage(intel)) return -1;

        if(!handIsStrong(intel)) return -1;

        if(intel.getScore() == 1 || intel.getOpponentScore() == 11) return -1;

        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if(card.isZap(intel.getVira()))
                return true;
        }
        if(intel.getOpponentScore() == 11)
            return true;
        if(intel.getOpponentScore() >=9){
            return handIsStrong(intel);
        }


        if(intel.getOpponentScore() <= 6)
            return true;
        if(handIsStrong(intel))
            return true;
        return handAboveAverage(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            if(handIsStrong(intel) || handAboveAverage(intel))
                return true;
        }



        if(intel.getOpponentCard().isPresent()){
            if(relativeValueBiggerCard(intel) < intel.getOpponentCard().get().relativeValue(intel.getVira()))
                return false;
        }
        if(intel.getOpponentScore() == 0)
            return true;
        if(intel.getScore() >= intel.getOpponentScore()+3){
            return true;
        }
        if(intel.getOpponentScore() >= 9){
            return false;
        }
        if(handAboveAverage(intel))
            return true;
        return handIsStrong(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        // Quem guarda ouro é pirata 🏴‍☠️
        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira()))
                return CardToPlay.of(card);
        }

        // Forca a primeira se tiver 2 cartas de ataque
        if(intel.getRoundResults().isEmpty()){
            TrucoCard smallestAttackCard = getSmallestAttackCard(intel);
            if(smallestAttackCard != null)
                return CardToPlay.of(smallestAttackCard);
        }

        // Se amarrar, deve jogar a mais forte
        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW)
            return CardToPlay.of(getBiggestCardOnHand(intel));


        // Se for possivel amarrar
        if(intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
            for (TrucoCard card : intel.getCards()) {
                if(card.getRank() == intel.getOpponentCard().get().getRank() && !card.isManilha(intel.getVira()))
                    return CardToPlay.of(card);
            }
        }


        TrucoCard smallestCardCapableOfWinning = chooseSmallestCardCapableOfWinning(intel);
        if(smallestCardCapableOfWinning == null)
            if(intel.getOpponentCard().isPresent())
                return intel.getRoundResults().isEmpty() ? CardToPlay.of(getSmallestCardOnHand(intel)) : CardToPlay.discard(getSmallestCardOnHand(intel));
            else
                return CardToPlay.of(getSmallestCardOnHand(intel));

        return CardToPlay.of(smallestCardCapableOfWinning);
    }

    private TrucoCard getBiggestCardOnHand(GameIntel intel) {
        return intel.getCards().stream().max(TrucoCard::relativeValue).get();
    }

    private TrucoCard getSmallestAttackCard(GameIntel intel){
        int countAttackCards = (int) intel.getCards().stream().filter(card -> attackCards.contains(card.getRank())).count();
        if(countAttackCards >= 2)
            return intel.getCards().stream().filter(card -> attackCards.contains(card.getRank())).min(TrucoCard::relativeValue).get();
        return null;
    }
    private TrucoCard chooseSmallestCardCapableOfWinning(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard smallestCardCapableOfWinning = null;
        TrucoCard vira = intel.getVira();

        if (opponentCard.isPresent()) {
            TrucoCard opponentCardValue = opponentCard.get();
            for (TrucoCard card : intel.getCards()) {
                if (card.relativeValue(vira) > opponentCardValue.relativeValue(vira)) {
                    if(smallestCardCapableOfWinning == null)
                        smallestCardCapableOfWinning = card;
                    else if(card.relativeValue(vira) < smallestCardCapableOfWinning.relativeValue(vira))
                        smallestCardCapableOfWinning = card;
                }
            }
        }

        return smallestCardCapableOfWinning;
    }

    private TrucoCard getSmallestCardOnHand(GameIntel intel) {
        TrucoCard smallestCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (smallestCard == null || card.relativeValue(intel.getVira()) < smallestCard.relativeValue(intel.getVira())) {
                smallestCard = card;
            }
        }
        return smallestCard;
    }
    private Boolean handIsStrong(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean isManilha = cards.stream()
                .anyMatch(card -> card.isManilha(vira));
        boolean hasHighRank = cards.stream()
                .filter(card -> !card.isManilha(vira))
                .anyMatch(card -> card.getRank().value() > 4);

        return isManilha && hasHighRank;
    }
    private Boolean handAboveAverage(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        int valueHand = 0;

        for (TrucoCard card : cards) {
            valueHand += card.relativeValue(vira);
        }
        return valueHand >= 18;

    }
    private int relativeValueBiggerCard(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        int biggerCard = 0;
        for (TrucoCard card : cards) {
            if(card.relativeValue(vira) > biggerCard)
                biggerCard = card.relativeValue(vira);
        }
        return biggerCard;

    }

    private boolean allManilhasPlayed(GameIntel intel) {
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                manilhas.add(card);
            }
        }
        return manilhas.size() == 4;
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    private boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    private boolean hasAttackCard(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> attackCards.contains(card.getRank()));
    }

    private boolean hasCasal(GameIntel intel) {
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                manilhas.add(card);
            }
        }
        return manilhas.size() == 2;
    }
}
