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
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        if (intel.getRoundResults().isEmpty() && intel.getOpenCards().size() == 1) {
            TrucoCard selectedCard = cards.stream()
                    .filter(card -> !card.equals(TrucoCard.closed()))
                    .max(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, vira)))
                    .orElse(cards.get(0));

            return CardToPlay.of(selectedCard);
        }
        List<TrucoCard> botManilhas = cards.stream()
                .filter(card -> card.isManilha(vira))
                .toList();

        boolean hasTied = intel.getRoundResults().contains(GameIntel.RoundResult.DREW);

        if (opponentCard != null && !hasTied) {
            TrucoCard tieCard = cards.stream()
                    .filter(card -> !card.isManilha(vira) && card.getRank() == opponentCard.getRank())
                    .findFirst()
                    .orElse(null);

            if (tieCard != null) {
                return CardToPlay.of(tieCard);
            }
        }

        // Jogar a manilha mais forte apos o empate
        if (hasTied && !botManilhas.isEmpty()) {
            TrucoCard strongestManilha = botManilhas.stream()
                    .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(botManilhas.get(0));

            return CardToPlay.of(strongestManilha);
        }

        TrucoCard genericCard = cards.stream()
                .min(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, vira)))
                .orElse(cards.get(0));

        return CardToPlay.of(genericCard);
    }



    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
