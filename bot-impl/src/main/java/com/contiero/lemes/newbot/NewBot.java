package com.contiero.lemes.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.services.analise.AnaliseWhileLosing;
import com.contiero.lemes.newbot.services.analise.DefaultAnalise;
import com.contiero.lemes.newbot.services.utils.PowerCalculatorService;
import static com.contiero.lemes.newbot.interfaces.Analise.HandStatus.*;

import java.util.List;




public class NewBot implements BotServiceProvider {
    private Analise.HandStatus status;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand(intel);
        if (status == GOD) return true;
        if (status == GOOD) {
            int scoreDistance = intel.getScore() - intel.getOpponentScore();
            if (scoreDistance >= 4) return true;
            return PowerCalculatorService.powerOfCard(intel, 0) >= 9;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand(intel);
        if (status == GOD && myCards.size() <= 2) return true;
        return status == GOOD && myCards.size() == 2 && PowerCalculatorService.powerOfCard(intel, 1) >= 8;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand(intel);

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;
        Analise analise = createAnaliseInstance(intel);
        status = analise.myHand(intel);
        if (status == GOD) return 1;
        if (status == GOOD && scoreDistance <= -6) {
            return 1;
        }
        if (status == BAD || (status == MEDIUM && scoreDistance <= -4)) return -1;
        return 0;
    }

    private Analise createAnaliseInstance(GameIntel intel) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;

        if (oppScore > myScore && scoreDistance < -6) {
            return new AnaliseWhileLosing(intel);
        } else {
            return new DefaultAnalise(intel);
        }

    }
}
