package com.tatayrapha.leonardabot;

import com.bueno.spi.model.CardRank;
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
        if (intel.getOpponentScore() == MAO_DE_ONZE_THRESHOLD) {
            return true;
        }
        List<TrucoCard> currentHandCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if (intel.getOpponentScore() >= 9) {
            if (hasHigherCasal(currentHandCards, vira)){
                return true;
            }
            if (hasCasal(currentHandCards, vira)) {
                return true;
            }
            if (isHandStrong(currentHandCards, vira)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getOpponentScore() == MAO_DE_ONZE_THRESHOLD) {
            return false;
        }
        List<TrucoCard> cards = intel.getCards();
        if (intel.getHandPoints() + intel.getOpponentScore() >= 11) {
            if (hasHigherCasal(cards, intel.getVira())) {
                return true;
            }
            if (hasManilha(cards, intel.getVira())) {
                if (hasCasal(cards, intel.getVira())) {
                    return true;
                }
                if (isHandStrong(cards, intel.getVira())) {
                    return true;
                }
            }
        }
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
        if (shouldQuit(roundResults)) {
            return -1;
        } else if (hasHigherCasal(intel.getCards(), intel.getVira())) {
            return 1;
        } else if (roundResults.size() == 2) {
            if (score >= 10) {
                return 1;
            }
        } else if (roundResults.size() == 1 && roundResults.get(0) == GameIntel.RoundResult.WON) {
            return 1;
        }
        return 0;
    }

    private boolean hasManilha(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasHigherCasal(List<TrucoCard> cards, TrucoCard vira) {
        int manilhas = 0;
        for (TrucoCard card : cards) {
            if (card.isZap(vira) || card.isCopas(vira)) {
                manilhas += 1;
                if (manilhas == 2){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasCasal(List<TrucoCard> cards, TrucoCard vira){
        int manilhas = 0;
        if (hasManilha(cards, vira)) {
            for (TrucoCard card : cards) {
                if (card.isManilha(vira)) {
                    manilhas++;
                    if (manilhas == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isHandStrong(List<TrucoCard> cards, TrucoCard vira){
        if (hasCasal(cards, vira)){
            return true;
        }
        int strongCards = 0;
        for (TrucoCard card : cards) {
            if (card.getRank() == CardRank.THREE || card.getRank() == CardRank.TWO || card.getRank() == CardRank.ACE || hasManilha(cards, vira)){
                strongCards += 1;
            }
        }
        return strongCards >= 2;
    }

    private boolean shouldQuit(List<GameIntel.RoundResult> roundResults) {
        if (roundResults == null) {
            return false;
        }
        return roundResults.size() == 2 && roundResults.get(0) == GameIntel.RoundResult.LOST
                && roundResults.get(1) == GameIntel.RoundResult.LOST; // Quit the hand
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
        if (opponentCard == null) {
            return CardToPlay.of(cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0)));
        }
        CardToPlay cardToPlay = findWinningCard(cards, opponentCard);
        if (cardToPlay != null) {
            return cardToPlay;
        }
        cards.sort(Comparator.comparing(TrucoCard::getRank));
        for (int i = cards.size() - 1; i >= 0; i--) {
            if (cards.get(i).getRank().value() < opponentCard.getRank().value()) {
                return CardToPlay.of(cards.get(i));
            }
        }
        return CardToPlay.of(cards.get(0));
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
