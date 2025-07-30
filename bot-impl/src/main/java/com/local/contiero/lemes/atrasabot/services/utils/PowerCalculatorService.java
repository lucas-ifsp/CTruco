package com.local.contiero.lemes.atrasabot.services.utils;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;

public class PowerCalculatorService {

    public static long powerOfCard(GameIntel intel, int index) {

        List<TrucoCard> myCards = intel.getCards();

        return myCards.stream()
                .map(card -> card.relativeValue(intel.getVira()))
                .sorted(Comparator.reverseOrder())
                .toList()
                .get(index);


    }

    public static boolean wonFirstRound(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
        return false;
    }

    public static boolean lostFirstRound(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
        return false;
    }

}
