package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

public class FernasSecondHand implements FernasStrategy {
    public static final int HAND_MAX_VALUE = 25;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<RoundResult> roundResults = intel.getRoundResults();
        int handValue = getHandValue(intel);
        if (roundResults.get(0) == RoundResult.WON){
            return (handValue >= HAND_MAX_VALUE * 0.6 && getAmountManilhas(intel) == 0) || getAmountManilhas(intel) >= 1;
        }
        if (getAmountManilhas(intel) >= 1) return true;
        return handValue >= HAND_MAX_VALUE * 0.68 && getAmountManilhas(intel) == 0;
//        if (roundResults.get(0) == RoundResult.WON){
//            return handValue >= HAND_MAX_VALUE * 0.52;
////            return true;
//        }
//        if (getAmountManilhas(intel) >= 1) return true;
//        return (handValue >= HAND_MAX_VALUE * 0.72 && getAmountManilhas(intel) == 0) ||
//                handValue >= HAND_MAX_VALUE * 0.68 && getAmountManilhas(intel) >= 1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = getCurrentCards(intel);

        RoundResult roundResult = intel.getRoundResults().get(0);

        if (roundResult == RoundResult.DREW) return CardToPlay.of(cards.get(1));

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()) {
            Optional<TrucoCard> menorCarta = getMenorCarta(intel);
            return menorCarta.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(cards.get(0)));
        }

        if (getAmountManilhas(intel) >= 1){
            return CardToPlay.of(cards.get(1));
        }
        List<TrucoCard> cartasTres = getTres(cards);
        if (cartasTres.isEmpty()) return CardToPlay.of(cards.get(0));
        else if (getAmountManilhas(intel) == 1){
            Optional<TrucoCard> manilha = getManilha(intel);
            if (manilha.isPresent()) return CardToPlay.of(manilha.get());
        }
        return CardToPlay.of(cartasTres.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handValue = getHandValue(intel);
        List<RoundResult> roundResults = intel.getRoundResults();
        if (roundResults.get(0) == RoundResult.WON) {
            if (handValue >= HAND_MAX_VALUE * 0.56 || getAmountManilhas(intel) >= 1) return 1;
            if (handValue >= HAND_MAX_VALUE * 0.60 && getAmountManilhas(intel) == 0) return 0;
        }
        if ((handValue >= HAND_MAX_VALUE * 0.64 && getAmountManilhas(intel) == 0) || getAmountManilhas(intel) >= 1) return 0;
        return -1;
    }
}
