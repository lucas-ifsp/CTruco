package com.aislan.deyvin;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;
import com.contiero.lemes.atrasabot.services.utils.MyCards;

import javax.smartcardio.Card;
import java.util.List;
import java.util.Random;

public class DeyvinBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        CardSuit[] suits = {CardSuit.HEARTS,CardSuit.DIAMONDS,CardSuit.SPADES,CardSuit.CLUBS};
        Random randomSuit = new Random();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> myCards = intel.getCards();
        CardSuit suit = suits[randomSuit.nextInt(suits.length-1)];

        if(myCards.stream().anyMatch(trucoCard -> trucoCard.isManilha(vira))) return true;

        if(intel.getScore() > intel.getOpponentScore() + 5)
            if(myCards.stream().min(TrucoCard::relativeValue).equals(TrucoCard.of(CardRank.JACK,suit)) &&
                    myCards.stream().anyMatch(trucoCard -> trucoCard.equals(TrucoCard.of(CardRank.THREE,suit)))) return true;

        if(myCards.stream().allMatch(trucoCard -> trucoCard.equals(TrucoCard.of(CardRank.THREE,suit)))) return true;

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(myCards.stream().allMatch(trucoCard -> trucoCard.isManilha(vira))) return true;
        if(intel.getRoundResults().contains(GameIntel.RoundResult.WON) && myCards.stream().anyMatch(trucoCard -> trucoCard.isManilha(vira))) return true;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);
        TrucoCard bestCard = myCards.get(myCards.size()-1);


        if(isFirstRound(intel)){
            return CardToPlay.of(myCards.get(0));
        }
        if(isSecondRound(intel)){
            if(intel.getRoundResults().contains(GameIntel.RoundResult.WON)) return CardToPlay.discard(myCards.get(0));
            else return CardToPlay.of(myCards.get(myCards.indexOf(bestCard)));
        }
        return CardToPlay.of(myCards.get(myCards.indexOf(bestCard)));
    }

    private boolean isFirstRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }
    private boolean isSecondRound(GameIntel intel){
        return intel.getRoundResults().size() == 1;
    }
    private boolean isLastRound(GameIntel intel){
        return intel.getRoundResults().size() == 2;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
