package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Analise;

import java.util.List;

public class AnaliseWhileLosing implements Analise {

    private final GameIntel intel;

    public AnaliseWhileLosing(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public HandStatus myHand() {
        List<TrucoCard> myCards = intel.getCards();
        return HandStatus.GOD;
    }
}
