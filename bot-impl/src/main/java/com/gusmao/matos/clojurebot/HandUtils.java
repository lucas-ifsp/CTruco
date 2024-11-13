package com.gusmao.matos.clojurebot;


import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public final class HandUtils {
    private HandUtils() {}

    public static int getHandPower(GameIntel intel, TrucoCard vira) {
        return intel.getCards().stream().mapToInt(trucoCard -> trucoCard.relativeValue(vira)).sum();
    }
}
