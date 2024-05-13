package com.castro.calicchio.unamedbot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UnamedBot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int totalPoints = intel.getScore();

        return totalPoints == 11;
    }


    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        sortHand(intel);

        int sum = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();
        
        long highValueCards = cards.stream().filter(card -> card.relativeValue(vira) > 9).count();
        
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int currentRound = roundResults.size();
        
        int raiseThreshold = 25;
        
        raiseThreshold += currentRound * 5;
        raiseThreshold += highValueCards * 2;
        
        return sum > raiseThreshold;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int currentRound = roundResults.size() + 1;

        sortHand(intel);
        TrucoCard strongestCard = cards.get(cards.size() - 1);

        if(currentRound == 1){
            if(opponentCard.isPresent()){
                if(strongestCard.relativeValue(vira) > opponentCard.get().relativeValue(vira)){
                    return CardToPlay.of(strongestCard);
                }
                else{
                    return CardToPlay.discard(cards.get(0));
                }
            }
            else{
                if(strongestCard.relativeValue(vira) >= 8 && !strongestCard.isZap(vira)){
                    return CardToPlay.of(strongestCard);
                }
                else{
                    return CardToPlay.discard(cards.get(0));
                }
            }
        }


        return CardToPlay.of(strongestCard);
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        sortHand(intel);
        int sum = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();
        if (sum > 20) {
            return 1;
        } else if (sum >= 10) {
            return 0;
        } else {
            return -1;
        }
    }

    public void sortHand(GameIntel intel){
        TrucoCard vira = intel.getVira();
        List <TrucoCard> cards = intel.getCards();
        if (!cards.isEmpty()) {
            cards.sort(Comparator.comparingInt(card -> card.relativeValue(vira)));
        }
        else{
            throw new IllegalStateException("Sem cartas por enquanto!");
        }
    }
}
