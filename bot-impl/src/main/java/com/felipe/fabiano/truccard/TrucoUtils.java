package com.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TrucoUtils {
    public static int manilhaCounter(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    public static TrucoCard pickStrongestCard(GameIntel intel) {
        return intel.getCards().stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    public static TrucoCard pickWeakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    public static Optional<TrucoCard> optimalCardPick(GameIntel intel, boolean canDraw) {
        Optional<TrucoCard> enemyCardOpt = intel.getOpponentCard();
        if (enemyCardOpt.isEmpty()) return Optional.empty();

        TrucoCard enemyCard = enemyCardOpt.get();
        TrucoCard vira = intel.getVira();

        if (canDraw) return intel.getCards().stream()
                .filter(card -> card.relativeValue(vira) >= enemyCard.relativeValue(vira))
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)));

        return intel.getCards().stream()
                .filter(card -> card.relativeValue(vira) > enemyCard.relativeValue(vira))
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)));
    }

    public static Optional<TrucoCard> strongestNonManilha(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        TrucoCard highestNonManilha = null;

        for (TrucoCard card : cards) {
            if (!card.isManilha(vira)) {
                if (highestNonManilha == null || card.compareValueTo(highestNonManilha, vira) > 0) {
                    highestNonManilha = card;
                }
            }
        }

        if (highestNonManilha != null) {
            return Optional.of(highestNonManilha);
        }

        return Optional.empty();
    }

    public static int strongCardsCounter(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 8)
                .count();
    }

    public static int strongCardsCounter(GameIntel intel, int value) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) > value)
                .count();
    }

    public static boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    public static boolean hasCopas(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isCopas(intel.getVira()));
    }

    public static boolean hasEspadilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isEspadilha(intel.getVira()));
    }

    public static boolean hasOuros(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isOuros(intel.getVira()));
    }

    public static boolean hasStrongManilha(GameIntel intel) {
        return hasEspadilha(intel) || hasCopas(intel) || hasZap(intel);
    }

    public static CardToPlay playRemainingCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    public static boolean isPlayingSecond(GameIntel intel) {
        return intel.getOpponentCard().isPresent();
    }

    public static boolean wonFirstRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public static boolean drewFirstRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW;
    }

    public static boolean hasStrongHand(GameIntel intel, double averageThreshold, int highCardThreshold) {
        int numOfTrumpCards = manilhaCounter(intel);
        int strongCardsCount = strongCardsCounter(intel, highCardThreshold);

        return numOfTrumpCards > 0 || strongCardsCount >= 2;
    }
}
