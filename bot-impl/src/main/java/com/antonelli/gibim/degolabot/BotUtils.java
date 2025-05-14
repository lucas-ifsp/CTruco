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


}
