package com.correacarini.impl.trucomachinebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

import static com.bueno.spi.model.CardRank.THREE;
import static com.bueno.spi.model.CardRank.TWO;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class TrucoMachineBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        if(hasZapAndManilha(intel)) return true;

        if(intel.getScore() - intel.getOpponentScore() > 3) {
            if(hasManilhaAndThree(intel))  return true;
            if(intel.getRoundResults().get(0).equals(WON)){
                if(hasManilhaAndTwo(intel)) return true;
            }
        }

        if(intel.getRoundResults().size() == 2 && intel.getOpponentCard().isPresent()){
            if(intel.getCards().get(0).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira  = intel.getVira();

        TrucoCard greatestCard = getGreatestCard(cards, vira);

        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0).equals(WON)){
            if(hasZap(intel)) return CardToPlay.discard(getLowestCard(intel.getCards(), vira));
        }

        if(intel.getOpponentCard().isPresent()){
            TrucoCard minimalGreaterCard = getMinimalGreaterCard(cards, vira, intel.getOpponentCard().get());
            if(minimalGreaterCard == null) {
                if (greatestCard.relativeValue(vira) == intel.getOpponentCard().get().relativeValue((vira))){
                    return CardToPlay.of(greatestCard);
                }

                TrucoCard lowestCard = getLowestCard(cards, vira);
                return CardToPlay.of(lowestCard);
            }
            return CardToPlay.of(minimalGreaterCard);
        }

        return CardToPlay.of(greatestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "Truco Machine";
    }

    private TrucoCard getGreatestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard greatestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(greatestCard, vira);
            if (comparison > 0) {
                greatestCard = card;
            }
        }

        return greatestCard;
    }

    private TrucoCard getMinimalGreaterCard( List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        TrucoCard minimalGreaterCard = null;

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(opponentCard, vira);
            if (comparison > 0) {
                if (minimalGreaterCard == null || card.compareValueTo(minimalGreaterCard, vira) < 0) {
                    minimalGreaterCard = card;
                }
            }
        }

        return minimalGreaterCard;
    }

    private TrucoCard getLowestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard lowestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(lowestCard, vira);
            if (comparison < 0) {
                lowestCard = card;
            }
        }

        return lowestCard;
    }

    private boolean hasZapAndManilha(GameIntel intel) {
        boolean zap = false;
        int manilhas = 0;

        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) {
                zap = true;
            }
            if (card.isOuros(intel.getVira()) || card.isEspadilha(intel.getVira()) || card.isCopas(intel.getVira())) {
                manilhas += 1;
            }
        }

        return zap && manilhas >= 1;
    }

    private boolean hasManilhaAndThree(GameIntel intel) {
        int manilhas = 0;
        boolean hasThree = false;

        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().equals(THREE)) {
                hasThree = true;
            }
            if (card.isOuros(intel.getVira()) || card.isEspadilha(intel.getVira()) || card.isCopas(intel.getVira()) || card.isZap(intel.getVira())) {
                manilhas += 1;
            }
        }

        return hasThree && manilhas >= 1;
    }

    private boolean hasManilhaAndTwo(GameIntel intel) {
        int manilhas = 0;
        boolean hasTwo = false;

        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().equals(TWO)) {
                hasTwo = true;
            }
            if (card.isOuros(intel.getVira()) || card.isEspadilha(intel.getVira()) || card.isCopas(intel.getVira()) || card.isZap(intel.getVira())) {
                manilhas += 1;
            }
        }
        return hasTwo && manilhas >= 1;
    }

    private boolean hasZap(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) {
                return true;
            }
        }
        return false;
    }
}
