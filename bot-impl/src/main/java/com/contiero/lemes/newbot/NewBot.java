package com.contiero.lemes.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.services.analise.AnaliseWhileLosing;
import com.contiero.lemes.newbot.services.analise.DefaultAnalise;
import com.contiero.lemes.newbot.services.utils.PowerCalculator;

import static com.contiero.lemes.newbot.interfaces.Analise.HandStatus.*;

public class NewBot implements BotServiceProvider {
    private static Analise.HandStatus status = Analise.HandStatus.BAD;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand();
        if (status == GOD) return true;
        if (status == GOOD) {
            int scoreDistance = intel.getScore() - intel.getOpponentScore();
            if (scoreDistance >= 4) return true;
            return PowerCalculator.powerOfCard(intel, 0) >= 9;
        }
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
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand();
        return 0;
    }

    private Analise createAnaliseInstance(GameIntel intel){
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;

        if (oppScore>myScore && scoreDistance < -6) {
            return new AnaliseWhileLosing(intel);
        }
        else {
            return new DefaultAnalise(intel);
        }

    }
}
