package com.gatti.casaque.caipirasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CaipirasBot implements BotServiceProvider {
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
        if(checkExistenceDiamondManilha(intel.getCards(),intel.getVira())){
            return CardToPlay.of(chooseDiamondInFirstRound(intel.getCards(),intel.getVira()));
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public boolean checkExistenceDiamondManilha(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.isOuros(vira)) {
                return true;
            }
        }
        return false;
    }

    public TrucoCard chooseDiamondInFirstRound(List<TrucoCard> cards, TrucoCard vira){
        for (TrucoCard card : cards) {
            if (card.isOuros(vira)) {
                return card;
            }
        }
        return null;
    }
}