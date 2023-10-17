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

// Authors: Juan Rossi e Guilherme Lopes

package com.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

import static com.rossi.lopes.trucoguru.TrucoGuruUtils.*;

public class TrucoGuru implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        final TrucoCard vira = intel.getVira();
        final List<TrucoCard> cards = intel.getCards();

        Boolean hasCasalMaior = TrucoGuruUtils.hasCasalMaior(cards, vira);
        if(hasCasalMaior) return true;
        Boolean hasCasalMenor = TrucoGuruUtils.hasCasalMenor(cards, vira);
        if(hasCasalMenor) return true;

        int opponentScore = intel.getOpponentScore();
        int botScore = intel.getScore();
        Boolean hasDoubleManilhas = TrucoGuruUtils.hasDoubleManilhas(cards, vira);
        if(botScore - opponentScore <= 5) return hasDoubleManilhas;

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        final Boolean isFirstRound = roundResults.size() == 0;
        if (isFirstRound) return false;

        final Boolean isMaoDeOnze = intel.getScore() == 11 || intel.getOpponentScore() == 11;
        if (isMaoDeOnze) return false;

        final TrucoCard vira = intel.getVira();
        final List<TrucoCard> cards = intel.getCards();

        final Boolean hasCasalMaior = TrucoGuruUtils.hasCasalMaior(cards, vira);
        final Boolean isSecondRound = roundResults.size() == 1;
        if (hasCasalMaior && isSecondRound) return true;

        final GameIntel.RoundResult lastRound = roundResults.get(roundResults.size() - 1);
        final Boolean hasWonLastRound = lastRound == GameIntel.RoundResult.WON;
        final Boolean hasStrongCard = TrucoGuruUtils.hasStrongCard(cards, vira);
        if (hasWonLastRound && hasStrongCard) return true;

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel)  {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        //cobre os casos de de mao de onze e caso esteja chamando no doze (n temos medo de nada)
        if(intel.getOpponentScore() >= 11) return 0;
        if(intel.getHandPoints() == 12) return 0;

        int roundNumber = intel.getRoundResults().size() + 1;
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (roundNumber == 1) {
            //casal maior e menor chama no seis froxo
            Boolean hasCasalMaior = TrucoGuruUtils.hasCasalMaior(intel.getCards(), intel.getVira());
            if(hasCasalMaior) return 1;
            Boolean hasCasalMenor = TrucoGuruUtils.hasCasalMenor(intel.getCards(), intel.getVira());
            if(hasCasalMenor) return 1;

            if (hasStrongHand(cards, vira)) {
                // Mão forte, pode chamar no 6
                return 1;
            }
        } else if (roundNumber == 2) {
            // Se for a segunda rodada, avalie a situação após a primeira rodada
            final List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
            GameIntel.RoundResult firstRoundResult = roundResults.get(0);
            final GameIntel.RoundResult lastRoundResult = roundResults.get(roundResults.size() - 1);

            if (firstRoundResult == GameIntel.RoundResult.WON || lastRoundResult == GameIntel.RoundResult.WON) {
                if (hasZap(cards, vira) || hasCopas(cards, vira)) {
                    // Tem zap, pode chamar no 6
                    return 1;
                } else if (hasManilha(cards, vira) || hasStrongCard(cards, vira)) {
                    // Tem manilha ou carta forte, aceita
                    return 0;
                }
            }
        }


        return -1;
    }
}
