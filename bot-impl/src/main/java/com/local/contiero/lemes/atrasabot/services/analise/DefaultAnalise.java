package com.local.contiero.lemes.atrasabot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.contiero.lemes.atrasabot.interfaces.Analise;
import com.local.contiero.lemes.atrasabot.services.utils.MyCards;
import com.local.contiero.lemes.atrasabot.services.utils.PowerCalculatorService;

import java.util.List;
import java.util.Optional;

public class DefaultAnalise extends Analise {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;


    public DefaultAnalise(GameIntel intel) {
        this.intel = intel;
        vira = intel.getVira();

        MyCards myCards = new MyCards(intel.getCards(),vira);

        bestCardValue = myCards.getBestCard().relativeValue(vira);
        secondBestCardValue = myCards.getSecondBestCard().relativeValue(vira);
    }

    @Override
    public HandStatus threeCardsHandler(List<TrucoCard> myCards) {
        if (haveAtLeastTwoManilhas()) {
            return HandStatus.GOD;
        }
        if (haveAtLeastOneManilha()) {
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, vira) > 0)
                        .count() == 3) {
                    return HandStatus.GOD;
                }
            }

            if (secondBestCardValue >= 5) return HandStatus.GOOD;
            return HandStatus.MEDIUM;
        }
        long handPower = powerOfTheTwoBestCards();
        if (handPower >= 17) {
            return HandStatus.GOOD;
        }
        if (handPower >= 10) {
            return HandStatus.MEDIUM;
        }
        return HandStatus.BAD;
    }

    @Override
    public HandStatus twoCardsHandler(List<TrucoCard> myCards) {
        if (PowerCalculatorService.wonFirstRound(intel)) {
            if (haveAtLeastOneManilha()) return HandStatus.GOD;
            if (bestCardValue >= 8) return HandStatus.GOOD;
            if (bestCardValue >= 4) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }
        if (PowerCalculatorService.lostFirstRound(intel)) {

            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, vira) > 0)
                        .count() == 2) {
                    return HandStatus.MEDIUM;
                }
                return HandStatus.BAD;
            }

            if (haveAtLeastTwoManilhas()) return HandStatus.GOD;

            if (haveAtLeastOneManilha()) {
                if (secondBestCardValue >= 8) return HandStatus.GOD;
                if (secondBestCardValue >= 6) return HandStatus.GOOD;
                return HandStatus.MEDIUM;
            }
            if (powerOfTheTwoBestCards() >= 17) return HandStatus.GOOD;
            if (powerOfTheTwoBestCards() >= 14) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }

        if (haveAtLeastOneManilha()) return HandStatus.GOD;
        if (bestCardValue == 9) return HandStatus.GOOD;
        if (bestCardValue >= 6) return HandStatus.MEDIUM;
        return HandStatus.BAD;
    }

    @Override
    public HandStatus oneCardHandler() {
        TrucoCard myCard = intel.getCards().get(0);
        Optional<TrucoCard> oppCard = intel.getOpponentCard();

        if (PowerCalculatorService.wonFirstRound(intel)) {
            if (oppCard.isPresent()) return oneCardHandlerWinningFirstRound(oppCard.get(), myCard);
        }

        if (PowerCalculatorService.lostFirstRound(intel)) {
            return oneCardHandlerLosingFirstRound();
        }

        if (intel.getHandPoints() <= 3) return HandStatus.GOD;
        if (bestCardValue >= 5) return HandStatus.GOOD;
        return HandStatus.BAD;
    }

    private HandStatus oneCardHandlerWinningFirstRound(TrucoCard oppCard, TrucoCard myCard) {
        if (intel.getHandPoints() <= 3) {
            return HandStatus.GOD;
        }
        if (myCard.compareValueTo(oppCard, vira) > 0) {
            return HandStatus.GOD;
        }
        return HandStatus.BAD;
    }

    private HandStatus oneCardHandlerLosingFirstRound() {
        if (haveAtLeastOneManilha()) {
            return HandStatus.GOD;
        }
        if (bestCardValue == 9) {
            long numberOfCardsBetterThanThree = intel.getOpenCards().stream()
                    .filter(card -> card.isManilha(vira) || card.relativeValue(vira) == 9)
                    .count();
            if (numberOfCardsBetterThanThree >= 2) {
                return HandStatus.GOD;
            }
            return HandStatus.GOOD;
        }
        if (bestCardValue == 8) {
            return HandStatus.GOOD;
        }
        if (bestCardValue >= 6) {
            return HandStatus.MEDIUM;
        }
        return HandStatus.BAD;
    }

    private boolean haveAtLeastTwoManilhas() {
        return getManilhaAmount() >= 2;
    }

    private boolean haveAtLeastOneManilha() {
        return getManilhaAmount() >= 1;
    }

    private long getManilhaAmount() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    private long powerOfTheTwoBestCards() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .mapToLong(card -> card.relativeValue(vira))
                .sorted()
                .limit(2)
                .sum();
    }
}
