package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BotUtils {

    public static boolean isPlayingSecond(GameIntel intel) {
        return intel.getOpponentCard().isPresent();
    }

    public static Stream<TrucoCard> cards(GameIntel intel) {
        return intel.getCards().stream();
    }

    public static TrucoCard selectStrongestCard(GameIntel intel) {
        return cards(intel)
                .max(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())))
                .orElseThrow();
    }

    public static TrucoCard selectWeakestCard(GameIntel intel) {
        return cards(intel)
                .min(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())))
                .orElseThrow();
    }

    public static long countStrongCards(GameIntel intel) {
        return cards(intel)
                .filter(card -> card.relativeValue(intel.getVira()) >= 8)
                .count();
    }

    public static long countStrongCards(GameIntel intel, int threshold) {
        return cards(intel)
                .filter(card -> card.relativeValue(intel.getVira()) >= threshold)
                .count();
    }

    public static long countManilha(GameIntel intel) {
        return cards(intel)
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    public static Optional<TrucoCard> chooseOptimalCard(GameIntel intel, boolean aggressive) {
        return cards(intel)
                .sorted(Comparator.comparingInt(c -> c.relativeValue(intel.getVira()) * (aggressive ? -1 : 1)))
                .findFirst();
    }

    public static Optional<TrucoCard> strongestNonManilha(GameIntel intel) {
        return cards(intel)
                .filter(c -> !c.isManilha(intel.getVira()))
                .max(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())));
    }

    public static boolean didWinFirstRound(GameIntel intel) {
        List<GameIntel.RoundResult> resultados = intel.getRoundResults();


        if (resultados.isEmpty()) return false;

        return resultados.get(0) == GameIntel.RoundResult.WON;
    }
    public static boolean hasOnlyWeakCards(GameIntel intel) {
        return countWeakCards(intel) == 3;
    }

    public static boolean hasOnlyMediumCards(GameIntel intel) {
        return countMediumCards(intel) >= 1
                && countStrongCards(intel, 8) == 0
                && countManilha(intel) == 0;
    }

    public static boolean hasOnlyStrongCards(GameIntel intel) {
        return countStrongCards(intel, 8) >= 1 && countManilha(intel) == 0;
    }

    public static boolean hasManilhaAndStrongCard(GameIntel intel) {
        return countManilha(intel) == 1 && countStrongCards(intel, 8) >= 1;
    }

    public static boolean hasManilhaAndTwoWeakCards(GameIntel intel) {
        return countManilha(intel) == 1 && countWeakCards(intel) == 2;
    }
    public static long countMediumCards(GameIntel intel) {
        return intel.getCards().stream()
                .filter(c -> {
                    int val = c.relativeValue(intel.getVira());
                    return val > 5 && val < 8;
                }).count();
    }
    public static long countWeakCards(GameIntel intel) {
        return intel.getCards().stream()
                .filter(c -> {
                    int val = c.relativeValue(intel.getVira());
                    return val >= 1 && val <= 5;
                }).count();
    }
    public static Optional<TrucoCard> findManilha(GameIntel intel) {
        return intel.getCards().stream()
                .filter(c -> c.isManilha(intel.getVira()))
                .findFirst();
    }
    public static TrucoCard selectCardGreaterThanTwo(GameIntel intel) {
        return intel.getCards().stream()
                .filter(c -> c.relativeValue(intel.getVira()) > 2)
                .findFirst()
                .orElse(selectStrongestCard(intel));
    }

    public static TrucoCard findWeakestWinningCard(List<TrucoCard> cards, TrucoCard adversario, GameIntel intel) {
        return cards.stream()
                .filter(c -> c.relativeValue(intel.getVira()) > adversario.relativeValue(intel.getVira()))
                .min(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())))
                .orElse(null);
    }

    public static TrucoCard findTyingCard(List<TrucoCard> cards, TrucoCard adversario, GameIntel intel) {
        return cards.stream()
                .filter(c -> c.relativeValue(intel.getVira()) == adversario.relativeValue(intel.getVira()))
                .findFirst()
                .orElse(null);
    }

    public static int handStrength(GameIntel intel) {
        int handStrength = 0;
        for (TrucoCard card : intel.getCards()) {
            handStrength += card.relativeValue(intel.getVira());
        }
        return handStrength;
    }

}
