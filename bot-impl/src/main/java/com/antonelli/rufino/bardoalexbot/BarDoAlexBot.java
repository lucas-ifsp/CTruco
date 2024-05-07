package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import java.util.List;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class BarDoAlexBot {

    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        long manilhasCount = cards.stream().filter(card -> card.isManilha(vira)).count();
        return manilhasCount >= 3;
    }

    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        return cards.get(cards.size() - 1);
    }

    public int getRaiseResponse(GameIntel intel) {
        int botScore = intel.getBotScore();
        int opponentScore = intel.getOpponentScore();

        if (botScore == 11) {
            return -1;
        }

        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        if (botScore == 0 && opponentScore == 11) {
            if (!hasStrongHandCards(cards)) {
                return 1;
            }
        }

        long manilhasCount = cards.stream().filter(card -> card.isManilha(vira)).count();

        if (manilhasCount >= 3) {
            return 1;
        }

        return -1;


}

}