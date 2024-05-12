package com.luna.jundi.jokerBot.states;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.luna.jundi.jokerBot.JokerBotUtils.*;
import static com.luna.jundi.jokerBot.utils.CardUtils.*;

public class RoundOneState implements RoundState {

    public RoundOneState(GameIntel intel) {
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        //Primeiro Round sempre carta maior porem, se for o segundo a jogar
        // na primeira rodada e oponente tem carta maior , descartar mais fraca

        if(isJokerBotWhoPlaysFirst(intel)) return getBestCard(intel);
        else {
            if(isOpponentsCardBetterThenMine(intel)) return getWorstCard(intel);
            if(isSameCardToDrew(intel)) return drewCard(intel);
            return null;
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
