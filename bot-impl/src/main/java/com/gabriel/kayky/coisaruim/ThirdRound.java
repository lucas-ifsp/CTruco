package com.gabriel.kayky.coisaruim;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Random;

//verificar tudo
public class ThirdRound implements GameStrategy{

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(intel.getOpponentCard().isPresent()){
            if(intel.getCards().stream().
                    anyMatch(e->e.compareValueTo(intel.getOpponentCard().get(),intel.getVira())>=0)){
                return 1;
            }
        }
        if(intel.getCards().stream().anyMatch(e->e.relativeValue(intel.getVira())>=7)) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore()<9){
            return intel.getCards().stream().anyMatch(e -> e.isManilha(intel.getVira()));
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getOpponentCard().isPresent()){
            if(intel.getCards().stream().anyMatch(e->e.compareValueTo(
                    intel.getOpponentCard().get(),intel.getVira())>=0)
                    &&intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)){
                return true;

            }
            if(intel.getCards().stream().anyMatch(e->e.compareValueTo(
                    intel.getOpponentCard().get(),intel.getVira())>0)){
                return true;
            }
            if(intel.getOpponentScore()<9){
                Random random=new Random();
                int maracutaia=random.nextInt(1,5);
                return maracutaia == 3;
            }
            return false;
        }
        return intel.getCards().stream().anyMatch(e -> e.relativeValue(intel.getVira()) >=7);
    }
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard cardToPlay = intel.getCards().get(0);
        return CardToPlay.of(cardToPlay);
    }
}
