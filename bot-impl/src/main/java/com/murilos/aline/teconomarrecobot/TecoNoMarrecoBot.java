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
package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;

public class TecoNoMarrecoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        if(hasCasalMaior(intel)){
            return true;
        }
        if(hasHandOfThree(intel) == 3 || hasHandOfThree(intel) == 2){
            return true;
        }
        if(manilhaCount(cards, cardVira) == 2){
            return true;
        }
        if(manilhaCount(cards, cardVira) == 1 && valueOfTheHand(intel) >= 28){
            return true;
        }
        if(manilhaCount(cards, cardVira) == 1 && hasHandOfThree(intel) == 1){
            return true;
        }
        if(intel.getOpponentScore() < 4 && !(valueOfTheHand(intel) < 18)){
            return true;
        }
        if(intel.getOpponentScore() == 11 && intel.getScore() == 11){
            return true;
        }
        return false;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        if((intel.getRoundResults().size() >= 1) && intel.getRoundResults().get(0) != GameIntel.RoundResult.DREW && valueOfTheHand(intel) >= 15){
            return true;
        }if((intel.getRoundResults().size() == 2) &&  valueOfTheHand(intel) >= 10){
            return true;
        }if((intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && valueOfTheHand(intel) >= 10)){
            return true;
        }if((manilhaCount(cards, intel.getVira()) > 0) && (intel.getRoundResults().size() > 0) && intel.getRoundResults().get(0).equals(DREW)){
            if(strongManilha(intel).relativeValue(intel.getVira()) > 11){
                return true;
            }
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        List<TrucoCard> cards = intel.getCards();
        Integer manilhas = manilhaCount(cards,intel.getVira());

        // responde a jogadas do oponente
        if (intel.getOpponentCard().isPresent()) {

            //tenta amarrar se tiver zap seco
            if (manilhas == 1 && hasZap(intel)){
                if(intel.getRoundResults().isEmpty()){
                    for (TrucoCard card : intel.getCards()) {
                        if(card.getRank() == intel.getOpponentCard().get().getRank() && !card.isManilha(intel.getVira()))
                            return CardToPlay.of(card);
                    }
                }

            }
            //tenta matar manilha se não der joga mais fraca
            if (intel.getOpponentCard().get().isManilha(intel.getVira())){
                return CardToPlay.of(killOpponentManilha(intel));
            }
            // tenta matar se não for manilha
            return CardToPlay.of(killCard(intel));
        }

        // joga se tiver manilha
        if (manilhas>0){
            // descarta o cagão
            for (TrucoCard card : intel.getCards()) {
                if (card.isOuros(intel.getVira()))
                    return CardToPlay.of(card);
            }
            // se tiver amarrado joga maior manilha
            if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
                return CardToPlay.of(strongManilha(intel));
            }
            //se tiver casal maior joga nada na primeira
            if (hasCasalMaior(intel) && intel.getRoundResults().isEmpty()){
                return CardToPlay.of(weakestCard(intel));
            }
        }
        // joga a carta mais forte da mão
        return CardToPlay.of(strongCard(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        Integer manilhas =  manilhaCount(intel.getCards(),intel.getVira());

        // seis rato!
        if (hasCasalMaior(intel))return 1;
        if (!intel.getRoundResults().isEmpty() && hasZap(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 1;
        if (!intel.getRoundResults().isEmpty() && hasZap(intel) && intel.getRoundResults().get(0) == DREW)return 1;
        if (manilhas > 0 && strongManilha(intel).relativeValue(intel.getVira()) >= 11) return 1;

        // desce que eu quero ver!
        if (!intel.getRoundResults().isEmpty() && hasHandOfThree(intel) >= 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 0;
        if (manilhas > 0 && hasHandOfThree(intel) >= 1) return 0;
        if (manilhas >= 1) return 0;

        // volta pro monte..
        return -1;
    }

    private boolean hasCasalMaior(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isZap(cardVira) || card.isCopas(cardVira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private int hasHandOfThree(GameIntel intel){
        Integer contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.getRank().value() == 10){
                contador += 1;
            }
        }
        return contador;
    }

    private int manilhaCount(List<TrucoCard> cards, TrucoCard vira){
        int manilhaCount = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                manilhaCount++;
            }
        }
        return manilhaCount;
    }

    private int valueOfTheHand(GameIntel intel){
        Integer hand = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isManilha(intel.getVira())){
                hand += card.relativeValue(intel.getVira());
            }else{
                hand += card.getRank().value();
            }
        }
        return hand;
    }

    private TrucoCard strongCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        Integer maior = 0;
        Integer indexMaior = -1;
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() > maior) {
                maior = card.getRank().value();
                indexMaior++;
            }
        }
        return cards.get(indexMaior);
    }

    private TrucoCard weakestCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        Integer menor = Integer.MAX_VALUE;;
        TrucoCard cardPlay = null;

        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira()) ){
                if (card.relativeValue(intel.getVira()) < menor){
                    menor = card.relativeValue(intel.getVira());
                    cardPlay = card;
                }
            }else {
                if (card.getRank().value() < menor) {
                    menor = card.getRank().value();
                    cardPlay = card;
                }
            }
        }
        return cardPlay;

    }
    private TrucoCard killCard(GameIntel intel){
        TrucoCard cardPlay = null;
        List<TrucoCard> strongerCards = strongestCardsInTheHand(intel);
        if (!strongerCards.isEmpty()){
            cardPlay = strongerCards.get(0);
            for (TrucoCard card : strongerCards){
                if (card.getRank().value() < cardPlay.getRank().value()){
                    cardPlay=card;
                }
            }
        }else{
            cardPlay = weakestCard(intel);
        }
        return cardPlay;
    }
    private List<TrucoCard> strongestCardsInTheHand(GameIntel intel){
        List<TrucoCard> strongerCards = new ArrayList<>();
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() > intel.getOpponentCard().get().getRank().value()) {
                strongerCards.add(card);
            }
        }
        return strongerCards;
    }

    private TrucoCard strongManilha(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        Integer maior = 0;
        Integer indexMaior = -1;
        for (TrucoCard card : intel.getCards()) {
            if (card.relativeValue(intel.getVira()) > maior) {
                maior = card.getRank().value();
                indexMaior++;
            }
        }
        return cards.get(indexMaior);
    }
    private TrucoCard killOpponentManilha(GameIntel intel) {
        List<TrucoCard> manilhas = manilhasInTheHandLargerThanTheOpponents(intel);
        TrucoCard cardPlay = weakestCard(intel);
        if (!manilhas.isEmpty()) {
            cardPlay = manilhas.get(0);
            for (TrucoCard manilha : manilhas) {
                    if (manilha.relativeValue(intel.getVira()) < cardPlay.relativeValue(intel.getVira())){
                        cardPlay = manilha;
                    }
            }
        }
        return cardPlay;
    }

    private List<TrucoCard> manilhasInTheHandLargerThanTheOpponents(GameIntel intel){
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                if (card.relativeValue(intel.getVira()) > intel.getOpponentCard().get().relativeValue(intel.getVira())){
                    manilhas.add(card);
                }
            }
        }
        return manilhas;
    }

    private Boolean hasZap(GameIntel intel){
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira()))
                return true;
        }
        return false;
    }







}
