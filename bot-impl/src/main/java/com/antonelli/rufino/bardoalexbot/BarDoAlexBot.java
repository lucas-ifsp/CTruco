package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import java.util.List;
import java.util.Optional;

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
        int drew_index = intel.getRoundResults().lastIndexOf(GameIntel.RoundResult.DREW);
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (drew_index == 0 || drew_index == 1){
            return CardToPlay.of(getStrongestCard(cards, vira));
        }
        Optional<TrucoCard> oponnentCard = intel.getOpponentCard();
        if (oponnentCard.isPresent()){
            List<TrucoCard> cards_that_can_win = new java.util.ArrayList<>(List.of());
            for (TrucoCard card :cards){
                if (card.compareValueTo(oponnentCard.get(),vira) > 0)
                    cards_that_can_win.add(card);
            }
            if (!cards_that_can_win.isEmpty()){
                return CardToPlay.of(getWeakestCard(cards_that_can_win,vira));
            }else {
                return CardToPlay.of(getWeakestCard(cards,vira));
            }
        }
        return CardToPlay.of(getStrongestCard(cards,vira));
    }


    public TrucoCard getStrongestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard strongest = cards.get(0);

        for (TrucoCard card : cards){
            if (card.compareValueTo(strongest,vira) > 0)
                strongest = card;
        }
        return strongest;
    }

    public TrucoCard getWeakestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard weakest = cards.get(0);

        for (TrucoCard card : cards){
            if (card.compareValueTo(weakest,vira) < 0)
                weakest = card;
        }
        return weakest;
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
