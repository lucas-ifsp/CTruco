package com.bruno.tiago.jeckiechanbot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

public abstract sealed class Strategy permits StrategyRoundOne, StrategyRoundTwo, StrategyRoundThree {
    static Strategy of(GameIntel intel){
        int currentRound = intel.getRoundResults().size();
        if (currentRound == 0) return new StrategyRoundOne(intel);
        if (currentRound == 1) return new StrategyRoundTwo(intel);
        return new StrategyRoundThree(intel);
    }

    abstract boolean decideIfRaises();

    abstract int getRaiseResponse();

    abstract CardToPlay chooseCard();

    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = sortCards(intel.getCards(), vira);

        int differencePoints = intel.getScore() - intel.getOpponentScore();

        if (differencePoints >= 7) return getCardsValues(cards, vira) >= 24;

        if (differencePoints >= 4) return getCardsValues(cards, vira) >= 25;

        return getCardsValues(cards, vira) >= 24;
    }

    protected final Optional<TrucoCard> drawCard(List<TrucoCard> cards, TrucoCard opponentCard) {
        return cards.stream().filter(card -> card.getRank() == opponentCard.getRank()).findFirst();
    }

    protected final TrucoCard lowestCard(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream()
                .sorted((curr, next) -> curr.compareValueTo(next, vira))
                .toList().get(0);
    }

    protected final boolean haveCopas(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isCopas(vira));
    }

    protected final boolean haveZap(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().anyMatch(card -> card.isZap(vira));
    }

    protected final int getCardsValues(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().mapToInt(card -> getCardValue(card, vira)).sum();
    }

    protected final List<TrucoCard> getManilhas(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().filter((card) -> card.isManilha(vira)).toList();
    }

    protected final List<TrucoCard> sortCards(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .sorted((curr, next) -> curr.compareValueTo(next, vira))
                .toList();
    }

    protected final int getCardValue(TrucoCard card, TrucoCard vira){ //corrigir isso
        return card.relativeValue(vira) + (card.getRank().value() > vira.getRank().value() ? 1 : 0);
    }

    protected final List<TrucoCard>
    getAllHighestCardsThanOpponentCard(List<TrucoCard> cards, TrucoCard opponentCard, TrucoCard vira) {
        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .toList();
    }

}