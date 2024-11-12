package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class BlackCouple implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(!intel.getRoundResults().isEmpty()) {
            if(TiaoDoTruco.firstRoundWon(intel) || intel.getRoundResults().get(1) == GameIntel.RoundResult.DREW) return true;

            return false;
        }

        return !intel.getRoundResults().isEmpty();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        //primeiro round com o oponente jogando
        if(intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            Optional<TrucoCard> drawCard = intel.getCards().stream()
                    .filter(e -> e.compareValueTo(opponentCard, intel.getVira()) == 0)
                    .findFirst();

            if(opponentCard.relativeValue(intel.getVira()) > 8 && TiaoDoTruco.cardCanKill(intel, weakestCard))
                return CardToPlay.of(drawCard.orElse(weakestCard));

            if(!TiaoDoTruco.cardCanKill(intel, strongestCard)) return CardToPlay.of(weakestCard);

            return CardToPlay.of(strongestCard);
        }

        //primeiro round comigo jogando
        if(intel.getRoundResults().isEmpty()) return CardToPlay.of(weakestCard);

        if(intel.getRoundResults().get(1) == GameIntel.RoundResult.DREW) return CardToPlay.of(strongestCard);

        return CardToPlay.of(strongestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(!intel.getRoundResults().isEmpty()) {
            if(TiaoDoTruco.lostFirstRoundWithManilha(intel) && !TiaoDoTruco.hasThree(intel) || TiaoDoTruco.hasTwo(intel)) return -1;

            if(intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) return 1;
        }

        return 0;
    }
}
