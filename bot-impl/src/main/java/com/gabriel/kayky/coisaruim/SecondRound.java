package com.gabriel.kayky.coisaruim;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public class SecondRound implements GameStrategy {


    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(intel.getOpponentCard().isPresent()){
            if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW) ){
                if(intel.getCards().stream().anyMatch(e->e.compareValueTo(intel.getOpponentCard().get(),intel.getVira())>=0)) {
                    return 1;
                }
            }
            if(intel.getCards().stream().filter(e->e.compareValueTo(intel.getOpponentCard().
                    get(),intel.getVira())>0).count()>1 ){
                if(intel.getCards().stream().anyMatch(e -> e.isManilha(intel.getVira()))){
                    return 1;
                }
                if(intel.getCards().stream().anyMatch(e->e.relativeValue(intel.getVira())>8)){
                    return 0;
                }
            }
        }
        if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)){
            if(intel.getCards().stream().anyMatch(e->e.isManilha(intel.getVira()))){
                return 1;
            }
            else if(intel.getCards().stream().
                    filter(e->e.relativeValue(intel.getVira())>8).count()>=2 && intel.getOpponentScore()<9){
                return 0;
            }

        }
        if(intel.getOpponentScore()<9 && intel.getCards().stream().anyMatch(e->e.isManilha(intel.getVira()))) {
            return 0;
        }
        if(intel.getCards().stream().anyMatch(e -> e.relativeValue(intel.getVira()) > 7) && intel.getOpponentScore()<9){
            return 0;
        }
        if(intel.getCards().
                stream().
                anyMatch(e->e.isManilha(intel.getVira())) && intel.getCards()
                .stream().anyMatch(e->!e.isManilha(intel.getVira()) &&
                        e.relativeValue(intel.getVira())>=8)){
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
        if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)){
            if(intel.getCards().stream().anyMatch(e->e.isManilha(intel.getVira()))){
                return true;
            }
            return intel.getCards().stream().filter(e -> e.relativeValue(intel.getVira()) >= 8).count() >= 2;
        }
        if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW)){
            if(intel.getOpponentCard().isPresent()){
                if(intel.getCards().stream().anyMatch(e->e.compareValueTo(intel.getOpponentCard().get(), intel.getVira())>0)){
                    return true;
                }
            }
            return intel.getCards().stream().anyMatch(e -> e.relativeValue(intel.getVira()) > 10);
        }
        return intel.getCards().stream().filter(e -> e.relativeValue(intel.getVira()) >= 9).count() >= 2;
    }

    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard strongest=intel.getCards().get(0);
        TrucoCard weakest=intel.getCards().get(1);
        if(weakest.relativeValue(intel.getVira())>strongest.relativeValue(intel.getVira())){
            TrucoCard temp=strongest;
            strongest=weakest;
            weakest=temp;

        }
        if (intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW)) {
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard opponentCard = intel.getOpponentCard().get();
                if (weakest.compareValueTo(opponentCard, intel.getVira()) > 0) {
                    return CardToPlay.of(weakest);
                }
                return CardToPlay.of(strongest);
            }
            return CardToPlay.of(strongest);
        }
        if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.LOST)){TrucoCard opponentCard = intel.
                getOpponentCard().get();
            if (weakest.compareValueTo(opponentCard, intel.getVira()) > 0) {
                return CardToPlay.of(weakest);
            }
            return CardToPlay.of(strongest);
        }
        if(weakest.relativeValue(intel.getVira())>=6){
            return CardToPlay.of(weakest);
        }
        strongest.isZap(intel.getVira());
        return CardToPlay.of(strongest);
    }
}
