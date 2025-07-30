package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class TrucoTools {

    static Stream<TrucoCard> cards(GameIntel intel) {
        return intel.getCards().stream();
    }

    public static int countManilha(GameIntel intel) {
        return (int) cards(intel)
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private static Comparator<TrucoCard> relativeCompare(TrucoCard vira) {
        return Comparator.comparingInt(card -> card.relativeValue(vira));
    }

    public static TrucoCard selectStrongestCard(GameIntel intel) {
        return cards(intel)
                .max(relativeCompare(intel.getVira()))
                .orElseThrow(() -> new NoSuchElementException("SEM CARTAS DISPONÍVEIS"));
    }

    public static TrucoCard selectWeakestCard(GameIntel intel) {
        return cards(intel)
                .min(relativeCompare(intel.getVira()))
                .orElseThrow(() -> new NoSuchElementException("SEM CARTAS DISPONÍVEIS"));
    }

    public static Optional<TrucoCard> chooseOptimalCard(GameIntel intel, boolean canDraw) {
        Optional<TrucoCard> enemyCardOpt = intel.getOpponentCard();
        if (enemyCardOpt.isEmpty()) return Optional.empty();

        TrucoCard enemyCard = enemyCardOpt.get();
        TrucoCard vira = intel.getVira();

        if (canDraw) return cards(intel)
                .filter(card -> card.compareValueTo(enemyCard, vira) >= 0)
                .min(relativeCompare(vira));

        return cards(intel)
                .filter(card -> card.compareValueTo(enemyCard, vira) > 0)
                .min(relativeCompare(vira));
    }

    public static Optional<TrucoCard> strongestNonManilha(GameIntel intel) {
        return cards(intel)
                .filter(card -> !card.isManilha(intel.getVira()))
                .max(relativeCompare(intel.getVira()));
    }

    public static int countStrongCards(GameIntel intel) {
        return countStrongCards(intel, 7);
    }

    public static int countStrongCards(GameIntel intel, int value) {
        return (int) cards(intel)
                .filter(card -> card.relativeValue(intel.getVira()) > value)
                .count();
    }


    public static CardToPlay playLastCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    public static boolean isPlayingSecond(GameIntel intel) {
        return intel.getOpponentCard().isPresent();
    }


}
