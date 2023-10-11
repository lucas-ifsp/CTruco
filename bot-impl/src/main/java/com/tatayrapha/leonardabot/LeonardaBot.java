package com.tatayrapha.leonardabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class LeonardaBot implements BotServiceProvider {
    private static final int QUIT_THRESHOLD = 9;
    private static final int ACCEPT_THRESHOLD = 10;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        int playerScore = intel.getScore();
        return playerScore >= 9 && cards.stream().anyMatch(card -> card.getRank() == CardRank.ACE);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int playerScore = intel.getScore();
        return playerScore >= QUIT_THRESHOLD || intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);
        if (opponentCard == null) {
            TrucoCard chosenCard = cards.stream().min(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
            return CardToPlay.of(chosenCard);
        }
        TrucoCard highestWinningCard = null;
        for (TrucoCard card : cards) {
            if (card.getRank().value() > opponentCard.getRank().value()) {
                if (highestWinningCard == null || card.getRank().value() > highestWinningCard.getRank().value()) {
                    highestWinningCard = card;
                }
            }
        }
        if (highestWinningCard != null) {
            return CardToPlay.of(highestWinningCard);
        }
        TrucoCard lowestCard = cards.stream().min(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
        return CardToPlay.of(lowestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int playerScore = intel.getScore();
        if (playerScore < QUIT_THRESHOLD) {
            return -1;
        } else if (playerScore <= ACCEPT_THRESHOLD) {
            return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira())) ? 1 : 0;
        } else {
            return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira())) ? 1 : 0;
        }
    }
}
