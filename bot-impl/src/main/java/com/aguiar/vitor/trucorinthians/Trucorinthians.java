package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import javax.smartcardio.Card;
import java.util.Comparator;
import java.util.List;

public class Trucorinthians implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getStrategicCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private CardToPlay getStrategicCard(GameIntel intel) {
        int round = intel.getRoundResults().size();
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        switch (round) {
            case 0:
                return isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            case 1:
                return getSecondRoundStrategy(intel);
            case 2:
                return getStrongest(hand, vira);
            default:
                break;
        }

        return CardToPlay.of(hand.get(0));
    }

    private CardToPlay getWeakest(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard weakest = hand.stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(TrucoCard.closed());
        return CardToPlay.of(weakest);
    }

    private CardToPlay getSmallestNonLosing(List<TrucoCard> hand, TrucoCard vira, TrucoCard opponentCard) {
        return hand.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) >= 0)
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .map(CardToPlay::of)
                .orElseGet(() -> {
                    return getWeakest(hand, vira);
                });
    }

    private CardToPlay getStrongest(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard strongest = hand.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(TrucoCard.closed());
        return CardToPlay.of(strongest);
    }

    private CardToPlay getSecondRoundStrategy(GameIntel intel) {
        GameIntel.RoundResult firstResult = intel.getRoundResults().get(0);
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        switch (firstResult) {
            case WON:
                return isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            case LOST:
                return getStrongest(hand, vira);
            case DREW:
                return getStrongest(hand, vira);
        }

        return getStrongest(hand, vira);
    }
}
