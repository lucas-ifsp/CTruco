package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class MiddleHand implements BotServiceProvider {
    private boolean trucar;

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

        //primeira rodada adversario começa
        if(intel.getOpponentCard().isPresent() && intel.getRoundResults().isEmpty()) {
            if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

            if(TiaoDoTruco.cardCanKill(intel, midCard.orElse(strongestCard))) return CardToPlay.of(midCard.orElse(strongestCard));

            return CardToPlay.of(weakestCard);
        }

        //primeira rodada eu começo
        if(intel.getRoundResults().isEmpty()) {
           return CardToPlay.of(strongestCard);
        }

        //segunda rodada eu começo
        if(TiaoDoTruco.hasWonFirstHand(intel)) { //se eu ganhei a primeira rodada
            if(TiaoDoTruco.hasThree(intel)) {
                trucar = true;
                return CardToPlay.of(strongestCard);
            }

            return CardToPlay.of(strongestCard);
        }

        //segunda rodada adversário joga
        if(intel.getOpponentCard().isPresent()) {
            if(TiaoDoTruco.canKill(intel)) { //caso eu possa matar
                if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

                return CardToPlay.of(strongestCard);
            }

            return CardToPlay.of(weakestCard);
        }


        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        if(TiaoDoTruco.hasWonFirstHand(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) { //se ganhou a primeira
            try{
                if(TiaoDoTruco.canKill(intel)) return 1;
            } catch (IllegalArgumentException e) {
                System.err.println(e + " Acessando uma propriedade inexistente em middle hand");
            }

            if(TiaoDoTruco.hasThree(intel) || TiaoDoTruco.getHandStrength(intel) > 10) { //se caso tem carta boa e o oponente nao vai abrir muita vantagem
                return 0;
            }
        }

        return -1;
    }
}
