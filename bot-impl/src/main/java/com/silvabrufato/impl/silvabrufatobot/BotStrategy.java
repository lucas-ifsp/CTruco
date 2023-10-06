package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.CardToPlay;

public enum BotStrategy {

    FIRST_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    },
    
    SECOND_HAND_STRATEGY  {
        @Override
        public CardToPlay throwCard() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    },

    THIRD_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    };
    
    public abstract CardToPlay throwCard(GameIntel gameIntel);
}
