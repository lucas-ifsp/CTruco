package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import java.util.List;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class BarDoAlexBot implements BotServiceProvider {

    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        long manilhasCount = cards.stream().filter(card -> card.isManilha(vira)).count();
        return manilhasCount >= 2;
    }

    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard chosenCard = cards.get(cards.size() - 1);
        return CardToPlay.of(chosenCard);
    }

    public int getRaiseResponse(GameIntel intel) {
        int botScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();

        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        if (botScore == 11) {
            return -1;
        }

        if (botScore == 0 && opponentScore == 11) {
            if (!hasStrongHandCards(cards)) {
                return 1;
            }
        }

        long manilhasCount = cards.stream().filter(card -> card.isManilha(vira)).count();

        if (manilhasCount >= 2) {
            return 1;
        }

        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON || manilhasCount >= 1)
            return 0;

        return -1;
    }

    private boolean hasStrongHandCards(List<TrucoCard> cards) {
        for (TrucoCard card : cards) {
            if (card.getRank().value() >= 10) {
                return true;
            }
        }
        return false;
    }

    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int botScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();

        if (botScore == 0 && opponentScore == 11) {
            return !hasStrongHandCards(intel.getCards());
        }

        return false;
    }
}
