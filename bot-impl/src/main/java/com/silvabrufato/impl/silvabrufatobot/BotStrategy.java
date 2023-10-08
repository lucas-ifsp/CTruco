package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;

import java.util.ArrayList;
import java.util.List;

public enum BotStrategy {

    FIRST_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if(isOpponentThatStartTheRound()) return chooseCardThoWinTheRoundIfPossible();
            return chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne();
        }
    },
    
    SECOND_ROUND_STRATEGY  {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if(!isOpponentThatStartTheRound() && countManilhas(gameIntel) > 0) 
                return chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne();
            if(!isOpponentThatStartTheRound()) 
                return discardTheLowestCard();
            return chooseCardThoWinTheRoundIfPossible();
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

    private static void setUpStrategy(GameIntel gameIntel) {
        setTheGameIntel(gameIntel);
        sortCards();
    }

    private static void setTheGameIntel(GameIntel gameIntel) {
        BotStrategy.gameIntel = gameIntel;
    }

    private static void sortCards() {
        ArrayList<TrucoCard> arrayOfCards = new ArrayList<>();
        arrayOfCards.addAll(gameIntel.getCards());
        arrayOfCards.sort((card1, card2) -> card1.compareValueTo(card2, gameIntel.getVira()));
        BotStrategy.sortedCards = List.copyOf(arrayOfCards);
    }

    private static boolean isOpponentThatStartTheRound() {
        return BotStrategy.gameIntel.getOpponentCard().isPresent();
    }

    private static CardToPlay chooseTheLowestCardToPlay() {
        return CardToPlay.of(sortedCards.get(0));
    }

    private static CardToPlay chooseCardThoWinTheRoundIfPossible() {
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

    private static CardToPlay discardTheLowestCard() {
        return CardToPlay.discard(chooseTheLowestCardToPlay().content());
    }

     public static int countManilhas(GameIntel gameIntel) {
        int count = 0;
        for(TrucoCard card : gameIntel.getCards()) 
            if(card.isManilha(gameIntel.getVira())) count++;
        return count;
    }

    public static int countCardsEqualOrHigherThanAce(GameIntel gameIntel) {
        int count = 0;
        for(TrucoCard card : gameIntel.getCards()) 
            if(card.getRank().value() >= CardRank.ACE.value()) count++;
        return count;
    }

    public abstract CardToPlay throwCard(GameIntel gameIntel);
}
