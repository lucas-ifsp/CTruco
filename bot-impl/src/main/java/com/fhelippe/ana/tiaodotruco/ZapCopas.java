package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class ZapCopas implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return !intel.getRoundResults().isEmpty();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(intel.getRoundResults().isEmpty()) return CardToPlay.of(midCard.get());

        if(intel.getRoundResults().size() == 1) CardToPlay.discard(weakestCard);

        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 1;
    }
}
