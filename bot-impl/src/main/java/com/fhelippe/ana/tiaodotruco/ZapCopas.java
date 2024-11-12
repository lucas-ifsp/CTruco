package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class ZapCopas implements BotServiceProvider {
    private boolean trucar;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {

        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(trucar) return true;

        return !intel.getRoundResults().isEmpty();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();

            if(opponentCard.compareValueTo(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), intel.getVira()) > 1) {
                if(weakestCard.compareValueTo(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), intel.getVira()) >= 0) trucar = true;
            }
        }

        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 1;
    }
}
