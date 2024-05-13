package com.gabriel.kayky.coisaruim;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.stream.Stream;

public class FirstRound implements GameStrategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> ourCards=intel.getCards();
        if(intel.getOpponentCard().isPresent()){
            TrucoCard opponentCard=intel.getOpponentCard().get();
            if(ourCards.stream().filter(e->e.compareValueTo(opponentCard,intel.getVira())>=0).count()>=2){
                return 0;
            }
        }
        if(ourCards.stream().anyMatch(e->e.isZap(intel.getVira())) && ourCards
                .stream().anyMatch(e->e.isManilha(intel.getVira()) && !e.isZap(intel.getVira())))
            return 1;
        if(ourCards.stream().anyMatch(e -> e.isManilha(intel.getVira()))){
            return 0;
        }
        if(ourCards.stream().filter(e->e.relativeValue(intel.getVira())>=8).count()>=2){
            return 0;
        }
        return  -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore()<9){
            return intel.getCards().stream().anyMatch(e -> e.isManilha(intel.getVira()));
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getCards().stream().filter(e->e.isManilha(intel.getVira())).count()>=2){
            return true;
        }
        if(intel.getOpponentCard().isPresent()){
            if(intel.getCards().stream().anyMatch(e->e.compareValueTo(intel
                    .getOpponentCard().get(),intel.getVira())>0) || intel.getCards()
                    .stream().filter(e->e.relativeValue(intel.getVira())>7).count()>=2){
                return true;
            }
            return intel.getCards().stream().filter(e -> e.compareValueTo(intel.getOpponentCard()
                    .get(), intel.getVira()) >= 0).count() >= 2 && intel.getCards()
                    .stream().anyMatch(e -> e.relativeValue(intel.getVira()) >= 8);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard strongest=intel.getCards().get(0);
        TrucoCard mid=intel.getCards().get(1);
        TrucoCard weakest=intel.getCards().get(2);

        List<TrucoCard> sortedCards = Stream.of(strongest, mid, weakest)
                .sorted((c1, c2) -> Integer.compare(c2.relativeValue(intel.getVira()),
                        c1.relativeValue(intel.getVira())))
                .toList();

        strongest = sortedCards.get(0);
        mid = sortedCards.get(1);
        weakest = sortedCards.get(2);

        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            if (intel.getScore() < 6 && intel.getOpponentScore() > 9) {
                if (intel.getCards().stream().filter(e -> e.relativeValue(
                        intel.getVira()) >= 8).count() >= 2) {

                    return CardToPlay.of(mid);
                }
            }
            if (weakest.relativeValue(intel.getVira()) > opponentCard.relativeValue(
                    intel.getVira())) {
                return CardToPlay.of(weakest);
            }
            if (mid.relativeValue(intel.getVira()) > opponentCard.relativeValue(
                    intel.getVira())) {
                return CardToPlay.of(mid);
            }
            if (strongest.relativeValue(intel.getVira()) >= opponentCard.relativeValue(
                    intel.getVira())) {
                return CardToPlay.of(strongest);
            }
        } else {
            if (strongest.isManilha(intel.getVira())&& !strongest.isZap(
                    intel.getVira())) {
                return CardToPlay.of(strongest);
            }
            if(weakest.isManilha(intel.getVira())){
                return CardToPlay.of(weakest);
            }
            return CardToPlay.of(strongest);
        }
        return CardToPlay.of(weakest);
    }
}