/*
 *  Copyright (C) 2023 Lucas Pereira dos Santos and Felipe Santos Lourenço
 *  Contact: lucas <dot> pereira3 <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: santos <dot> lourenco <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SecondRoundStrategy implements BotServiceProvider {
    private List<TrucoCard> roundCards;
    private List<TrucoCard> ordendedCards; // ascending order
    private TrucoCard vira;
    private DefaultFunctions defaultFunctions;

    public SecondRoundStrategy() {}

    private void setCards(GameIntel intel){
        this.vira = intel.getVira();
        this.roundCards = intel.getCards();
        this.defaultFunctions = new DefaultFunctions(roundCards, vira);
        this.ordendedCards = defaultFunctions.sortCards(roundCards);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (hasJustManilhas(ordendedCards)) return 1;
        if (defaultFunctions.isPowerfull(ordendedCards)) return 0;

        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            if (opponentCard.isPresent()){
                int index = indexOfCardThatCanWin(ordendedCards, opponentCard);
                if (index != -1){
                    if (defaultFunctions.isPowerfull(ordendedCards)) return 1;
                    if (defaultFunctions.isMedium(ordendedCards)) return 0;
                } else {
                    if (defaultFunctions.isPowerfull(ordendedCards)) return 0;
                }
            } else {
                if (defaultFunctions.isPowerfull(ordendedCards)) return 1;
                Random random = new Random();
                int numero = random.nextInt(100);
                if (numero > 0 && numero <= 30) {
                    if (defaultFunctions.isMedium(ordendedCards)) return 0;
                }
            }
        } else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            if (opponentCard.isPresent()){
                int index = indexOfCardThatCanWin(ordendedCards, opponentCard);
                if (index != -1){
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (ordendedCards.get(0).isManilha(vira)) return 0;
            }
        } else {
            if (opponentCard.isPresent()){
                int index = indexOfCardThatCanWin(ordendedCards, opponentCard);
                if (index != -1) {
                    if (defaultFunctions.isPowerfull(ordendedCards)) return 0;
                } else {
                    return -1;
                }
            } else {
                if (defaultFunctions.isPowerfull(ordendedCards)) return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        setCards(intel);
        return defaultFunctions.maoDeOnzeResponse(ordendedCards, intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        setCards(intel);
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (hasJustManilhas(ordendedCards)) return true;
        // Se ganhamos ou empatamos a primeira
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON || intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            if (opponentCard.isPresent()){
                int index = indexOfCardThatCanWin(ordendedCards, opponentCard);
                return index >= 0; // conseguimos ganhar a segunda se for >= 0
            }
            // Se o oponente ainda não jogou e temos carta maior que 3
            return hasThreeOrBetter(ordendedCards);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            if (opponentCard.isPresent()) {
                return lowestCardToWinOrDiscard(ordendedCards, opponentCard);
            }
            return CardToPlay.of(ordendedCards.get(0)); // Guarda maior carta para o terceiro round

        } else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            return CardToPlay.of(ordendedCards.get(1));

        } else{
            if (opponentCard.isPresent()){
                return lowestCardToWinOrDiscard(ordendedCards, opponentCard);
            }
            // se o oponente não jogou, jogamos nossa carta mais forte
            return CardToPlay.of(ordendedCards.get(1));
        }
    }

    private CardToPlay lowestCardToWinOrDiscard(List<TrucoCard> ordendedCards, Optional<TrucoCard> opponentCard){
        if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(0));
        } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(1));
        }
        return CardToPlay.discard(ordendedCards.get(0));
    }

    private int indexOfCardThatCanWin(List<TrucoCard> ordendedCards, Optional<TrucoCard> opponentCard){
        if (ordendedCards.size() == 2){
            if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                return 0;
            } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                return 1;
            }
        } else if (ordendedCards.size() == 1){
            if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                return 0;
            }
        }
        return -1;
    }


    private boolean hasThreeOrBetter(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 2) return ordendedCards.get(0).relativeValue(vira) >= 9 && ordendedCards.get(1).relativeValue(vira) >= 9;
        else return ordendedCards.get(0).relativeValue(vira) >= 9;
    }

    private boolean hasJustManilhas(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 2) return ordendedCards.get(1).isManilha(vira) && ordendedCards.get(0).isManilha(vira);
        else return ordendedCards.get(0).isManilha(vira);
    }
}
