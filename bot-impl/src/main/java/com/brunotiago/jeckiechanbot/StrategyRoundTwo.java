package com.brunotiago.jeckiechanbot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.GameIntel.RoundResult;

import java.util.List;
import java.util.Optional;

public final class StrategyRoundTwo extends Strategy {
    GameIntel intel;
    List<TrucoCard> cards;
    Optional<TrucoCard> opponentCard;
    TrucoCard vira;
    List<TrucoCard> manilhas;
    RoundResult firstRoundResult;

    public StrategyRoundTwo(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = sortCards(intel.getCards(), vira);
        this.manilhas = getManilhas(cards, vira);
        this.opponentCard = intel.getOpponentCard();
        this.firstRoundResult = intel.getRoundResults().get(0);
    }

    @Override
    CardToPlay chooseCard() {
        return switch (firstRoundResult) {
            case WON -> CardToPlay.of(cards.get(0));
            case LOST -> {
                if (opponentCard.isPresent()) {
                    List<TrucoCard> highestCards = getAllHighestCardsThanOpponentCard(cards, opponentCard.get(), vira);
                    if (!highestCards.isEmpty()) yield CardToPlay.of(highestCards.get(0));
                }
                yield CardToPlay.of(cards.get(1));
            }
            default -> CardToPlay.of(cards.get(1));
        };
    }

    @Override
    int getRaiseResponse() {
        TrucoCard lowestCard = cards.get(0);
        TrucoCard highestCard = cards.get(cards.size() - 1);
        int lowestCardValue = getCardValue(lowestCard, vira);
        int highestCardValue = getCardValue(highestCard, vira);
        int handPoints = intel.getHandPoints();
        Optional<TrucoCard> lastOpponentCard = opponentCard;

        return switch (firstRoundResult) {
            case WON -> {
                if (lowestCardValue > CardRank.TWO.value()) yield 1;
                if (lowestCardValue >= CardRank.KING.value()) yield 0;
                yield -1;
            }
            case DREW -> {
                if (lastOpponentCard.isPresent()) {
                    int isCardWinning = highestCard.compareValueTo(lastOpponentCard.get(), vira);
                    if (isCardWinning > 0) yield  1;
                    if (isCardWinning == 0 && lowestCardValue >= CardRank.KING.value()) yield 0;
                    yield -1;
                }
                if (haveZap(manilhas, vira) || haveCopas(manilhas, vira) || lowestCard.isZap(vira) || lowestCard.isCopas(vira)) yield 1;
                if (highestCardValue >= CardRank.THREE.value() && handPoints == 1) yield 0;
                yield -1;
            }
            case LOST -> {
                if (lowestCardValue >= CardRank.ACE.value() && handPoints == 1) yield 0;
                yield -1;
            }
        };
    }

    @Override
    boolean decideIfRaises() {
        TrucoCard strongestCard = cards.get(1);
        int strongestCardValue = getCardValue(strongestCard, vira);

        return switch (firstRoundResult) {
            case WON -> strongestCardValue >= CardRank.KING.value();
            case DREW -> {
                if (opponentCard.isEmpty()) yield strongestCardValue >= CardRank.KING.value();
                yield strongestCardValue > getCardValue(opponentCard.get(), vira);
            }
            default -> getCardValue(cards.get(0), vira) >= CardRank.TWO.value() && intel.getHandPoints() == 1;
        };
    }
}