package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import javax.smartcardio.Card;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Trucorinthians implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        return intel.getCards().stream()
                .anyMatch(card -> card.isManilha(vira));
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
        boolean isLosing = intel.getScore() < intel.getOpponentScore();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        return switch (round) {
            case 0 -> {
                if (isLosing) {
                    yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
            case 1 -> getSecondRoundStrategy(intel);
            case 2 -> getStrongest(hand, vira);
            default -> CardToPlay.of(hand.get(0));
        };

    }

    private CardToPlay getSecondRoundStrategy(GameIntel intel) {
        GameIntel.RoundResult firstResult = intel.getRoundResults().get(0);
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();
        boolean isLosing = intel.getScore() < intel.getOpponentScore();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        return switch (firstResult) {
            case WON -> {
                if (isLosing) {
                    yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
            case LOST -> getStrongest(hand, vira);
            case DREW -> {
                if (isLosing) {
                    yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
        };
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
                .orElseGet(() -> getWeakest(hand, vira));
    }

    private CardToPlay getStrongest(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard strongest = hand.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(TrucoCard.closed());
        return CardToPlay.of(strongest);
    }

    private CardToPlay getSmallestWinning(List<TrucoCard> hand, TrucoCard vira, TrucoCard opponentCard) {
        Optional<TrucoCard> winning = hand.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)));

        return winning.map(CardToPlay::of).orElseGet(() -> getSmallestNonLosing(hand, vira, opponentCard));
    }
}
