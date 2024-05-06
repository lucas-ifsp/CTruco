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

            // encontrar nao manilha que possa ganhar
            Optional<TrucoCard> winningNonManilha = cards.stream()
                    .filter(card -> !card.isManilha(vira) && card.compareValueTo(opponentCard, vira) > 0)
                    .findFirst();

            if (winningNonManilha.isPresent()) {
                // Se existe uma nao manilha que pode ganhar, joga ela
                return CardToPlay.of(winningNonManilha.get());
            } else {
                // Caso contrario, joga a manilha mais fraca
                return utils.playWeakestManilha(manilhas);
            }
        }

        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
