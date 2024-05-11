package com.contiero.lemes.newbot.services.choose_card;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.interfaces.Choosing;
import com.contiero.lemes.newbot.services.utils.MyCards;
import static com.contiero.lemes.newbot.interfaces.Analise.HandStatus.*;

import java.util.List;
import java.util.Optional;

public class AgressiveChoosing implements Choosing {

    private final GameIntel intel;
    private final Analise.HandStatus status;
    private final TrucoCard vira;
    private final TrucoCard bestCard;
    private final TrucoCard secondBestCard;
    private final TrucoCard worstCard;

    public AgressiveChoosing(GameIntel intel, Analise.HandStatus status) {
        this.intel = intel;
        this.status = status;
        vira = intel.getVira();

        MyCards myCards = new MyCards(intel.getCards(), intel.getVira());

        bestCard = myCards.getBestCard();
        secondBestCard = myCards.getSecondBestCard();
        worstCard = myCards.getWorstCard();
    }

    @Override
    public CardToPlay firstRoundChoose() {

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

    @Override
    public CardToPlay secondRoundChoose() {
        if (wonFirstRound()){
            if (status == GOD || status == GOOD) return CardToPlay.of(worstCard);
            return CardToPlay.of(bestCard);
        }

        if (lostFirstRound()){
            Optional<TrucoCard> oppCard = intel.getOpponentCard();
            if (oppCard.isPresent()) {
                if (worstCard.compareValueTo(oppCard.get(),vira) > 0) return CardToPlay.of(worstCard);
                return CardToPlay.of(bestCard);
            }
        }

        return CardToPlay.of(bestCard);
    }

    @Override
    public CardToPlay thirdRoundChoose() {
        return null;
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
