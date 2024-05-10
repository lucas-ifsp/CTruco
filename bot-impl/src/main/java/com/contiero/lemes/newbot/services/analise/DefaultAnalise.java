package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.services.utils.PowerCalculatorService;

import java.util.List;
import java.util.Optional;

public class DefaultAnalise extends Analise {

    private final GameIntel intel;

    public DefaultAnalise(GameIntel intel) {
        this.intel = intel;
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
                        .filter(card -> card.compareValueTo(oppCard, intel.getVira()) > 0)
                        .count() == 3) {
                    return HandStatus.GOD;
                }
            }

            if (PowerCalculatorService.powerOfCard(intel, 1) >= 5) return HandStatus.GOOD;
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
        if (wonFirstRound()) {
            if (haveAtLeastOneManilha()) return HandStatus.GOD;
            if (PowerCalculatorService.powerOfCard(intel, 0) >= 8) return HandStatus.GOOD;
            if (PowerCalculatorService.powerOfCard(intel, 0) >= 4) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }
        if (lostFirstRound()) {

            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, intel.getVira()) > 0)
                        .count() == 2) {
                    return HandStatus.MEDIUM;
                }
                return HandStatus.BAD;
            }

            if (haveAtLeastTwoManilhas()) return HandStatus.GOD;

            if (haveAtLeastOneManilha()) {
                if (PowerCalculatorService.powerOfCard(intel, 1) >= 8) return HandStatus.GOD;
                if (PowerCalculatorService.powerOfCard(intel, 1) >= 6) return HandStatus.GOOD;
                return HandStatus.MEDIUM;
            }
            if (powerOfTheTwoBestCards() >= 17) return HandStatus.GOOD;
            if (powerOfTheTwoBestCards() >= 14) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }

        if (haveAtLeastOneManilha()) return HandStatus.GOD;
        if (PowerCalculatorService.powerOfCard(intel, 0) == 9) return HandStatus.GOOD;
        if (PowerCalculatorService.powerOfCard(intel, 0) >= 6) return HandStatus.MEDIUM;
        return HandStatus.BAD;
    }

    @Override
    public HandStatus oneCardHandler() {
        TrucoCard myCard = intel.getCards().get(0);
        Optional<TrucoCard> oppCard = intel.getOpponentCard();

        if (wonFirstRound()) {
            if (oppCard.isPresent()) return oneCardHandlerWinningFirstRound(oppCard.get(), myCard);
        }

        if (lostFirstRound()) {
            return oneCardHandlerLosingFirstRound();
        }

        if (intel.getHandPoints() <= 3) return HandStatus.GOD;
        if (PowerCalculatorService.powerOfCard(intel, 0) >= 5) return HandStatus.GOOD;
        return HandStatus.BAD;
    }

    private HandStatus oneCardHandlerWinningFirstRound(TrucoCard oppCard, TrucoCard myCard) {
        if (intel.getHandPoints() <= 3) {
            return HandStatus.GOD;
        }
        if (myCard.compareValueTo(oppCard, intel.getVira()) > 0) {
            return HandStatus.GOD;
        }
        return HandStatus.BAD;
    }

    private HandStatus oneCardHandlerLosingFirstRound() {
        if (haveAtLeastOneManilha()) {
            return HandStatus.GOD;
        }
        if (PowerCalculatorService.powerOfCard(intel, 0) == 9) {
            long numberOfCardsBetterThanThree = intel.getOpenCards().stream()
                    .filter(card -> card.isManilha(intel.getVira()) || card.relativeValue(intel.getVira()) == 9)
                    .count();
            if (numberOfCardsBetterThanThree >= 2) {
                return HandStatus.GOD;
            }
            return HandStatus.GOOD;
        }
        if (PowerCalculatorService.powerOfCard(intel, 0) == 8) {
            return HandStatus.GOOD;
        }
        if (PowerCalculatorService.powerOfCard(intel, 0) >= 6) {
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
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private long powerOfTheTwoBestCards() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .mapToLong(card -> card.relativeValue(intel.getVira()))
                .sorted()
                .limit(2)
                .sum();
    }

    private boolean wonFirstRound() {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
        return false;
    }

    private boolean lostFirstRound() {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
        return false;
    }
}
