package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (intel.getRoundResults().isEmpty() && intel.getOpponentCard().isEmpty()){
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
        if (intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
                return CardToPlay.of(whenRespondingOpponentCard(intel));
        }
        return CardToPlay.of(intel.getCards().get(0));
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

    private TrucoCard whenRespondingOpponentCard(GameIntel intel){

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        int highestValue = 14;
        TrucoCard bestCard = null;

        List<TrucoCard> cards = intel.getCards();

        for (TrucoCard card :cards){
            if (card.getRank().value() > opponentCard.get().getRank().value() && card.getRank().value() < highestValue){
                bestCard = card;
                highestValue = card.getRank().value();
            }
        }
        if (bestCard == null && !hasManilha(intel)){
            for (TrucoCard card : cards){
                if (card.getRank().value() == opponentCard.get().relativeValue(intel.getVira())) {
                    return card;
                }
            }
            return weakestCard(intel);
        } else {
            return bestCard;
        }
    }

}
