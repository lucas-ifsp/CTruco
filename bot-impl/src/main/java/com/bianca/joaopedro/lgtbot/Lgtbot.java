package com.bianca.joaopedro.lgtbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Lgtbot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentScore() < 7 && getStrongerCards(intel, CardRank.JACK).size() == 3 &&
                !getManilhas(intel).isEmpty()) {
            return true;
        }
        if (intel.getOpponentScore() < 11 && getStrongerCards(intel, CardRank.ACE).size() == 3 &&
                !getManilhas(intel).isEmpty()){
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

        if (myScore >= 9 || (myScore - opponentScore) > 6)  {
            if (okCards.size() >= 2 || !manilhas.isEmpty()) {
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
                if (!manilhas.isEmpty() && !strongCards.isEmpty()) {
                    return true; // Pedir truco
                }
            }
            if (round == 2) {
                if (didIWinFirstRound(intel) && !strongCards.isEmpty()) {
                    return true; // Pedir truco
                }
                if (!didIWinFirstRound(intel) && strongCards.size() >= 2) {
                    return true; // Pedir truco
                }
            }
            if (round == 3) {
                if (!strongCards.isEmpty() || !manilhas.isEmpty()) {
                    return true; // Pedir truco
                }
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = getRoundNumber(intel);
        List<TrucoCard> strongCards = getStrongerCards(intel, CardRank.KING);
        List<TrucoCard> okCards = getStrongerCards(intel, CardRank.QUEEN);
        List<TrucoCard> badCards = getHorrifyingCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);

        //goodCards (manilha até J)
        //horrifyngCards (4 à Q)
        int goodCardsCount = strongCards.size() + okCards.size() + manilhas.size();
        int badCardsCount = badCards.size();
        TrucoCard theBestCard = getTheBestCard(intel);

        //---------------------------------------------------------
        if (round == 1) {
            if(isFirstToPlay(intel)){
                if (goodCardsCount == 2 && badCardsCount == 1) {
                    TrucoCard weakestCard = getWeakCard(badCards);
                    return CardToPlay.of(weakestCard);
                }
                if (goodCardsCount >= 1){
                    return CardToPlay.of(theBestCard);
                }
                if (badCardsCount == 3){
                    return CardToPlay.of(theBestCard);
                }
            }
            else{
                // condição ainda
            }
        }
        if (round == 2){

        }


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

    public List<TrucoCard> getHorrifyingCards(GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> card.getRank() == CardRank.FOUR ||
                        card.getRank() == CardRank.FIVE ||
                        card.getRank() == CardRank.SIX ||
                        card.getRank() == CardRank.SEVEN ||
                        card.getRank() == CardRank.QUEEN)
                .collect(Collectors.toList());
    }

    private boolean isFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    private TrucoCard getTheBestCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        if (cards.isEmpty()) {
            return null;
        }
        cards.sort((card1, card2) -> card2.getRank().compareTo(card1.getRank()));
        return cards.get(0);
    }


    private TrucoCard getWeakCard(List<TrucoCard> cards) {
        cards.sort(Comparator.comparing(TrucoCard::getRank));
        return cards.get(0);
    }
}
