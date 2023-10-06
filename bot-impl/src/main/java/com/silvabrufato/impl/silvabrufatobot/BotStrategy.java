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
            TrucoCard cardOne = null;
            TrucoCard cardTwo = null;

            for (TrucoCard card : cards) {
                if (card.isManilha(vira)) manilha = card;
            }

            for (TrucoCard card : cards) {
                if (!card.isManilha(vira)){
                    cardOne = card;
                }
            }

            for (TrucoCard card : cards) {
                if (!card.isManilha(vira) && card != cardOne){
                    cardTwo = card;
                }
            }

            return (cardOne.compareValueTo(cardTwo, vira) >= 0) ? CardToPlay.of(cardOne) : CardToPlay.of(cardTwo);
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
