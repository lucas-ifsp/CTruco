package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class VeioDoBarBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard refCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        long numberOfCardsGreaterOrEqualToTwo = intel.getCards()
                .stream()
                .filter(card -> card.compareValueTo(refCard, vira) >= 0)
                .count();

        return numberOfCardsGreaterOrEqualToTwo > 2;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        var vira = intel.getVira();
        var cards = intel.getCards();
        var param = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        var cardsThree = intel.getCards().stream()
                .filter(card -> card.compareValueTo(param, vira) >= 0)
                .toList();

        if(hasCasalMaior(vira, cards)){
            return true;
        }

        if (cardsThree.size() == 3){
            return true;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        Objects.requireNonNull(intel, "Game intel must be given for the bot choose how to act!");

        var cards = sortedCards(intel);
        TrucoCard vira = intel.getVira();
        TrucoCard handSmallestCard = cards.get(0);

        if (isLastRound(intel) || wonTheFirstRound(intel) || hasCasalMaior(vira, cards))
            return CardToPlay.of(handSmallestCard);

        var refCard = intel.getOpponentCard().orElse(handSmallestCard);

        return cards.stream()
                .filter(card -> card.compareValueTo(refCard, vira) > 0)
                .min((current, next) -> current.compareValueTo(next, vira))
                .map(CardToPlay::of)
                .orElse(isFirstRound(intel) ? CardToPlay.of(handSmallestCard) : CardToPlay.discard(handSmallestCard));
    }

    private List<TrucoCard> sortedCards(GameIntel intel) {
        return intel.getCards()
                .stream()
                .sorted((current, other) -> current.compareValueTo(other, intel.getVira()))
                .toList();
    }

    private boolean hasCasalMaior(TrucoCard vira, List<TrucoCard> cards) {
        var manilhaRank = vira.getRank().next();
        return cards.contains(TrucoCard.of(manilhaRank, CardSuit.HEARTS)) &&
                cards.contains(TrucoCard.of(manilhaRank, CardSuit.CLUBS));
    }

    private boolean wonTheFirstRound(GameIntel intel) {
        var roundResults = intel.getRoundResults();
        return !roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON;
    }

    private boolean isLastRound(GameIntel intel) { return intel.getRoundResults().size() == 2; }

    private boolean isFirstRound(GameIntel intel) { return intel.getRoundResults().isEmpty(); }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        var jackParam = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
        var twoParam = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        var vira = intel.getVira();


        var cardsGreaterThanJack = intel.getCards()
                .stream()
                .filter(card -> card.compareValueTo(jackParam, intel.getVira()) >= 0)
                .toList();

        if (cardsGreaterThanJack.size() < 2){return -1;}
        if (countMainilha(intel) > 0){return 0;}

        var cardsGreaterThanTwo = intel.getCards().stream()
                .filter(card -> card.compareValueTo(twoParam, intel.getVira()) >= 0)
                .toList();

        if (cardsGreaterThanTwo.size() >= 2){
            return 0;
        }

        return -1;
    }

    private int countMainilha(GameIntel intel){
        var vira = intel.getVira();
        var allCards = sortedCards(intel);
        var contadorManilha = 0;

        for (TrucoCard allCard : allCards) {
            if (allCard.isManilha(vira))
                contadorManilha++;
        }

        return contadorManilha;
    }
}
