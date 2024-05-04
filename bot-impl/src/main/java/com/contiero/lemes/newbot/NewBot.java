package com.contiero.lemes.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.services.analise.AnaliseWhileLosing;
import com.contiero.lemes.newbot.services.analise.AnaliseWhileTied;
import com.contiero.lemes.newbot.services.analise.AnaliseWhileWinning;

public class NewBot implements BotServiceProvider {
    private static Analise.HandStatus status = Analise.HandStatus.BAD;

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
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand();

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private Analise createAnaliseInstance(GameIntel intel){
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;

        if (scoreDistance <= 3 && scoreDistance >= -3){
            return new AnaliseWhileTied(intel);
        } else if (myScore>oppScore) {
            return new AnaliseWhileWinning(intel);
        }
        else {
            return new AnaliseWhileLosing(intel);
        }

    }
}
