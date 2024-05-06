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
        List<TrucoCard> cards = intel.getCards();

        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);
        // Escolhe uma carta que não seja fechada se for o segundo jogador na primeira rodada
        if (intel.getRoundResults().isEmpty() && intel.getOpenCards().size() == 1) {
            return cards.stream()
                    .filter(card -> !card.equals(TrucoCard.closed()))
                    .max(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, intel.getVira())))
                    .map(CardToPlay::of)
                    .orElse(CardToPlay.of(cards.get(0)));
        }

        // Logica generica para outras situacoes
        return cards.stream()
                .min(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, intel.getVira())))
                .map(CardToPlay::of)
                .orElse(CardToPlay.of(cards.get(0)));
    }



    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
