package com.local.abel.francisco.fogao6boca;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class Fogao6BocaUtils {

    protected CardToPlay decideCardToPlay(List<TrucoCard> cards, Optional<TrucoCard> oponentCard, TrucoCard vira, GameIntel intel){
        if(oponentCard.isPresent()){
            List<TrucoCard> possibleCards = new ArrayList<>();
            possibleCards.addAll(cards.stream()
                    .filter(card -> card.compareValueTo(oponentCard.get(), vira) > 0)
                    .toList());
            if(!possibleCards.isEmpty()) return CardToPlay.of(possibleCards.get(possibleCards.size()-1));

            return cards.stream()
                    .filter(card -> card.compareValueTo(oponentCard.get(),vira) == 0)
                    .findFirst()
                    .map(CardToPlay::of)
                    .orElse(CardToPlay.of(cards.get(cards.size()-1)));

        }

        if(intel.getRoundResults().isEmpty() && qtdManilhas(intel) >= 2) return CardToPlay.of(cards.get(cards.size()-1));

        if(intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == WON){
            return CardToPlay.of(cards.get(cards.size()-1));
        }
        return CardToPlay.of(cards.get(0));
    }

    protected CardToPlay gameRound(GameIntel intel){
        List<TrucoCard> botCards = sortedCardStrengh(intel.getCards(),intel.getVira());
        return decideCardToPlay(botCards, intel.getOpponentCard(), intel.getVira(), intel);
    }

    protected List<TrucoCard> sortedCardStrengh(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((card1, card2) -> card2.compareValueTo(card1,vira));
        return sortedCards;
    }

    protected double verifyElevenHandStrengh(GameIntel intel){
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) > 6)
                .count();
    }


    protected int bluffVerify(GameIntel intel){
        if(intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == LOST){
            boolean someGreaterCard = intel.getCards().stream().anyMatch(card -> card.compareValueTo(intel.getOpenCards().get(1), intel.getVira()) > 0
                    && card.compareValueTo(intel.getOpenCards().get(2), intel.getVira()) > 0);
            return someGreaterCard ? 0 : -1;
        }
        if(intel.getRoundResults().isEmpty() && verifyHandStrengh(intel) > 6){
            return 0;
        }
        return -1;

    }

    protected double verifyHandStrengh(GameIntel intel){
        return intel.getCards().stream()
                .mapToDouble(card -> card.relativeValue(intel.getVira()))
                .average()
                .orElse(0);
    }

    protected int qtdManilhas(GameIntel intel){
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }


    protected boolean casalMaior(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        boolean zap = cards.stream().anyMatch(card -> card.isZap(intel.getVira()));
        boolean copas = cards.stream().anyMatch(card -> card.isCopas(intel.getVira()));

        return zap && copas;
    }

    protected int qtdThree(GameIntel intel){
        return (int) intel.getCards().stream()
                .filter(card -> card.getRank().equals(CardRank.THREE))
                .count();
    }
}