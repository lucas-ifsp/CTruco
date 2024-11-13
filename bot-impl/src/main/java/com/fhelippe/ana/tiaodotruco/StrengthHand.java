package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class StrengthHand implements BotServiceProvider {
    private boolean trucar;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getOpponentScore() < 6;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(trucar) return true;

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()) {
            if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

            if(TiaoDoTruco.cardCanKill(intel, midCard.orElse(strongestCard))) return CardToPlay.of(midCard.orElse(strongestCard));
        }

        if(TiaoDoTruco.hasWonFirstHand(intel) || intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW
                && TiaoDoTruco.hasThree(intel))
        {
            trucar = true;
            return CardToPlay.of(strongestCard);
        }

        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(TiaoDoTruco.hasWonFirstHand(intel) && TiaoDoTruco.canKill(intel)) return 1;

        if(TiaoDoTruco.hasWonFirstHand(intel) || intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW && TiaoDoTruco.hasThree(intel) || TiaoDoTruco.hasTwo(intel)) return 1;

        return -1;
    }
}
