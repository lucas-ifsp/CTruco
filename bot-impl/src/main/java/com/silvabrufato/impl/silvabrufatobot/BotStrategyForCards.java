package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;

import java.util.ArrayList;
import java.util.List;

public enum BotStrategyForCards {

    FIRST_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setTheGameIntel(gameIntel);
            sortCards();
            if(isOpponentThatStartTheHand()) return chooseCardThoWinTheHandIfPossible();
            return chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne();
        }
    },
    
    SECOND_ROUND_STRATEGY  {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
        }
    },

    THIRD_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
        }
    };
    
    private static GameIntel gameIntel;
    private static List<TrucoCard> sortedCards;

    private static void setTheGameIntel(GameIntel gameIntel) {
        BotStrategyForCards.gameIntel = gameIntel;
    }

    private static void sortCards() {
        ArrayList<TrucoCard> arrayOfCards = new ArrayList<>();
        arrayOfCards.addAll(gameIntel.getCards());
        arrayOfCards.sort((card1, card2) -> card1.compareValueTo(card2, gameIntel.getVira()));
        BotStrategyForCards.sortedCards = List.copyOf(arrayOfCards);
    }

    private static boolean isOpponentThatStartTheHand() {
        return BotStrategyForCards.gameIntel.getOpponentCard().isPresent();
    }

    private static CardToPlay chooseTheLowestCardToPlay() {
        return CardToPlay.of(sortedCards.get(0));
    }

    private static CardToPlay chooseCardThoWinTheHandIfPossible() {
        for(TrucoCard card : sortedCards)
            if(card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) > 0) 
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay();
    }

    private static CardToPlay chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne() {
        for(TrucoCard card : sortedCards) 
            if(card.isManilha(gameIntel.getVira())) return CardToPlay.of(card);
            return chooseTheLowestCardToPlay();
    }

    public abstract CardToPlay throwCard(GameIntel gameIntel);
}
