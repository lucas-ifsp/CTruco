package com.local.contiero.lemes.atrasabot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.contiero.lemes.atrasabot.interfaces.Analise;
import com.local.contiero.lemes.atrasabot.services.utils.MyCards;
import com.local.contiero.lemes.atrasabot.services.utils.PowerCalculatorService;

import java.util.List;
import java.util.Optional;

public class AnaliseWhileLosing extends Analise {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;


    public AnaliseWhileLosing(GameIntel intel) {
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
                        .filter(card -> card.compareValueTo(oppCard, intel.getVira()) > 0)
                        .count() == 2 && secondBestCardValue >= 7) {
                    return HandStatus.GOOD;
                }
            }
            if (secondBestCardValue >= 8) return HandStatus.GOOD;
            return HandStatus.MEDIUM;
        }

        long handPower = powerOfTheTwoBestCards();

        if (handPower >= 14) {
            return HandStatus.GOOD;
        }
        if (handPower >= 11) {
            return HandStatus.MEDIUM;
        }
        return HandStatus.BAD;
    }

    public HandStatus twoCardsHandler(List<TrucoCard> myCards) {

        if (PowerCalculatorService.wonFirstRound(intel)) {
            if (bestCardValue >= 9) {
                return HandStatus.GOD;
            }
            if (bestCardValue >= 7) {
                return HandStatus.GOOD;
            }
            return HandStatus.MEDIUM;
        }

        if (PowerCalculatorService.lostFirstRound(intel)) {

            if (haveAtLeastTwoManilhas()) return HandStatus.GOD;

            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (secondBestCardValue > oppCard.relativeValue(vira) && bestCardValue >= 9) {
                    return HandStatus.GOOD;
                }
                return HandStatus.MEDIUM;
            }

            if (haveAtLeastOneManilha()) {

                if (secondBestCardValue >= 9) return HandStatus.GOD;
                if (secondBestCardValue == 8) return HandStatus.GOOD;
                if (secondBestCardValue == 7) return HandStatus.MEDIUM;

                return HandStatus.BAD;
            }

            if (powerOfTheTwoBestCards() >= 17) return HandStatus.GOOD;
            if (powerOfTheTwoBestCards() >= 13) return HandStatus.MEDIUM;

            return HandStatus.BAD;
        }

        if (haveAtLeastOneManilha()) return HandStatus.GOD;
        if (bestCardValue >= 8) return HandStatus.GOOD;
        if (bestCardValue >= 6) return HandStatus.MEDIUM;
        return HandStatus.BAD;
    }

    @Override
    public HandStatus oneCardHandler() {
        TrucoCard myCard = intel.getCards().get(0);
        Optional<TrucoCard> oppCard = intel.getOpponentCard();

        if (PowerCalculatorService.wonFirstRound(intel)) {
            if (oppCard.isPresent()) {
                if (myCard.compareValueTo(oppCard.get(), intel.getVira()) > 0) {
                    return HandStatus.GOD;
                }
                if (intel.getHandPoints() <= 6) {
                    return HandStatus.GOOD;
                }
                return HandStatus.BAD;
            }
        }

        if (PowerCalculatorService.lostFirstRound(intel)) {
            if (bestCardValue >= 9) return HandStatus.GOD;
            if (bestCardValue >= 7) return HandStatus.GOOD;
            if (bestCardValue >= 3) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }

        if (intel.getHandPoints() <= 3) return HandStatus.GOD;
        if (bestCardValue >= 5) return HandStatus.GOOD;
        if (bestCardValue >= 3) return HandStatus.MEDIUM;
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

}
