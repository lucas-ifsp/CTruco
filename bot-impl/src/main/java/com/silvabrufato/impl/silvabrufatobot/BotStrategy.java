package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;

import java.util.List;

public enum BotStrategy {

    FIRST_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            List<TrucoCard> cards = gameIntel.getCards();
            TrucoCard vira = gameIntel.getVira();
            TrucoCard manilha = null;
            TrucoCard cardTwo = null;
            TrucoCard cardThree = null;

            //verified if exists manilha
            for (TrucoCard card : cards) {
                if (card.isManilha(vira)) manilha = card;
            }

            //if not exists manilha verified higest card
            if (manilha == null) {
                //compare 1° with 2°
                if (cards.get(0).compareValueTo(cards.get(1), vira) >=0 ){
                    //compare 1° with 3°
                    if (cards.get(0).compareValueTo(cards.get(2), vira) >= 0){
                        return CardToPlay.of(cards.get(0));
                    }else{
                        return CardToPlay.of(cards.get(2));
                    }
                }else {
                    //compare 2° with 3°
                    if (cards.get(1).compareValueTo(cards.get(2), vira) >= 0){
                        return CardToPlay.of(cards.get(1));
                    }else{
                        return CardToPlay.of(cards.get(2));
                    }
                }
            }

            for (TrucoCard card : cards) {
                if (!card.isManilha(vira)){
                    cardTwo = card;
                }
            }

            for (TrucoCard card : cards) {
                if (!card.isManilha(vira) && card != cardTwo){
                    cardThree = card;
                }
            }
            return (cardTwo.compareValueTo(cardThree, vira) >= 0) ? CardToPlay.of(cardTwo) : CardToPlay.of(cardThree);
        }
    },
    
    SECOND_HAND_STRATEGY  {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
        }
    },

    THIRD_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
        }
    };
    
    public abstract CardToPlay throwCard(GameIntel gameIntel);
}
