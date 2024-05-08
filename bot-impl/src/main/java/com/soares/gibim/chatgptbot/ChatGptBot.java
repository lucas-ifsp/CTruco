package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatGptBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return handStrength(intel) > 21;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(!CheckIfItsHandOfEleven(intel)){
            if( !intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON ){
                return true;
            }
            if(intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST && countManilhas(intel) == 2) {
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
        if ((intel.getRoundResults().size() == 1) && intel.getOpponentCard().isEmpty()){
            if (handStrength(intel) <= 14 && !hasManilha(intel)){
                return CardToPlay.of(strongestCard(intel));
            } else {
                return CardToPlay.of(weakestCard(intel));
            }
        }
        if (intel.getRoundResults().size() == 1 && intel.getOpponentCard().isPresent()){
            return CardToPlay.of(whenRespondingOpponentCardInTheSecondRound(intel));
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            if (countManilhas(intel) > 0 || getSumOfCardValues(intel) > 22) {
                return 0;
            }
        } else if (intel.getRoundResults().size() == 1) {
            if (countManilhas(intel) > 0 || getSumOfCardValues(intel) > 14) {
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
            sum +=  card.relativeValue(intel.getVira());
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


    private int handStrength (GameIntel intel){
        int handStrength = 0;
        for (TrucoCard card : intel.getCards()){
            handStrength = handStrength + card.relativeValue(intel.getVira());
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
            if (card.relativeValue(intel.getVira()) > opponentCard.get().relativeValue(intel.getVira())
                    && card.relativeValue(intel.getVira()) < highestValue){
                bestCard = card;
                highestValue = card.relativeValue(intel.getVira());
            }
        }
        if (bestCard == null){
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

    private TrucoCard whenRespondingOpponentCardInTheSecondRound(GameIntel intel){

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        int highestValue = 14;
        TrucoCard bestCard = null;

        List<TrucoCard> cards = intel.getCards();

        for (TrucoCard card :cards){
            if (card.relativeValue(intel.getVira()) > opponentCard.get().relativeValue(intel.getVira())
                    && card.relativeValue(intel.getVira()) < highestValue){
                bestCard = card;
                highestValue = card.relativeValue(intel.getVira());
            }
        }

        if (bestCard == null){
            return strongestCard(intel);
        } else {
            return bestCard;
        }
    }
}
