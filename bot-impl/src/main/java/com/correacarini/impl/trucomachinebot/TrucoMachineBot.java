package com.correacarini.impl.trucomachinebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class TrucoMachineBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira  = intel.getVira();

        TrucoCard greatestCard = getGreatestCard(cards, vira);

        if(intel.getOpponentCard().isPresent()){
            TrucoCard minimalGreaterCard = getMinimalGreaterCard(cards, vira, intel.getOpponentCard().get());
            if(minimalGreaterCard == null) {
                if (greatestCard.relativeValue(vira) == intel.getOpponentCard().get().relativeValue((vira))){
                    return CardToPlay.of(greatestCard);
                }

                TrucoCard lowestCard = getLowestCard(cards, vira);
                return CardToPlay.of(lowestCard);
            }
            return CardToPlay.of(minimalGreaterCard);
        }

        return CardToPlay.of(greatestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "Truco Machine";
    }

    private TrucoCard getGreatestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard greatestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(greatestCard, vira);
            if (comparison > 0) {
                greatestCard = card;
            }
        }

        return greatestCard;
    }

    private TrucoCard getMinimalGreaterCard( List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        TrucoCard minimalGreaterCard = null;

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(opponentCard, vira);
            if (comparison > 0) {
                if (minimalGreaterCard == null || card.compareValueTo(minimalGreaterCard, vira) < 0) {
                    minimalGreaterCard = card;
                }
            }
        }

        return minimalGreaterCard;
    }

    private TrucoCard getLowestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard lowestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(lowestCard, vira);
            if (comparison < 0) {
                lowestCard = card;
            }
        }

        return lowestCard;
    }
}
