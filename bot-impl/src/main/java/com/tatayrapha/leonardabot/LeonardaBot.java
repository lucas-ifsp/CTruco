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
    private static final int LOWER_RANK_THRESHOLD = 6;
    private TrucoCard manilhaCard;
    private List<TrucoCard> leonardaCards;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        List<TrucoCard> currentHandCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if (opponentScore == MAO_DE_ONZE_THRESHOLD) {
            return true;
        }
        if (hasHigherCasal(currentHandCards, vira)) {
            return true;
        }
        if (hasCasal(currentHandCards, vira)) {
            return true;
        }
        if (isHandStrong(currentHandCards, vira)) {
            return true;
        }
        if (opponentScore >= 9) {
            return isHandStrong(currentHandCards, vira);
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        int score = intel.getScore();
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return !(opponentScore == MAO_DE_ONZE_THRESHOLD || score == MAO_DE_ONZE_THRESHOLD) && (hasHigherCasal(cards, vira) || hasCasal(cards, vira) || (intel.getHandPoints() + opponentScore >= 12 && isHandStrong(cards, vira)) || (!roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON && isHandStrong(cards, vira)));
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        boolean shouldBluff = determineIfBluffingIsWarranted(roundResults, cards);

        if (shouldBluff) {
            return chooseLowerRankedCard(cards);
        } else if (getMaoDeOnzeResponse(intel)) {
            return playMaoDeOnze(cards);
        } else if (roundResults.size() == 1) {
            return playFirstRound(cards);
        } else if (roundResults.size() == 2) {
            return playSecondRound(cards, roundResults);
        } else {
            return playThirdRound(cards, opponentCard);
        }
    }

    private boolean determineIfBluffingIsWarranted(List<GameIntel.RoundResult> roundResults, List<TrucoCard> cards) {
        if (roundResults == null || roundResults.isEmpty()) {
            return hasLowRankedCard(cards);
        } else if (roundResults.size() == 1) {
            if (roundResults.get(0) == GameIntel.RoundResult.LOST) {
                return hasLowRankedCard(cards);
            }
        }
        return false;
    }

    private boolean hasLowRankedCard(List<TrucoCard> cards) {
        for (TrucoCard card : cards) {
            if (card.getRank().value() < LOWER_RANK_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    private CardToPlay chooseLowerRankedCard(List<TrucoCard> cards) {
        TrucoCard lowerRankedCard = null;
        for (TrucoCard card : cards) {
            if (card.getRank().value() < LOWER_RANK_THRESHOLD) {
                if (lowerRankedCard == null || card.getRank().value() < lowerRankedCard.getRank().value()) {
                    lowerRankedCard = card;
                }
            }
        }
        return lowerRankedCard != null ? CardToPlay.of(lowerRankedCard) : null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int score = intel.getScore();
        if (hasHigherCasal(intel.getCards(), intel.getVira())) {
            return 1;
        }
        if (shouldQuit(roundResults)) {
            return -1;
        }
        if (roundResults.size() == 2 && roundResults.get(0) == GameIntel.RoundResult.LOST) {
            if (intel.getOpponentScore() >= 9) {
                return -1;
            }
        }
        if (roundResults.size() == 2) {
            if (score >= 10) {
                return 1;
            }
        }
        if (roundResults.size() == 1 && roundResults.get(0) == GameIntel.RoundResult.WON) {
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
                if (manilhas == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasCasal(List<TrucoCard> cards, TrucoCard vira) {
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

    private boolean isHandStrong(List<TrucoCard> cards, TrucoCard vira) {
        if (hasCasal(cards, vira)) {
            return true;
        }
        int strongCards = 0;
        for (TrucoCard card : cards) {
            if (card.getRank() == CardRank.THREE || card.getRank() == CardRank.TWO || card.getRank() == CardRank.ACE || hasManilha(cards, vira)) {
                strongCards += 1;
            }
        }
        return strongCards >= 2;
    }

    private boolean shouldQuit(List<GameIntel.RoundResult> roundResults) {
        if (roundResults == null) {
            return false;
        }
        return roundResults.size() == 2 && roundResults.get(0) == GameIntel.RoundResult.LOST && roundResults.get(1) == GameIntel.RoundResult.LOST;
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

    public void setManilhaCard(TrucoCard manilhaCard) {
        this.manilhaCard = manilhaCard;
    }

    public void setPlayerCards(List<TrucoCard> leonardaCards) {
        this.leonardaCards = leonardaCards;
    }

    public List<Integer> calculateCardRanks() {
        List<Integer> cardRanks = new ArrayList<>();
        for (TrucoCard card : leonardaCards) {
            cardRanks.add(calculateRank(card));
        }
        return cardRanks;
    }

    private int calculateRank(TrucoCard card) {
        return card.isManilha(manilhaCard) ? 1 : card.getRank().value();
    }
}

