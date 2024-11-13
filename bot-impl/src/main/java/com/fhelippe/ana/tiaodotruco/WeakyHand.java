package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class WeakyHand implements BotServiceProvider {
    private boolean trucar = false;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return trucar;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

        if(TiaoDoTruco.cardCanKill(intel, strongestCard)) return CardToPlay.of(strongestCard);

        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }
}
