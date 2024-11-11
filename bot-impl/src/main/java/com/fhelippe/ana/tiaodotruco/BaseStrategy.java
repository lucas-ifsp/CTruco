package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class BaseStrategy implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore() < 8) return true;

        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        return firstRoundWon(intel) && getCardStrength(intel) > 7 && intel.getOpponentScore() < 9;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard= TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

        if(TiaoDoTruco.hasZap(intel ) || TiaoDoTruco.hasCopas(intel)) return CardToPlay.of(weakestCard);

        return CardToPlay.of(strongestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(TiaoDoTruco.canKill(intel) && intel.getHandPoints() <= 3 && intel.getOpponentScore() < 9) return 1;

        if(firstRoundWon(intel) && intel.getRoundResults().size() > 1) return 0;

        return -1;
    }

    public boolean firstRoundWon(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public int getCardStrength(GameIntel intel) {
        return intel.getCards()
                .get(0)
                .getRank()
                .value();
    }
}
