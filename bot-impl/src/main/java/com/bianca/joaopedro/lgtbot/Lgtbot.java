package com.bianca.joaopedro.lgtbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class Lgtbot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentScore() < 7 && getStrongCards(intel, CardRank.JACK).size() == 3 &&
                getManilhas(intel).size() > 0) {
            return true;
        }
        if (intel.getOpponentScore() < 11 && getStrongCards(intel, CardRank.ACE).size() == 3 &&
                getManilhas(intel).size() > 0){
            return true;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }
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

    public List<TrucoCard> getStrongCards(GameIntel intel, CardRank referenceRank){
        return intel.getCards().stream()
                .filter(card -> card.getRank().compareTo(referenceRank) > 0)
                .toList();
    }


    private List<TrucoCard> getManilhas(GameIntel intel) {
        TrucoCard viraCard = intel.getVira();
        return intel.getCards().stream()
                .filter(carta -> carta.isManilha(viraCard))
                .toList();
    }
}
