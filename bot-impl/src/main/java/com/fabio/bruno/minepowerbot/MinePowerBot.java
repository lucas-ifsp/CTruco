package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class MinePowerBot implements BotServiceProvider {
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
        int roundNumber = getRoundNumber(intel);
        if (!isTheFirstToPlay(intel)) {
            switch(roundNumber) {
                case 1 -> {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    TrucoCard vira = intel.getVira();
                    var lowestCardStrongerThanOpponentCard =  intel.getCards().stream()
                            .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                            .min( (card1, card2) -> card1.compareValueTo(card2, vira));
                    if (lowestCardStrongerThanOpponentCard.isPresent())
                        return CardToPlay.of(lowestCardStrongerThanOpponentCard.get());
                }
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    private List<TrucoCard> getManilha(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .toList();
    }

    private boolean isTheFirstToPlay(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    private int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}
