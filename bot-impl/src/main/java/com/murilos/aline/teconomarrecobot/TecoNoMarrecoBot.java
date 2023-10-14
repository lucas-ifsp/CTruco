package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class TecoNoMarrecoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        if(possuiCasalMaior(intel)){
            return true;
        }
        if(possuiMaoTres(intel)){
            return true;
        }
        if(manilhaCount(cards, cardVira) == 2){
            return true;
        }
        if(valueOfTheHand(intel) > 20){
            return true;
        }
        if(intel.getOpponentScore() < 4){
            return true;
        }
        return false;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if((intel.getRoundResults().size() >= 1) && valueOfTheHand(intel) >= 15){
            return true;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        Integer manilhas = manilhaCount(cards,intel.getVira());
        switch (manilhas) {
            case 1:
                return CardToPlay.of(cards.get(0));
            case 2:
                return CardToPlay.of(cards.get(1));
            case 3:
                return CardToPlay.of(cards.get(2));
            default:
                if (intel.getOpponentCard().isPresent()) {
                    return CardToPlay.of(killCard(intel));
                }else{
                    // força o pé
                    return CardToPlay.of(strongCard(intel));
                }
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {return 0;}

    private boolean possuiCasalMaior(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard cardVira = intel.getVira();

        for(int i = 0; i < 3; i++){
            if (cards.get(i).isZap(cardVira)) {
                for (int k = 0; k < 3; k++) {
                    if (cards.get(k).isCopas(cardVira)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean possuiMaoTres(GameIntel intel){
        Integer contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.getRank().value() == 10){
                contador += 1;
            }
        }
        if(contador == 3){
            return true;
        }
        return  false;
    }

    private int manilhaCount(List<TrucoCard> cards, TrucoCard vira){
        int manilhaCount = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                manilhaCount++;
            }
        }
        return manilhaCount;
    }

    private int valueOfTheHand(GameIntel intel){
        Integer hand = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isManilha(intel.getVira())){
                hand += card.relativeValue(intel.getVira());
            }else{
                hand += card.getRank().value();
            }
        }
        return hand;
    }

    private TrucoCard strongCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        Integer maior = 0;
        Integer indexMaior = -1;
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() > maior) {
                maior = card.getRank().value();
                indexMaior++;
            }
        }
        return cards.get(indexMaior);
    }

    private TrucoCard weakestCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        Integer menor = Integer.MAX_VALUE;;
        Integer indexMenor = -1;
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() < menor) {
                menor = card.getRank().value();
                indexMenor++;
            }
        }
        return cards.get(indexMenor);

    }

    private TrucoCard killCard(GameIntel intel){
        TrucoCard cardPlay = null;
        List<TrucoCard> strongerCards = strongestCardsInTheHand(intel);
        if (!strongerCards.isEmpty()){
            cardPlay = strongerCards.get(0);
            for (TrucoCard card : strongerCards){
                if (card.getRank().value() < cardPlay.getRank().value()){
                    cardPlay=card;
                }
            }
        }else{
            cardPlay = weakestCard(intel);
        }
        return cardPlay;
    }
    private List<TrucoCard> strongestCardsInTheHand(GameIntel intel){
        List<TrucoCard> strongerCards = new ArrayList<>();
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() > intel.getOpponentCard().get().getRank().value()) {
                strongerCards.add(card);
            }
        }
        return strongerCards;
    }








}
