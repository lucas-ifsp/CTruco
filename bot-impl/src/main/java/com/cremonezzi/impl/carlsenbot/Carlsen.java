package com.cremonezzi.impl.carlsenbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

/* Notes
*
* CARDS RANK ORDER
*  4 - 5 - 6 - 7 - Q - J - K - A - 2 - 3
*
*  40 Cards in the deck (each rank has all the 4 suits => 10 ranks x 4 suits)
*
*  CARDS SUIT ORDER
*  Diamond - Spade - Heart - Club
*
* */

public class Carlsen implements BotServiceProvider {
    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int haveZap = haveZap(intel.getCards(), vira);

        return haveZap > -1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getOpponentCard().isEmpty()) {
            return CardToPlay.of(lowerInHand(intel.getCards(), intel.getVira()));
        }

        TrucoCard opponentCard = intel.getOpponentCard().get();
        if (opponentCard.isZap(intel.getVira())) {
            if (intel.getRoundResults().isEmpty()) {
                return CardToPlay.of(lowerInHand(intel.getCards(), intel.getVira()));
            }

            return CardToPlay.discard(lowerInHand(intel.getCards(), intel.getVira()));
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public String getName() {
        return "Trucus Carlsen";
    }

    private int haveZap(List<TrucoCard> botCards, TrucoCard vira) {
        Optional<TrucoCard> haveZap = botCards.stream().filter(trucoCard -> trucoCard.isZap(vira)).findFirst();

        return haveZap.map(botCards::indexOf).orElse(-1);
    }

    private TrucoCard lowerInHand(List<TrucoCard> botCards, TrucoCard vira) {
        TrucoCard lower = botCards.get(0);

        for (TrucoCard trucoCard : botCards) {
            if (trucoCard.relativeValue(vira) < lower.relativeValue(vira)) {
                lower = trucoCard;
            }
        }

        return lower;
    }
}
