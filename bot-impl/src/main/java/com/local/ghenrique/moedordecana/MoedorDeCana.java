package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

import static com.local.ghenrique.moedordecana.TrucoTools.*;

public class MoedorDeCana implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int numberOfManilhas = countManilha(intel);
        int enemyScore = intel.getOpponentScore();
        long highValueCardsCount = intel.getCards()
                .stream()
                .filter(card -> card.relativeValue(intel.getVira()) > 8)
                .count();
        long veryHighValueCardsCount = intel.getCards()
                .stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 9)
                .count();
        long strongCards = countStrongCards(intel);
        if (numberOfManilhas >= 2) return true;
        if (enemyScore >= 8 && strongCards == 3) return true;
        if (isPlayingSecond(intel)) {
            return (numberOfManilhas == 1 && highValueCardsCount >= 2) ||
                    (enemyScore > 8 && strongCards == 2 && veryHighValueCardsCount >= 1) ||
                    (enemyScore > 9 && strongCards == 2) ||
                    (enemyScore > 9 && strongCards == 1 && highValueCardsCount >= 2);
        } else {
            return (numberOfManilhas == 1 && highValueCardsCount >= 2) ||
                    (enemyScore > 8 && veryHighValueCardsCount >= 2) ||
                    (enemyScore > 9 && strongCards == 2) ||
                    (enemyScore > 9 && strongCards == 1 && highValueCardsCount >= 2);
        }
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).getRaiseResponse(intel);

    }

    private SuperGameStrategy getRound(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FirstRound();
            case 1 -> new SecondRound();
            case 2 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + roundNumber);
        };
    }

}
