package com.abel.francisco.fogao6boca;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class Fogao6Boca implements BotServiceProvider {

    Fogao6BocaUtils botUtils = new Fogao6BocaUtils();


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(botUtils.casalMaior(intel)) return true;
        if(botUtils.qtdManilhas(intel) >= 2) return true;
        if(botUtils.qtdManilhas(intel) == 1 && botUtils.qtdThree(intel) == 2) return true;
        if(botUtils.qtdThree(intel) == 3) return true;
        return botUtils.verifyElevenHandStrengh(intel) > 1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(botUtils.casalMaior(intel)) return true;
        if(botUtils.qtdManilhas(intel) >= 2) return true;
        if(botUtils.qtdManilhas(intel) == 1 && botUtils.qtdThree(intel) == 2) return true;
        if(botUtils.qtdThree(intel) == 3) return true;
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() > 1) {
            if (intel.getRoundResults().get(0) == WON || intel.getRoundResults().get(1) == WON) return true;
        }
        return botUtils.verifyHandStrengh(intel) > 7;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return botUtils.gameRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(botUtils.casalMaior(intel)) return 1;
        if(botUtils.qtdManilhas(intel) >= 2) return 1;
        if(botUtils.qtdManilhas(intel) == 1 && botUtils.qtdThree(intel) == 2) return 0;
        if(botUtils.qtdThree(intel) == 3) return 0;
        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == WON)
            if(botUtils.verifyHandStrengh(intel) > 7) return 0;
        return botUtils.bluffVerify(intel);
    }

    @Override
    public String getName(){ return BotServiceProvider.super.getName(); }


}