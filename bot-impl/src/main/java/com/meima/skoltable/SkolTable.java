package com.meima.skoltable;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class SkolTable implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        boolean isFirstRound = intel.getRoundResults().isEmpty();
        boolean existsOpponentCard = intel.getOpponentCard().isPresent();

        TrucoCard vira = intel.getVira();
        TrucoCard strongestCardInHand = getStrongestCardInHand(intel, vira);
        TrucoCard weakestCardInHand = getWeakestCardInHand(intel, vira);
        TrucoCard opponentCard;

        if(isFirstRound){
            if(existsOpponentCard) {
                opponentCard = intel.getOpponentCard().get();
                if(strongestCardInHand.compareValueTo(opponentCard, vira) > 0){
                    return CardToPlay.of(strongestCardInHand);
                } else {
                    return CardToPlay.of(weakestCardInHand);
                }
            }
            return CardToPlay.of(strongestCardInHand);
        }
        List<TrucoCard> hand = intel.getCards();
        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        boolean isFirstRound = intel.getRoundResults().isEmpty();
        List<GameIntel.RoundResult> rounds = intel.getRoundResults();

        if(!isFirstRound){
            if(rounds.get(0).equals(GameIntel.RoundResult.WON)){
                return 0;
            }
        }
        return -1;
    }

    private TrucoCard getStrongestCardInHand(GameIntel intel, TrucoCard vira) {
        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira))).get();
    }

    private TrucoCard getWeakestCardInHand(GameIntel intel, TrucoCard vira) {
        List<TrucoCard> cards = intel.getCards();

        return cards.stream().min(Comparator.comparingInt(card -> card.relativeValue(vira))).get();
    }

}