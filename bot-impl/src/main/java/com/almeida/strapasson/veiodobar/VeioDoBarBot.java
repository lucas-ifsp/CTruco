package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
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
        var cards = sortedCards(intel);
        var refCard = intel.getOpponentCard().orElse(cards.get(0));
        TrucoCard minCardToWin = null;
        TrucoCard vira = intel.getVira();

        if (cards.get(2).compareValueTo(refCard, vira) > 0)
            minCardToWin = cards.get(2);
        if (cards.get(1).compareValueTo(refCard, vira) > 0)
            minCardToWin = cards.get(1);
        if (cards.get(0).compareValueTo(refCard, vira) > 0)
            minCardToWin = cards.get(0);

        return minCardToWin == null ? CardToPlay.discard(cards.get(0)) : CardToPlay.of(minCardToWin);
    }

    private List<TrucoCard> sortedCards(GameIntel intel) {
        return intel.getCards()
                .stream()
                .sorted((current, other) -> current.compareValueTo(other, intel.getVira()))
                .toList();
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
