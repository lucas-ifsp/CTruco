package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class ThreeComaTwoAndManilha implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getOpponentScore() < 6;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return intel.getRoundResults().size() > 1 && TiaoDoTruco.firstRoundWon(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        //primeira mao com o oponente jogando
        if(intel.getOpponentCard().isPresent() && intel.getRoundResults().isEmpty()) {
            //caso eu possa empatar a primeira, empato e peço truco para jogar a mais forte
            TrucoCard opponentCard = intel.getOpponentCard().get();
            Optional<TrucoCard> drawCard = intel.getCards().stream()
                    .filter(e -> e.compareValueTo(opponentCard, intel.getVira()) == 0)
                    .findFirst();

            if(TiaoDoTruco.cardCanKill(intel, weakestCard)) {
                return CardToPlay.of(
                        drawCard.orElse(weakestCard)
                );
            }

            return CardToPlay.of(weakestCard);
        }

        //se for o nosso bot jogando a primeira
        if(intel.getRoundResults().isEmpty()) {
            return CardToPlay.of(midCard.get());
        }

        if(TiaoDoTruco.firstRoundWon(intel)) {
            CardToPlay.discard(weakestCard);
        }

        //todo jogar o dois pra perder a primeira e depois tentar ganhar as outras duas

        return CardToPlay.of(strongestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
