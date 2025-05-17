package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class Trucorinthians implements BotServiceProvider {
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
        List<TrucoCard> hand = intel.getCards();

        if (hand.isEmpty()) {
            return CardToPlay.discard(TrucoCard.closed());
        }

        TrucoCard vira = intel.getVira();
        int round = intel.getRoundResults().size();
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();

        if (round == 0 && isFirstToPlay) {
            TrucoCard weakest = hand.stream()
                    .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(TrucoCard.closed());

            return CardToPlay.of(weakest);
        }

        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
