package com.luna.jundi.jokerBot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import static com.luna.jundi.jokerBot.JokerBotUtils.*;

public class RoundOne implements HandState {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        //Primeiro Round sempre carta maior porem, se for o segundo a jogar na primeira rodada e oponete tem carta maior ,
        // descartar mais fraca

        if(isJokerBotWhoPlaysFirst(intel)) return bestCard(intel);
        else {
            if(isOpponentsCardBetterThenMine(intel)) return worstCard(intel);
            if(isSameCardToDrew(intel)) return cardToDrew(intel);
            return null;
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
