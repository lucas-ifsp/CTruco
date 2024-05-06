package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

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
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> openCards = intel.getOpenCards();

        if (!openCards.isEmpty()) {
            TrucoCard opponentCard = openCards.get(0);
            List<TrucoCard> manilhas = utils.getManilhas(cards, vira);

            if (manilhas.isEmpty()) {
                throw new IllegalStateException("Expected manilhas but none found");
            }

            Optional<TrucoCard> winningNonManilha = cards.stream()
                    .filter(card -> !card.isManilha(vira) && card.compareValueTo(opponentCard, vira) > 0)
                    .findFirst();

            if (winningNonManilha.isPresent()) {
                return CardToPlay.of(winningNonManilha.get());
            } else {
                return utils.playWeakestManilha(manilhas);
            }
        }
        throw new IllegalStateException("SlayerBot chooseCard called in an unexpected game state");
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
