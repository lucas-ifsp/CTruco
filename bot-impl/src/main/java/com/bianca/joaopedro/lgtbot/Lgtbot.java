package com.bianca.joaopedro.lgtbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class Lgtbot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentScore() < 7 && getStrongerCards(intel, CardRank.JACK).size() == 3 &&
                getManilhas(intel).size() > 0) {
            return true;
        }
        if (intel.getOpponentScore() < 11 && getStrongerCards(intel, CardRank.ACE).size() == 3 &&
                getManilhas(intel).size() > 0){
            return true;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int round = getRoundNumber(intel);
        int myScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();
        List<TrucoCard> strongCards = getStrongerCards(intel, CardRank.KING);
        List<TrucoCard> okCards = getStrongerCards(intel, CardRank.QUEEN);
        List<TrucoCard> manilhas = getManilhas(intel);

        if (myScore >= 10 || (myScore - opponentScore) > 6)  {
            if (okCards.size() >= 2 || manilhas.size() > 0) {
                return true;
            }
        }

        if (opponentScore == 11) {
            return true;
        }

        if (opponentScore != 11) {
            if (round == 1) {
                if (strongCards.size() >= 2) {
                    return true; // Pedir truco
                }
                if (manilhas.size() > 0 && strongCards.size() > 0) {
                    return true; // Pedir truco
                }
            }
            if (round == 2) {
                if (didIWinFirstRound(intel) && strongCards.size() > 0) {
                    return true; // Pedir truco
                }
                if (!didIWinFirstRound(intel) && strongCards.size() >= 2) {
                    return true; // Pedir truco
                }
            }
            if (round == 3) {
                if (strongCards.size() > 0 || manilhas.size() > 0) {
                    return true; // Pedir truco
                }
            }
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

    public List<TrucoCard> getStrongerCards(GameIntel intel, CardRank referenceRank){
        return intel.getCards().stream()
                .filter(card -> card.getRank().compareTo(referenceRank) > 0)
                .toList();
    }


    private List<TrucoCard> getManilhas(GameIntel intel) {
        TrucoCard viraCard = intel.getVira();
        return intel.getCards().stream()
                .filter(carta -> carta.isManilha(viraCard))
                .toList();
    }

    private int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    public boolean didIWinFirstRound(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (!roundResults.isEmpty()) {
            if (roundResults.get(0) == GameIntel.RoundResult.WON) {
                return true;
            }
        }
        return false;
    }
}
