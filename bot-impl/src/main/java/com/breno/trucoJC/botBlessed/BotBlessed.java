package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class BotBlessed implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();
        int playerScore = intel.getScore();

        long manilhaCount = hand.stream().filter(c -> c.isManilha(vira)).count();
        boolean hasZap = hand.stream().anyMatch(c -> c.isZap(vira));
        boolean hasCopas = hand.stream().anyMatch(c -> c.isCopas(vira));
        boolean hasThree = hand.stream().anyMatch(c -> c.getRank() == CardRank.THREE);
        boolean hasStrongManilha = hand.stream().anyMatch(c -> c.isManilha(vira) && !c.isZap(vira) && !c.isCopas(vira));

        boolean scoreDiffOk = Math.abs(playerScore - opponentScore) <= 5;
        boolean isMaoDeOnze = playerScore == 11 || opponentScore == 11;

        if (!isMaoDeOnze) return false;

        if (scoreDiffOk && hasStrongManilha && hasThree) return true;

        if (manilhaCount >= 2) return true;

        if ((hasZap || hasCopas) && hasThree) return true;

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
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        int round = intel.getRoundResults().size();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        List<TrucoCard> sortedHand = hand.stream().sorted((c1, c2) -> c2.compareValueTo(c1, vira)).toList();
        TrucoCard strongest = sortedHand.get(0);
        TrucoCard weakest = sortedHand.get(sortedHand.size() - 1);

        if (round == 0) {
            if (opponentCard.isPresent()) {
                TrucoCard opponentPlay = opponentCard.get();
                if (opponentPlay.isManilha(vira)) {
                    boolean hasStrongerManilha = hand.stream().anyMatch(c -> c.isManilha(vira) && c.compareValueTo(opponentPlay, vira) > 0);
                    return hasStrongerManilha ? CardToPlay.of(strongest) : CardToPlay.of(weakest);
                }
            }
            return CardToPlay.of(strongest);
        }

        if (round == 1) {
            return CardToPlay.of(weakest);
        }

        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean hasStrongCard = hand.stream().anyMatch(c -> c.isManilha(vira) || c.isZap(vira));
        long threes = hand.stream().filter(c -> c.getRank() == CardRank.THREE).count();

        if (hasStrongCard || threes >= 2) {
            return 1;
        }

        if (intel.getOpponentScore() >= 11 && intel.getScore() < 5) {
            return -1;
        }

        return -1;
    }

    @Override
    public String getName() { return "BotBlessedByJC"; }

}
