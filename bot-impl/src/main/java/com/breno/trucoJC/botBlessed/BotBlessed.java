package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class BotBlessed implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();
        int playerScore = intel.getScore();

        boolean hasThree = hand.stream().anyMatch(c -> c.getRank() == CardRank.THREE);
        boolean hasManilha = hand.stream().anyMatch(c -> c.isManilha(vira));
        long threeCount = hand.stream().filter(c -> c.getRank() == CardRank.THREE).count();
        boolean hasZap = hand.stream().anyMatch(c -> c.isZap(vira));

        if (playerScore == 11 || opponentScore == 11) {
            // Responder agressivamente se o jogador tiver uma manilha, Zap, ou 2 ou mais 3s
            if (Math.abs(playerScore - opponentScore) <= 5 && (hasManilha || hasZap || threeCount >= 2)) {
                return true;
            }

            // Resposta conservadora, se o bot estiver em desvantagem ou tiver poucas chances de vencer
            if (opponentScore >= 11 && playerScore < 5) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        boolean hasManilha = intel.getCards().stream().anyMatch(c -> c.isManilha(intel.getVira()));
        boolean hasZap = intel.getCards().stream().anyMatch(c -> c.isZap(intel.getVira()));
        long threes = intel.getCards().stream().filter(c -> c.getRank() == CardRank.THREE).count();

        if (hasManilha || hasZap || threes >= 2) {
            return true;
        }

        if (intel.getOpponentScore() >= 11 && intel.getScore() < 5) {
            return false;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() { return "BotBlessedByJC";
    }
}
