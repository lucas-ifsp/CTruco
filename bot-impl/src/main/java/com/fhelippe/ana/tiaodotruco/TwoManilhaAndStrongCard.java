package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class TwoManilhaAndStrongCard implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return true;
    }

    public boolean firstRoundWon(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(firstRoundWon(intel) && intel.getRoundResults().size() == 1) return CardToPlay.discard(weakestCard);

        return CardToPlay.of(strongestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(TiaoDoTruco.hasWonFirstHand(intel)) return 1;

        //caso seja o segundo round e o oponente que torna
        if(!intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()) {
            if(TiaoDoTruco.canKill(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) return 1;

            if(TiaoDoTruco.canKill(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) return 0;
            return -1;
        }

        return 0;
    }
}
