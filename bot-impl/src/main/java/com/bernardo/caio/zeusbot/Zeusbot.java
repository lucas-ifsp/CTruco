/*
 *  Copyright (C) 2024 Caio V. Soares and Bernardo F. Filho
 *  Contact: caio <dot> vitor <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: bernardo <dot> f <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.bernardo.caio.zeusbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Objects;

public class Zeusbot implements BotServiceProvider {
    private List<TrucoCard> cards;
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard midCard;
    private TrucoCard worstCard;
    private enum Round{FIRSTROUND, SECONDROUND, THIRDROUND}
    private Round currentStrategy;
    private boolean startedFirstRound;



    private void setCards(GameIntel intel){
        this.cards = Objects.requireNonNull(intel.getCards());
    }
    private void setVira(GameIntel intel){
        this.vira =  Objects.requireNonNull(intel.getVira());
    }


    private void rankCards(GameIntel intel){
        setCards(intel);
        setVira(intel);

        if(cards.isEmpty()){
            bestCard = TrucoCard.closed();
            midCard = TrucoCard.closed();
            worstCard = TrucoCard.closed();
        }
        else if(cards.size() == 1){
            bestCard = cards.get(0);
        }
        else{
            bestCard = cards.stream()
                    .max((card1, card2) -> Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira)))
                    .orElse(null);

            worstCard = cards.stream()
                    .max((card1, card2) -> Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira)))
                    .orElse(null);

            if (cards.size() == 3){

                midCard = cards.stream()
                        .filter(card -> !card.equals(bestCard) && !card.equals(worstCard))
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    private void chooseStrategy(GameIntel intel){
        int currentRound = intel.getRoundResults().size();

        if(currentRound == 0) this.currentStrategy = Round.FIRSTROUND;
        else if(currentRound == 1) this.currentStrategy = Round.SECONDROUND;
        else this.currentStrategy = Round.THIRDROUND;
    }

    private CardToPlay firstRoundChooseCard(GameIntel intel){
        if(intel.getOpponentCard().isEmpty()){
            startedFirstRound = true;
            if(bestCard.relativeValue(vira) >= 11) return CardToPlay.of(bestCard);
            else return CardToPlay.of(midCard);
        }
        else{
            startedFirstRound = false;
            if(intel.getOpponentCard().get().relativeValue(vira) < worstCard.relativeValue(vira)) //adicionar um = para teste
                return CardToPlay.of(worstCard);
            else if(intel.getOpponentCard().get().relativeValue(vira) < midCard.relativeValue(vira))
                return CardToPlay.of(midCard);
            else if(intel.getOpponentCard().get().relativeValue(vira) < bestCard.relativeValue(vira) && bestCard.relativeValue(vira) <= 10)
                return CardToPlay.of(bestCard);
            else if (intel.getOpponentCard().get().relativeValue(vira) < bestCard.relativeValue(vira) && bestCard.relativeValue(vira) > 10
                    && midCard.relativeValue(vira) == intel.getOpponentCard().get().relativeValue(vira)) {
                return CardToPlay.of(midCard);
            } else return CardToPlay.of(worstCard);
        }
    }

    private CardToPlay secondRoundChooseCard(GameIntel intel){
        if(intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            return CardToPlay.of(worstCard);
        }
        else if(intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            return CardToPlay.of(bestCard);
        } else{
            if(worstCard.relativeValue(vira) > intel.getOpponentCard().get().relativeValue(vira))
                return CardToPlay.of(worstCard);
            else
                return CardToPlay.of(bestCard);
        }
    }

    private CardToPlay thirdRoundChooseCard(GameIntel intel){
        return CardToPlay.of(bestCard);
    }

    private boolean firstRoundDecideIfRaises(GameIntel intel){
        if(intel.getOpponentCard().isPresent()){
            startedFirstRound = false;

            if(worstCard.relativeValue(vira) > intel.getOpponentCard().get().relativeValue(vira) || midCard.relativeValue(vira) > intel.getOpponentCard().get().relativeValue(vira)){
                return bestCard.relativeValue(vira) >= 8;
            }
            else
                return bestCard.relativeValue(vira) > 10 && midCard.relativeValue(vira) >= 9;
        }
        else {
            startedFirstRound = true;
            return bestCard.relativeValue(vira) > 10 && midCard.relativeValue(vira) >= 9;
        }
    }

    private boolean secondRoundDecideIfRaises(GameIntel intel){
        if(startedFirstRound){
            if(intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)
                return bestCard.relativeValue(vira) >= 11;
            else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) {
                return bestCard.relativeValue(vira) >= 9;
            }
            else
                return bestCard.relativeValue(vira) >= 11 && midCard.relativeValue(vira) >= 9;
        }
        else{
            if(intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)
                return bestCard.relativeValue(vira) >= 11;
            else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW)
                return bestCard.relativeValue(vira) >= 9;
            else return false;

        }
    }

    private boolean thirdRoundDecideIfRaises(GameIntel intel){
        if(intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST){
            return intel.getOpponentCard().get().relativeValue(vira) <= 8;
        } else if (intel.getRoundResults().get(1) == GameIntel.RoundResult.WON) {
            return true;
        } else {
            return bestCard.relativeValue(vira) >= 7;
        }
    }

    public int firstRoundGetRaiseResponse(GameIntel intel){
        if(startedFirstRound){
            if(bestCard.relativeValue(vira) > 9 && midCard.relativeValue(vira) >= 9){
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else if(bestCard.relativeValue(vira) >= 8 && midCard.relativeValue(vira) >= 7)
                return 0;

            else return -1;
        }
        else{
            if(bestCard.relativeValue(vira) > 11 && worstCard.relativeValue(vira) >= 9){
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else if(bestCard.relativeValue(vira) >= 9 && worstCard.relativeValue(vira) >= 9)
                return 0;

            else return -1;
        }
    }

    public int secondRoundGetRaiseResponse(GameIntel intel){
        if(intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            if(bestCard.relativeValue(vira) >= 6)
                return 0;
            else if (bestCard.relativeValue(vira) > 12) return 1;

            else if (bestCard.relativeValue(vira) > 11) {
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else return -1;

        }
        else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) {
            if(bestCard.relativeValue(vira) >= 8)
                return 0;
            else if (bestCard.relativeValue(vira) >= 12){
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else
                return -1;
        }
        else{
            if(bestCard.relativeValue(vira) >= 9 && worstCard.relativeValue(vira) >= 7)
                return 0;
            else if(bestCard.relativeValue(vira) >= 11 && worstCard.relativeValue(vira) >= 9){
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else
                return -1;
        }
    }

    public int thirdRoundGetRaiseResponse(GameIntel intel){
        if(intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST){
            if(bestCard.relativeValue(vira) >= 8)
                return 0;
            else if (bestCard.relativeValue(vira) > 12) return 1;

            else if (bestCard.relativeValue(vira) > 10) {
                if (intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else
                return -1;
        }
        else if (intel.getRoundResults().get(1) == GameIntel.RoundResult.DREW) {
            if(bestCard.relativeValue(vira) >= 5)
                return 0;
            else if (bestCard.relativeValue(vira) >= 9) {
                if(intel.getHandPoints() == 1) return 1;
                else return 0;

            } else return -1;
        }
        else{
            if(bestCard.relativeValue(vira) >= 8)
                return 0;
            else if (bestCard.relativeValue(vira) > 12) return 1;

            else if (bestCard.relativeValue(vira) > 10){
                if(intel.getHandPoints() == 1) return 1;
                else return 0;
            }
            else
                return -1;
        }
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        rankCards(intel);

        if(intel.getOpponentScore() == 11) return true;
        else if(intel.getOpponentScore() > 8) {

            return bestCard.relativeValue(vira) >= 9 && midCard.relativeValue(vira) >= 8;
        }
        else if(intel.getOpponentScore() == 8){
            return bestCard.relativeValue(vira) > 6 && midCard.relativeValue(vira) > 6;
        }
        else{
            return (bestCard.relativeValue(vira) >= 9 && midCard.relativeValue(vira) >= 8) ||
                    ( bestCard.relativeValue(vira) >= 8 && midCard.relativeValue(vira) >= 6 && bestCard.relativeValue(vira) >= 6);
        }
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        rankCards(intel);
        chooseStrategy(intel);

        if(currentStrategy == Round.FIRSTROUND)
            return firstRoundDecideIfRaises(intel);
        else if (currentStrategy == Round.SECONDROUND)
            return secondRoundDecideIfRaises(intel);
        else
            return thirdRoundDecideIfRaises(intel);


    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        rankCards(intel);
        chooseStrategy(intel);

        if(currentStrategy == Round.FIRSTROUND){
            return firstRoundChooseCard(intel);
        }
        else if(currentStrategy == Round.SECONDROUND){
            return secondRoundChooseCard(intel);
        }else
            return thirdRoundChooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        rankCards(intel);
        chooseStrategy(intel);

        if(currentStrategy == Round.FIRSTROUND){
            return firstRoundGetRaiseResponse(intel);
        }
        else if(currentStrategy == Round.SECONDROUND){
            return secondRoundGetRaiseResponse(intel);
        }else
            return thirdRoundGetRaiseResponse(intel);
    }

}




