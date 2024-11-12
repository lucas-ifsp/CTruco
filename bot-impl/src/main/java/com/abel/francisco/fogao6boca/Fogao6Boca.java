package com.abel.francisco.fogao6boca;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fogao6Boca implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return verifyHandStrengh(intel) > 6;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return firstGameRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)
            if(verifyHandStrengh(intel) > 6) return 0;
        return -1;
    }

    private CardToPlay firstGameRound(GameIntel intel){
        List<TrucoCard> botCards = sortedCardStrengh(intel.getCards(),intel.getVira());
        return decideCardToPlay(botCards, intel.getOpponentCard(), intel.getVira());
    }

    private List<TrucoCard> sortedCardStrengh(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((card1, card2) -> card2.compareValueTo(card1,vira));
        return sortedCards;
    }

    private CardToPlay decideCardToPlay(List<TrucoCard> cards, Optional<TrucoCard> oponentCard, TrucoCard vira){
        if(oponentCard.isPresent()){
            List<TrucoCard> possibleCards = new ArrayList<>();
            for(TrucoCard card : cards){
                if(card.compareValueTo(oponentCard.get(), vira) > 0)
                    possibleCards.add(card);
            }
            if(!possibleCards.isEmpty()) return CardToPlay.of(possibleCards.get(possibleCards.size()-1));

            for(TrucoCard card : cards){
                if(card.compareValueTo(oponentCard.get(), vira) == 0)
                    return CardToPlay.of(card);
            }
            return CardToPlay.of(cards.get(cards.size()-1));
        }
        return CardToPlay.of(cards.get(0));
    }

    private double verifyHandStrengh(GameIntel intel){
        double soma = 0;
        for(TrucoCard card : intel.getCards()){
            soma += card.relativeValue(intel.getVira());
        }
        return soma/intel.getCards().size();

    }

}
