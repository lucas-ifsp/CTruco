package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.stream.Collectors;

public final class VeioDoBarBot implements BotServiceProvider {
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
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        var param = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
        var cards = intel.getCards()
                .stream()
                .filter(card -> card.compareValueTo(param, intel.getVira()) >= 0)
                .toList();

        return cards.isEmpty() ? -1 : 0;
    }
}
