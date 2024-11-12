package com.aislan.deyvin;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

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
}
