package com.contiero.lemes.newbot.services.choose_card;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Choosing;
import com.contiero.lemes.newbot.services.utils.MyCards;

import java.util.List;

public class AgressiveChoosing implements Choosing {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final TrucoCard bestCard;
    private final TrucoCard secondBestCard;
    private final TrucoCard worstCard;

    @Override
    public CardToPlay choose(GameIntel intel) {
        return null;
    }

    public AgressiveChoosing(GameIntel intel) {
        this.intel = intel;
        vira = intel.getVira();

        MyCards myCards = new MyCards(intel.getCards(), intel.getVira());

        bestCard = myCards.getBestCard();
        secondBestCard = myCards.getSecondBestCard();
        worstCard = myCards.getWorstCard();
    }

    public CardToPlay firstRoundChooseCard(GameIntel intel) {
        int bestCardValue = bestCard.relativeValue(vira);
        int secondBestCardValue = secondBestCard.relativeValue(vira);
        int worstCardValue = worstCard.relativeValue(vira);

        if (intel.getOpponentCard().isPresent()) {
            int opponentCardOnTableValue = intel.getOpponentCard().get().relativeValue(vira);

            if (worstCardValue >= opponentCardOnTableValue) return CardToPlay.of(worstCard);
            if (secondBestCardValue >= opponentCardOnTableValue) return CardToPlay.of(secondBestCard);
            if (bestCardValue > opponentCardOnTableValue) return CardToPlay.of(bestCard);
        }

        if (haveAtLeastTwoManilhas()) {
            if (secondBestCardValue == 12 || secondBestCardValue == 11) return CardToPlay.of(worstCard);
            if (secondBestCardValue == 10) return CardToPlay.of(secondBestCard);
        }

        if (haveAtLeastOneManilha()) {
            if (bestCardValue >= 11) return CardToPlay.of(secondBestCard);
            if (bestCardValue == 10) return CardToPlay.of(bestCard);

        }
        long handPower = powerOfTheTwoBestCards();

        if (handPower >= 16 && secondBestCardValue >= 8) return CardToPlay.of(secondBestCard);
        return CardToPlay.of(bestCard);

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
