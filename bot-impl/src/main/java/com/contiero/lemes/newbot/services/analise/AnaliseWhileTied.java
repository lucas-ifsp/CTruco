package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.contiero.lemes.newbot.interfaces.Analise;

public class AnaliseWhileTied implements Analise {

    private final GameIntel intel;

    public AnaliseWhileTied(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public HandStatus myHand() {
        return HandStatus.GOOD;
    }
}
