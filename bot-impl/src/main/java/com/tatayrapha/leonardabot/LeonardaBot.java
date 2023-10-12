package com.tatayrapha.leonardabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeonardaBot implements BotServiceProvider {
    private static final int MAO_DE_ONZE_THRESHOLD = 11;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getScore() >= MAO_DE_ONZE_THRESHOLD;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        if (getMaoDeOnzeResponse(intel)) {
            return playMaoDeOnze(cards);
        } else if (roundResults.size() == 1) {
            return playFirstRound(cards);
        } else if (roundResults.size() == 2) {
            return playSecondRound(cards, roundResults);
        } else {
            return playThirdRound(cards, opponentCard);
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int score = intel.getScore();
        if (roundResults != null && roundResults.size() == 2) {
            if (score >= 10) {
                return 1;
            }
        } else if (roundResults != null && roundResults.size() == 1 && roundResults.get(0) == GameIntel.RoundResult.WON) {
            return 1;
        } else if (shouldQuit(intel)) {
            return -1;
        }
        return 0;
    }

    private boolean shouldQuit(GameIntel intel) {
        return false;
    }

    private CardToPlay playMaoDeOnze(List<TrucoCard> cards) {
        return CardToPlay.of(cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
    }

    private CardToPlay playFirstRound(List<TrucoCard> cards) {
        return CardToPlay.of(cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
    }

    private CardToPlay playSecondRound(List<TrucoCard> cards, List<GameIntel.RoundResult> roundResults) {
        if (cards.isEmpty()) {
            return null;
        }
        TrucoCard highestCard = cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
        cards.remove(highestCard);
        if (cards.isEmpty()) {
            return CardToPlay.of(highestCard);
        }
        if (roundResults != null && roundResults.size() == 2) {
            return CardToPlay.of(cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
        } else {
            return playSecondHighestCard(cards);
        }
    }

    private CardToPlay playSecondHighestCard(List<TrucoCard> cards) {
        TrucoCard highestCard = cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
        cards.remove(highestCard);
        return CardToPlay.of(cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
    }

    private CardToPlay playThirdRound(List<TrucoCard> cards, TrucoCard opponentCard) {
        CardToPlay cardToPlay = findWinningCard(cards, opponentCard);
        if (cardToPlay != null) {
            return cardToPlay;
        }
        return CardToPlay.of(cards.stream().min(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
    }

    private CardToPlay findWinningCard(List<TrucoCard> cards, TrucoCard opponentCard) {
        CardToPlay winningCard = null;
        if (opponentCard != null) {
            for (TrucoCard card : cards) {
                if (card.getRank().value() > opponentCard.getRank().value()) {
                    if (winningCard == null || card.getRank().value() > winningCard.value().getRank().value()) {
                        winningCard = CardToPlay.of(card);
                    }
                }
            }
        }
        return winningCard;
    }
}
