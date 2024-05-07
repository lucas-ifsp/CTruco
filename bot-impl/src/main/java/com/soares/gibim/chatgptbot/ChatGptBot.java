package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class ChatGptBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(!CheckIfItsHandOfEleven(intel)){
            if( !intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON ){
                return true;
            }
            if(intel.getRoundResults().size() == 2 && haveZap(intel)){
                return true;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            if (countManilhas(intel) > 0 || getSumOfCardValues(intel) > 24){
                return 0;
            }
        }
        return -1;

    }

    private boolean haveZap(GameIntel intel){
        for (TrucoCard card : intel.getCards()){
            if (card.isZap(intel.getVira())) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckIfItsHandOfEleven(GameIntel intel){
        return intel.getHandPoints() == 11;
    }

    int getSumOfCardValues(GameIntel intel) {
        int sum = 0;

        for (TrucoCard card : intel.getCards()) {
            sum += card.relativeValue(intel.getVira());
        }

        return sum;
    }
    int countManilhas(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                count += 1;
            }
        }
        return count;
    }

}
