package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.CardRank;
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
        if (intel.getRoundResults().isEmpty()){
            if (handStrength(intel) <= 9){
                return CardToPlay.of(weakestCard(intel));
            }
            if (handStrength(intel) <= 21){
                return CardToPlay.of(strongestCard(intel));
            }
            if (handStrength(intel) > 21){
                return CardToPlay.of(strongestCard(intel));
            }
            if (hasManilha(intel) && (handStrength(intel) > 21)){
                return CardToPlay.of(strongestCardExceptManilha(intel));
            }
            if (hasManilha(intel) && (handStrength(intel) <= 21)){
                return CardToPlay.of(strongestCard(intel));
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
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

    private int handStrength (GameIntel intel){
        int handStrength = 0;
        for (TrucoCard card : intel.getCards()){
            handStrength = handStrength + card.getRank().value();
        }
        return handStrength;
    }

    private boolean hasManilha(GameIntel intel){
        for (TrucoCard card : intel.getCards()){
            if (card.isManilha(intel.getVira())){
                return true;
            }
        }
        return false;
    }

    private TrucoCard strongestCard(GameIntel intel){
        TrucoCard strongestCard = null;
        int strength = 0;
        for (TrucoCard card : intel.getCards()){
            if (card.getRank().value() > strength){
                strongestCard = card;
                strength = card.getRank().value();
            }
        }
        return strongestCard;
    }

    private TrucoCard strongestCardExceptManilha(GameIntel intel){
        TrucoCard strongestCard = null;
        int strength = 0;
        for (TrucoCard card : intel.getCards()){
            if (card.getRank().value() > strength && !card.isManilha(card)){
                strongestCard = card;
                strength = card.getRank().value();
            }
        }
        return strongestCard;
    }

    private TrucoCard weakestCard(GameIntel intel){
        TrucoCard weakestCard = null;
        int strength = 15;
        for (TrucoCard card : intel.getCards()){
            if (card.getRank().value() < strength){
                weakestCard = card;
                strength = card.getRank().value();
            }
        }
        return weakestCard;
    }
}
