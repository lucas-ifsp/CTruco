package com.yuri.impl;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class BotMadeInDescalvado implements BotServiceProvider {

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
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = intel.getRoundResults().size() + 1;

        return switch (round) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> throw new RuntimeException("Invalid round");
        };
    }

    private CardToPlay chooseCardFirstRound(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));

        boolean playsFirst = intel.getOpponentCard().isEmpty();

        if (playsFirst) {
            int manilhaCount = 0;

            for (var card : intel.getCards()) {
                if (card.isManilha(intel.getVira())) {
                    manilhaCount += 1;
                }
            }

            if (manilhaCount >= 2) {
                return CardToPlay.discard(intel.getCards().get(2));
            } else {
                return CardToPlay.of(intel.getCards().get(0));
            }
        } else {
            return CardToPlay.of(intel.getCards().get(0));
        }
    }

    private CardToPlay chooseCardSecondRound(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    private CardToPlay chooseCardThirdRound(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }
}
