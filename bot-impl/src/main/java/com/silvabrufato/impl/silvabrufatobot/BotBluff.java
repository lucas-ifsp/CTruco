package com.silvabrufato.impl.silvabrufatobot;

public class BotBluff {

    public enum Probability {
        P20, P40, P60, P80;
    }

    private Probability bluffProbability;

    private BotBluff(Probability bluffProbability) {
        this.bluffProbability = bluffProbability;
    }

    public static BotBluff of(Probability bluffProbability) {
        return new BotBluff(bluffProbability);
    }

    public boolean bluff() {
        int roundNumber = (int) Math.floor(Math.random() * 10);
        return switch (this.bluffProbability) {
            case P20 -> roundNumber > 8;
            case P40 -> roundNumber > 6;
            case P60 -> roundNumber > 4;
            case P80 -> roundNumber > 2;
            default -> false;      
        };
    }
    
}
