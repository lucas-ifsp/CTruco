package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;

public enum BotStrategy {

    FIRST_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return CardToPlay.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
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
