package com.gatti.casaque.caipirasbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CaipirasBot implements BotServiceProvider {
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
        if(checkExistenceDiamondManilha(intel.getCards(),intel.getVira())){
            return CardToPlay.of(chooseDiamondInFirstRound(intel.getCards(),intel.getVira()));
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public boolean checkExistenceDiamondManilha(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.isOuros(vira)) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkExistenceManilhaAndStronger(List<TrucoCard> cards, TrucoCard vira) {
        var count = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira) || card.compareValueTo(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),vira) >= 0) {
                count++;
            }
        }
        return count >= 2;
    }
    public boolean checkExistenceCasalMaior(List<TrucoCard> openCards) {
        for (TrucoCard card : openCards) {
            if (card.isCopas(openCards.get(0)) || card.isZap(openCards.get(0))) {
                return true;
            }
        }
        return false;
    }

    public TrucoCard chooseDiamondInFirstRound(List<TrucoCard> cards, TrucoCard vira){
        for (TrucoCard card : cards) {
            if (card.isOuros(vira)) {
                return card;
            }
        }
        return null;
    }

    public TrucoCard chooseWeakInFirstRound(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard weak = null;
        for (TrucoCard card : cards) {
            if (weak == null) {
                weak = card;
            }
            if(weak.compareValueTo(card,vira) > 0){
                weak = card;
            }
        }
        return weak;
    }

    public boolean bluffWhenOpponentThirdCardIsKnown(List<GameIntel.RoundResult> roundResults, List<TrucoCard> openCards) {
        if (roundResults.size() == 2) {
            return !checkExistenceCasalMaior(openCards);
        }
        return false;
    }
}