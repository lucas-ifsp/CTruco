package com.local.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TrucoUtils {

    static Stream<TrucoCard> cards(GameIntel intel) {
        return intel.getCards().stream();
    }

    public static int manilhaCounter(GameIntel intel) {
        return (int) cards(intel)
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private static Comparator<TrucoCard> relativelyCompare(TrucoCard vira) {
        return Comparator.comparingInt(card -> card.relativeValue(vira));
    }

    public static TrucoCard pickStrongestCard(GameIntel intel) {
        return cards(intel)
                .max(relativelyCompare(intel.getVira()))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    public static TrucoCard pickWeakestCard(GameIntel intel) {
        return cards(intel)
                .min(relativelyCompare(intel.getVira()))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    public static Optional<TrucoCard> optimalCardPick(GameIntel intel, boolean canDraw) {
        Optional<TrucoCard> enemyCardOpt = intel.getOpponentCard();
        if (enemyCardOpt.isEmpty()) return Optional.empty();

        TrucoCard enemyCard = enemyCardOpt.get();
        TrucoCard vira = intel.getVira();

        if (canDraw) return cards(intel)
                .filter(card -> card.compareValueTo(enemyCard, vira) >= 0)
                .min(relativelyCompare(vira));

        return cards(intel)
                .filter(card -> card.compareValueTo(enemyCard, vira) > 0)
                .min(relativelyCompare(vira));
    }

    public static Optional<TrucoCard> strongestNonManilha(GameIntel intel) {
        return cards(intel)
                .filter(card -> !card.isManilha(intel.getVira()))
                .max(relativelyCompare(intel.getVira()));
    }

    public static int strongCardsCounter(GameIntel intel) {
        return strongCardsCounter(intel, 8);
    }

    public static int strongCardsCounter(GameIntel intel, int value) {
        return (int) cards(intel)
                .filter(card -> card.relativeValue(intel.getVira()) > value)
                .count();
    }

    public static boolean isSpecificManilha(GameIntel intel, Predicate<TrucoCard> predicate) {
        return cards(intel).anyMatch(predicate);
    }

    public static boolean hasStrongManilha(GameIntel intel) {
        return isSpecificManilha(intel, card -> card.isEspadilha(intel.getVira()))
                || isSpecificManilha(intel, card -> card.isCopas(intel.getVira()))
                || isSpecificManilha(intel, card -> card.isZap(intel.getVira()));
    }

    public static CardToPlay playRemainingCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    public static boolean isPlayingSecond(GameIntel intel) {
        return intel.getOpponentCard().isPresent();
    }

    public static boolean firstRoundMatches(GameIntel intel, Predicate<RoundResult> predicate ) {
        return intel.getRoundResults().stream()
                .limit(1)
                .anyMatch(predicate);
    }
}
