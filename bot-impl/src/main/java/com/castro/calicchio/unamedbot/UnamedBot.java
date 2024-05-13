package com.castro.calicchio.unamedbot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UnamedBot implements BotServiceProvider{
    List <TrucoCard> cards = new ArrayList<>();
    TrucoCard vira;

    List<GameIntel.RoundResult> resultados = new ArrayList<>();
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        vira = intel.getVira();

        return hasStrongCards(intel);
    }


    @Override
    public boolean decideIfRaises(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if(opponentCard.isPresent()){
            if(hasHorribleHand(intel)){
                return true;
            }
        }
        return hasStrongCards(intel) || hasManilha(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        cards = intel.getCards();
        vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        resultados = intel.getRoundResults();


        int currentRound = resultados.size() + 1;
        sortHand(intel);
        TrucoCard strongestCard = cards.get(cards.size() - 1);
        TrucoCard lowestCard = cards.get(0);

        if(hasMoreThanOneManilha(intel)){
            if(currentRound == 1){
                CardToPlay.of(lowestCard);
            } else{
                CardToPlay.of(strongestCard);
            }
        }
        else{
            if (currentRound == 1) {
                if (opponentCard.isPresent()) {
                    if (lowestCard.relativeValue(vira) > opponentCard.get().relativeValue(vira)) {
                        return CardToPlay.of(lowestCard);
                    }
                    else{
                        if(cards.get(1).relativeValue(vira) > opponentCard.get().relativeValue(vira)){
                            return CardToPlay.of(cards.get(1));
                        }
                    }
                }
                else{
                    if(!strongestCard.isZap(vira) || !strongestCard.isCopas(vira)){
                        return CardToPlay.of(strongestCard);
                    }
                }
            } else if (currentRound == 2) {
                if (resultados.get(0) == GameIntel.RoundResult.WON) {
                    return CardToPlay.discard(cards.get(0));
                }
                else{
                    if(opponentCard.isPresent()){
                        if(cards.get(0).relativeValue(vira) > opponentCard.get().relativeValue(vira)){
                            return CardToPlay.of(cards.get(0));
                        }
                        else{
                            return CardToPlay.of(cards.get(1));
                        }
                    }
                    else{
                        return CardToPlay.of(cards.get(1));
                    }
                }
            }
        }

        return CardToPlay.of(cards.get(0));
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (hasStrongCards(intel) && hasManilha(intel)){
            return 1;
        }
        else{
            if(hasOneStrongCard(intel)){
                return 0;
            };
        }
        return - 1;
    }

    public void sortHand(GameIntel intel){
        TrucoCard vira = intel.getVira();
        List <TrucoCard> cards = intel.getCards();
        if (!cards.isEmpty()) {
            cards.sort(Comparator.comparingInt(card -> card.relativeValue(vira)));
        }
    }

    public boolean hasMoreThanOneManilha(GameIntel intel){
        List <TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        cards = cards.stream().filter(card -> card.isManilha(vira)).toList();
        return cards.size() == 2;
    }

    public boolean hasManilha(GameIntel intel){
        List <TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        cards = cards.stream().filter(card -> card.isManilha(vira)).toList();

        return !cards.isEmpty();
    }
    public boolean hasStrongCards(GameIntel intel){
        cards = intel.getCards();
        vira = intel.getVira();
        cards = cards.stream().filter(card -> card.isManilha(vira) || card.relativeValue(vira) >= 7).toList();
        return cards.size() >= 2;
    }

    public boolean hasOneStrongCard(GameIntel intel){
        cards = intel.getCards();
        vira = intel.getVira();
        cards = cards.stream().filter(card -> card.getRank().value() > 9).toList();

        return !cards.isEmpty();
    }
    public boolean hasHorribleHand(GameIntel intel){
        cards = intel.getCards();
        vira = intel.getVira();
        cards = cards.stream().filter(card -> card.getRank().value() > 9).toList();

        return cards.isEmpty();
    }
}
