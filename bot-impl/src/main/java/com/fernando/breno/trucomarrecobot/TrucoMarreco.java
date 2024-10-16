/*
 *  Copyright (C) 2024 Breno Augusto de Oliveira - IFSP/SCL
 *  Copyright (C) 2024 Fernando Candido Rodrigues - IFSP/SCL
 *  Contact: Breno <dot> Oliveira <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: Fernando <dot> Rodrigues <at> aluno <dot> ifsp <dot> edu <dot> br
 *
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

package com.fernando.breno.trucomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;

public class TrucoMarreco implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
       if( numberOfManilhas(intel) >= 2){
          return  true;
       }

       if(biggestCouple(intel)){
            return  true;
       }

        if(handStrong(intel)){
            return true;
        }

        if(intel.getOpponentScore() == 11 && intel.getScore() == 11){
            return true;
        }

        if(intel.getOpponentScore() <= 10 && (evaluateHandStrength(intel) >= 21)){
            return true;
        }
        if(countTheeHand(intel)){
           return true;
        }


       return  false;
    }


    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if( biggestCouple(intel)){
            return true;
        }
        if(wonFirstRound(intel) && hasZap(intel)){
            return  true;
        }
        if(evaluateHandStrength(intel) > 15 && hasZap(intel)) {
            return  true;
        }
        if(numberOfManilhas(intel) > 0 && evaluateHandStrength(intel) >= 10){
            return  true;
        }

        if(handStrong(intel)){
            return  true;
        }


        return  false;
    }


    @Override
    public CardToPlay chooseCard (GameIntel intel) {

        if (intel.getOpponentCard().get().isManilha(intel.getVira())){
            return CardToPlay.of(weakCard(intel).orElse(null));
        }

        if (biggestCouple(intel) && intel.getRoundResults().isEmpty()) {
            return CardToPlay.of(weakCard(intel).orElse(null));
        }
        if (intel.getRoundResults().equals(DREW) || wonFirstRound(intel)) {
            return CardToPlay.of(strongCard(intel).orElse(null));
        }



        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (biggestCouple(intel) ) return 1;

        if(handStrong(intel)) {
            return 1;
        }

        if (wonFirstRound(intel) && numberOfManilhas(intel) >= 1) {
            return 1; }

        if (numberOfManilhas(intel) < 1 && evaluateHandStrength(intel) > 15) {
            return 0;
        }


        if(hasZap(intel)) {
            return 1;
        }

        if ((hasZap(intel)) && strongCard(intel).isPresent()) {
            return 1;
        }

        if (countTheeHand(intel)){
            return 1;
        }

        if (intel.getRoundResults().contains(GameIntel.RoundResult.DREW) && hasZap(intel)) {
            return 1;
        }





        return 0;
    }



    public boolean wonFirstRound(GameIntel intel){

        return intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON);
    }


    public boolean handStrong(GameIntel intel){
        var hasManilha = numberOfManilhas(intel)  >= 1;
        var containsThree = intel.getCards().contains(3);

        return hasManilha && containsThree;
    }
    public boolean countTheeHand(GameIntel intel) {
        var countThree = intel.getCards().stream()
                .filter(card -> card.getRank().value() == 3)
                .count();


        return countThree >= 3;
    }


    public boolean biggestCouple(GameIntel intel){
        var hasZap = intel.getCards().stream().filter(card -> card.isZap(intel.getVira())).count() > 0;
        var hasCopas = intel.getCards().stream().filter(card -> card.isCopas(intel.getVira())).count() > 0;
        return hasZap && hasCopas;
    }

    public Optional<TrucoCard> weakCard(GameIntel intel) {
        return intel.getCards().stream().min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }

    public Optional<TrucoCard> strongCard(GameIntel intel) {
        return intel.getCards().stream().max((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }




    public Boolean hasZap(GameIntel intel){
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));

    }

    public long numberOfManilhas(GameIntel intel) {
        return intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }


    private int evaluateHandStrength(GameIntel intel) {
        int strength = 0;
        TrucoCard vira = intel.getVira();
        for (TrucoCard card : intel.getCards()) {

            strength += card.relativeValue(vira);
        }


      return  strength;
    }



    @Override
    public String getName() { return "Truco Marreco!"; }
}