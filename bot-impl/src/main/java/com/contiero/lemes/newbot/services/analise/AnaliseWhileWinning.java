package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.contiero.lemes.newbot.interfaces.Analise;

public class AnaliseWhileWinning implements Analise {

    private final GameIntel intel;

    public AnaliseWhileWinning(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public HandStatus myHand() {
        return HandStatus.BAD;
    }

}
