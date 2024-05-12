package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import com.luna.jundi.jokerBot.utils.CardUtils;

import java.util.List;

import static com.luna.jundi.jokerBot.HandClassification.EXCELLENT;
import static com.luna.jundi.jokerBot.utils.CardUtils.*;
import static com.luna.jundi.jokerBot.utils.HandUtils.*;
import static com.luna.jundi.jokerBot.utils.RoundUtils.isStartOfRound;

public final class JokerBot implements BotServiceProvider {

    //private HandState currentRound;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (averageRelativeValueOfCards(intel) >= 8) return true;
        if (averageRelativeValueOfCards(intel) >= 6 && getNumberOfManilhas(getMyCardsSorted(intel), intel.getVira()) >0 ) return true;
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return canRaiseHand(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
//            return switch (getRoundNumber(intel)) {
//                case 1 -> new RoundOne(intel).chooseCard(intel);
//                case 2 -> new RoundTwo(intel).chooseCard(intel);
//                case 3 -> new RoundThree(intel).chooseCard(intel);
//                default -> CardToPlay.of(intel.getCards().get(0));
//            };
        List<TrucoCard> myCardsSorted = getMyCardsSorted(intel);

        //returns the card it has
        if (myCardsSorted.size() == 1)
            return CardToPlay.of(myCardsSorted.get(0));

        //choose the biggest card
        //quando é o primeiro a jogar e é o inicio do round
        if (isFirstToPlay(intel) && isStartOfRound(intel))
            return getBestCard(intel);
        //quando é o primeiro a jogar e não está no começo do round
        if (isFirstToPlay(intel) && !isStartOfRound(intel) && isTiedHand(intel))
            return getBestCard(intel);

        //choose an equal card
        //quando NÃO é o primeiro E NÃO tem uma carta maior E tem uma carta igual
        if (!isFirstToPlay(intel) && !hasABiggerCardInHand(intel) && hasTheSameCardInHand(intel))
            return getTheSameCardInHand(intel);

        //choose the smallest card
        //quando NÃO é o primeiro E NÃO tem uma carta maior E NÃO tem uma carta igual
        if (!isFirstToPlay(intel) && !hasABiggerCardInHand(intel) && !hasTheSameCardInHand(intel))
            return getWorstCard(intel);

        //choose the smallest of the biggest (medium)
        //quando NÃO é o primeiro a jogar E tem uma carta maior E NÃO esta empatado/melado/meladão? (não tenho ideia do que é meladão)
        if (!isFirstToPlay(intel) && hasABiggerCardInHand(intel) && !isTiedHand(intel))
            return CardUtils.getMediumCard(intel);

        return CardToPlay.of(myCardsSorted.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // adicionar exception de quando o valor da mão for maior que 12

        //raise conditions
        if (EXCELLENT.equals(getHandClassification(intel))) return 1;

        //accept conditions
        if (!isLoosingHand(intel) && averageRelativeValueOfCards(intel) >= 7) return 0;
        if (isFirstToPlay(intel) && !isLoosingHand(intel)) return 0;

        //run conditions
        if (isStartOfRound(intel) && getNumberOfManilhas(getMyCardsSorted(intel), intel.getVira()) == 0) return -1;
        if (!isFirstToPlay(intel) && isTiedHand(intel) && averageRelativeValueOfCards(intel) < 7) return -1;
        if (isFirstToPlay(intel) && !isLoosingHand(intel)) return -1;

        return 0;
    }

}
