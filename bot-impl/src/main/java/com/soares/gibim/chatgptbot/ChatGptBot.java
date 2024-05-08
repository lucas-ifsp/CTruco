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
        if (!intel.getRoundResults().isEmpty()) {
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                return true;
            }
            if (intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
                return countManilhas(intel) == 2 || verifyIfHasManilhaAndOtherCardEqualOrHigherThanTwo(intel);
            }
            if (intel.getRoundResults().size() == 2 && hasManilha(intel) && intel.getOpponentCard().isPresent()) {
                return botCardWinsAgainstOpponentCard(intel);
            }
            if (intel.getRoundResults().size() == 2 &&
                    intel.getOpponentCard().isPresent() &&
                    (intel.getCards().get(0) == intel.getOpponentCard().get() ||
                            intel.getOpponentCard().get().relativeValue(intel.getVira()) < CardRank.QUEEN.value() ||
                            intel.getOpponentCard().get().relativeValue(intel.getVira()) < intel.getCards().get(0).getRank().value())) {
                return true;
            }
            return intel.getRoundResults().size() == 2 &&
                    intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST &&
                    (intel.getCards().get(0).relativeValue(intel.getVira()) >= 12 ||
                            intel.getCards().get(0).relativeValue(intel.getVira()) == 9);
        }
        return false;
    }


    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            if (intel.getOpponentCard().isEmpty()) {
                if (handStrength(intel) <= 9) {
                    return CardToPlay.of(weakestCard(intel));
                } else {
                    return CardToPlay.of(strongestCard(intel));
                }
            } else {
                return CardToPlay.of(whenRespondingOpponentCard(intel));
            }
        } else if (intel.getRoundResults().size() == 1) {
            if (intel.getOpponentCard().isEmpty()) {
                if (handStrength(intel) <= 14 && !hasManilha(intel)) {
                    return CardToPlay.of(strongestCard(intel));
                } else {
                    return CardToPlay.of(weakestCard(intel));
                }
            } else {
                return CardToPlay.of(whenRespondingOpponentCardInTheSecondRound(intel));
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            if (countManilhas(intel) == 2) {
                return 1;
            }
            if (verifyIfHasManilhaHigherThanSpadesAndOtherCardHigherThanTwo(intel)){
                return 1;
            }
            if (countManilhas(intel) > 0 || getSumOfCardValues(intel) > 22) {
                return 0;
            }
        } else if (intel.getRoundResults().size() == 1) {
            if (countManilhas(intel) == 2) {
                return 1;
            }
            if (verifyIfHasManilhaHigherThanSpadesAndOtherCardHigherThanTwo(intel)){
                return 1;
            }
            if (countManilhas(intel) > 0 || getSumOfCardValues(intel) > 14) {
                return 0;
            }
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && (hasManilha(intel) || hasThree(intel))) return 1;
        } else if (intel.getRoundResults().size() == 2) {

            if( (!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)
                    && getSumOfCardValues(intel) > 8 ) {
                return 1;
            }

            if (countManilhas(intel) > 0) {
                return 1;
            }
            if (getSumOfCardValues(intel) >= 7){
                return 0;
            }
        }
        return -1;
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

    private boolean hasThree(GameIntel intel){
        for (TrucoCard card : intel.getCards()){
            if (card.relativeValue(intel.getVira()) >= 9){
                return true;
            }
        }
        return false;
    }

    private boolean botCardWinsAgainstOpponentCard(GameIntel intel){
        if (intel.getCards().get(0).relativeValue(intel.getVira()) >
                intel.getOpponentCard().get().relativeValue(intel.getVira()))
            return true;
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

    private boolean verifyIfHasManilhaHigherThanSpadesAndOtherCardHigherThanTwo(GameIntel intel){
        int manilhaValue = 0;
        int cardValue = 0;
        for (TrucoCard card : intel.getCards()){
            if (card.isManilha(intel.getVira())){
                manilhaValue = card.relativeValue(intel.getVira());
            } else {
                if (cardValue < card.relativeValue(intel.getVira())) {
                    cardValue = card.relativeValue(intel.getVira());
                }
            }
        }
        if (manilhaValue >= 11 && cardValue >= 8){
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyIfHasManilhaAndOtherCardEqualOrHigherThanTwo(GameIntel intel){
        int cardValue = 0;
        for (TrucoCard card : intel.getCards()){
            if (!card.isManilha(intel.getVira())){
                cardValue = card.relativeValue(intel.getVira());
            }
        }
        if (hasManilha(intel) && cardValue >= 8){
            return true;
        } else {
            return false;
        }
    }
}
