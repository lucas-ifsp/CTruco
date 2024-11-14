package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class StrengthHandWithManilha implements BotServiceProvider {
    private boolean trucar;

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

        if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard); // se caso a menor carta puder matar, joga ela

        if(!TiaoDoTruco.cardCanKill(intel, weakestCard) && !TiaoDoTruco.cardCanKill(intel, strongestCard))  { //se caso não houver cartas para matar, sacrifica a menor
            if(TiaoDoTruco.hasThree(intel))return CardToPlay.discard(weakestCard);
            return CardToPlay.of(weakestCard);
        }

        return CardToPlay.of(midCard.orElse(strongestCard));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(TiaoDoTruco.hasManilha(intel) && ( TiaoDoTruco.hasThree(intel) || TiaoDoTruco.hasTwo(intel) ) && !intel.getRoundResults().isEmpty()) return 1;

        if(!TiaoDoTruco.hasWonFirstHand(intel) && !TiaoDoTruco.canKill(intel)) return -1;

        return 0;
    }
}
