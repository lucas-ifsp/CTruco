package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
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
    public boolean decideIfRaises(GameIntel game) {
        if (game.getOpenCards().size() == 1) {
            TrucoCard opponentCard = game.getOpenCards().get(0);
            TrucoCard vira = game.getVira();

            CardRank zapRank = vira.getRank().next();

            boolean hasZap = game.getCards().stream()
                    .anyMatch(card -> card.getRank() == zapRank && card.getSuit() == CardSuit.CLUBS);

            boolean hasWinningCard = game.getCards().stream()
                    .filter(card -> card.getRank() != zapRank)
                    .anyMatch(card -> card.compareValueTo(opponentCard, vira) > 0);

            return hasZap && hasWinningCard;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
