package com.luigi.ana.batatafritadobarbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class BatataFritaDoBarBot implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }



    boolean checkIfIsTheFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    boolean hasZap(GameIntel intel) {
        return false;
    }

    boolean hasCopas(GameIntel intel) {
        return false;
    }

    boolean hasEspadilha(GameIntel intel) {
        return false;
    }

    boolean hasOuros(GameIntel intel) {
        return false;
    }

}