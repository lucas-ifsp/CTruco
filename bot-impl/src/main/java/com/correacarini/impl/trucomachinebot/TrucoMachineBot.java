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
            CardToPlay.of(intel.getCards().get(0));
        }

        return CardToPlay.of(greatestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
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
}
