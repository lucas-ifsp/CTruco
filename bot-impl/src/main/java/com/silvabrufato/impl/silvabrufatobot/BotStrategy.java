package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public enum BotStrategy {

    FIRST_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    },
    
    SECOND_HAND_STRATEGY  {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    },

    THIRD_HAND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'throwCard'");
        }
    };
    
    public abstract CardToPlay throwCard(GameIntel gameIntel);
}
