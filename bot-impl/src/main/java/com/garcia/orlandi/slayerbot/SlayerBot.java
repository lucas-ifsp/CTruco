package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        List<TrucoCard> manilhas = utils.getManilhas(cards, vira);

        if(manilhas.isEmpty()){
            return false;
        }else if(manilhas.size() == 2){
            return true;
        }

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> openCards = intel.getOpenCards();
        List<GameIntel.RoundResult> roundResult = intel.getRoundResults();

        List<TrucoCard> manilhas = utils.getManilhas(cards, vira);

        if(openCards.size() == 1){
            if(!manilhas.isEmpty()){
                List<TrucoCard> cardsWithoutZap = cards.stream()
                        .filter(card -> !card.isZap(vira))
                        .toList();

                if(cardsWithoutZap.size() == 2){
                    for(TrucoCard card : cardsWithoutZap){
                        if(card.isCopas(vira) || card.isEspadilha(vira) || card.isOuros(vira)){
                            return CardToPlay.of(card);
                        }
                    }
                    TrucoCard secondStrongestCard = utils.getStrongestCard(cardsWithoutZap, vira);
                    return CardToPlay.of(secondStrongestCard);
                } else if (cardsWithoutZap.size() == 3) {
                    if(manilhas.size() == 3 || manilhas.size() ==1){
                        TrucoCard strongestManilha = utils.getStrongestCard(cards, vira);
                        TrucoCard secondStrongestManilha = utils.getSecondStrongestCard(cards, strongestManilha, vira);
                        return CardToPlay.of(secondStrongestManilha);
                    } else if(manilhas.size() == 2){
                        TrucoCard weakestManilha = utils.getWeakestCard(manilhas, vira);
                        return CardToPlay.of((weakestManilha));
                    }
                }

                TrucoCard chosenCard = cards.stream()
                        .filter(card -> !card.isZap(vira))
                        .findFirst()
                        .orElse(null);

                return CardToPlay.of(chosenCard);
            }else{
                List<TrucoCard> threes = utils.getThreesAtHand(cards);
                if(!threes.isEmpty()){
                    if(threes.size() == 2){
                        return CardToPlay.of(threes.get(0));
                    }else if(threes.size() == 1) {
                        TrucoCard secondStrongestCard = utils.getSecondStrongestCard(cards, threes.get(0), vira);
                        return CardToPlay.of(secondStrongestCard);

                    }
                }else{
                    TrucoCard strongestCard = utils.getStrongestCard(cards, vira);
                    TrucoCard secondStrongestCard = utils.getSecondStrongestCard(cards, strongestCard, vira);
                    return CardToPlay.of(secondStrongestCard);
                }
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
