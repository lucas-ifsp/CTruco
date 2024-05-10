package com.contiero.lemes.newbot.services.utils;

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

}
