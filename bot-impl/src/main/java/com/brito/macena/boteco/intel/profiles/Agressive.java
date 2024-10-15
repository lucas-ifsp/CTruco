package com.brito.macena.boteco.intel.profiles;

import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Game;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

public class Agressive extends ProfileBot {
    private final GameIntel intel;
    private final Status status;
    private final TrucoCard vira;
    private final TrucoCard bestCard;
    private final TrucoCard secondBestCard;
    private final TrucoCard worstCard;

    public Agressive(GameIntel intel, Status status) {
        this.intel = intel;
        this.status = status;
        vira = intel.getVira();

        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());

        bestCard = myHand.getBestCard();
        secondBestCard = myHand.getSecondBestCard();
        worstCard = myHand.getWorstCard();
    }

    @Override
    public CardToPlay firstRoundChoose() {

        if (intel.getOpponentCard().isPresent()) {
            int opponentCardOnTableValue = intel.getOpponentCard().get().relativeValue(vira);
            if (status == Status.EXCELLENT || status == Status.GOOD) {
                return CardToPlay.of(worstCard);
            }
            if (worstCard.relativeValue(vira) >= opponentCardOnTableValue) return CardToPlay.of(worstCard);
            if (secondBestCard.relativeValue(vira) >= opponentCardOnTableValue) return CardToPlay.of(secondBestCard);
            if (bestCard.relativeValue(vira) > opponentCardOnTableValue) return CardToPlay.of(bestCard);
        }

        if (haveAtLeastTwoManilhas()) {
            return CardToPlay.of(worstCard);
        }

        if (haveAtLeastOneManilha()) {
            if (bestCard.relativeValue(vira) >= 11) return CardToPlay.of(secondBestCard);
            if (bestCard.relativeValue(vira) == 10) return CardToPlay.of(bestCard);

        }
        long handPower = powerOfTheTwoBestCards();

        if (handPower >= 12 && secondBestCard.relativeValue(vira) >= 5) return CardToPlay.of(secondBestCard);
        return CardToPlay.of(bestCard);
    }

    @Override
    public CardToPlay secondRoundChoose() {
        if (Game.wonFirstRound(intel)) {
            if (status == Status.EXCELLENT) return CardToPlay.discard(worstCard);
            if (status == Status.GOOD) return CardToPlay.of(worstCard);
            return CardToPlay.of(bestCard);
        }

        if (Game.lostFirstRound(intel)) {
            Optional<TrucoCard> oppCard = intel.getOpponentCard();
            if (oppCard.isPresent()) {
                if (worstCard.compareValueTo(oppCard.get(), vira) > 0) return CardToPlay.of(worstCard);
            }
            return CardToPlay.of(bestCard);
        }

        return CardToPlay.of(bestCard);
    }

    @Override
    public CardToPlay thirdRoundChoose() {
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