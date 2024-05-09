package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bueno.spi.model.GameIntel.*;

public class JokerBot implements BotServiceProvider {

    private HandState currentRound;

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
    //    return switch (getRoundNumber(intel)) {
    //        case 1 -> new RoundOne(intel).chooseCard(intel);
    //        case 2 -> new RoundTwo(intel).chooseCard(intel);
    //        case 3 -> new RoundThree(intel).chooseCard(intel);
    //        default -> CardToPlay.of(intel.getCards().get(0));
    //    };

            List<TrucoCard> myCardsSorted = getMyCardsSorted(intel);

            //returns the card it has
            if (myCardsSorted.size() == 1) return CardToPlay.of(myCardsSorted.get(0));

            //choose the biggest card
            if (isFirstToPlay(intel) && isStartOfRound(intel)) return CardToPlay.of(myCardsSorted.get(0));

            //conditions to implement:
            //choose an equal card
            //choose the smallest card
            //choose the smallest of the biggest
            //discard

            //after implement remove this \/
            return CardToPlay.of(intel.getCards().get(0));
    }




    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    //vou comentar este método, pois implementei sem o +1, depois altero
//    int getRoundNumber (GameIntel intel) {
//        return intel.getRoundResults().size() + 1;
//    }

    //ctrl+v enorme:

    //Transform these methods in to functions and add to JokerBotUtils

    private List<TrucoCard> getMyCardsSorted(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        if (cards.size() <= 1) return cards;
        TrucoCard vira = intel.getVira();
        return cards.stream()
                .sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .collect(Collectors.toList());
    }
    private long numberOfManilhas(List<TrucoCard> trucoCards, TrucoCard vira){
        return trucoCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    private int getRoundNumber(GameIntel intel){
        return intel.getRoundResults().size();
    }

    private int getNumberOfCardsPlayed(GameIntel intel){
        return intel.getOpenCards().size() - 1;
    }

    private boolean isFirstToPlay(GameIntel intel){
        return getNumberOfCardsPlayed(intel) == 0;
    }

    private boolean isStartOfRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }

    private boolean isTied(GameIntel intel){
        List<RoundResult> roundResults = intel.getRoundResults();
        int roundNumber = getRoundNumber(intel);
        if (roundNumber == 0 && roundResults.isEmpty()) return false;
        //adicionar condições de empate
        return true;
    }
    //private boolean isTied(GameIntel intel){
    //    int numberOfResults = intel.getRoundResults().size();
    //    int roundNumber = getRoundNumber(intel);
    //    if (numberOfResults == 0 && roundNumber == 0) return false;
    //    //adicionar outras condições
    //    return true;
    //}

}
