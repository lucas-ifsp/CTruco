package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class ThreeGoodCards implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getOpponentScore() < 5;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(TiaoDoTruco.hasWonFirstHand(intel)) {
            return hasThree(intel);
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()) {
            if(TiaoDoTruco.cardCanKill(intel, weakestCard) || !TiaoDoTruco.cardCanKill(intel, strongestCard)) return CardToPlay.of(weakestCard);

            if(TiaoDoTruco.cardCanKill(intel, midCard.get())) return CardToPlay.of(midCard.get());

            return CardToPlay.of(strongestCard);
        }

        if(intel.getRoundResults().isEmpty()) {
            return CardToPlay.of(strongestCard);
        }

        if(!TiaoDoTruco.cardCanKill(intel, strongestCard)) return CardToPlay.of(weakestCard);

        if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

        return CardToPlay.of(strongestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }

    private boolean hasThree(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.getRank().value() == 10);
    }
}
