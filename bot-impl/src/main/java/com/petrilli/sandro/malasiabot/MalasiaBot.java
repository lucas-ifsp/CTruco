package com.petrilli.sandro.malasiabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MalasiaBot implements BotServiceProvider {

    private boolean podeMatar(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard opponentCardValue = opponentCard.get();
        TrucoCard vira = intel.getVira();

        List<Integer> List = null;

        for (TrucoCard card : intel.getCards()) {
            int value = card.compareValueTo(opponentCardValue, vira);
            List.add(value);
        }

        for (int i: List) {
            if (i > 0) {
                return true;
            }

        }

        return false;
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        return null;
    }
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }
}
