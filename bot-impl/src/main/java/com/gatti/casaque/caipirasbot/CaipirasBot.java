package com.gatti.casaque.caipirasbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class CaipirasBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return playMaoDeOnzeCondition(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        var raise = false;
        raise = checkRaiseInFirstRound(intel.getCards(),intel.getVira());
        return raise;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(checkExistenceDiamondManilha(intel.getCards(),intel.getVira())){
            return CardToPlay.of(chooseDiamondInFirstRound(intel.getCards(),intel.getVira()));
        }
        if(checkExistenceManilhaAndStronger(intel.getCards(),intel.getVira())){
            return CardToPlay.of(chooseWeakInFirstRound(intel.getCards(),intel.getVira()));
        }
        if(checkExistenceSpadesManilha(intel.getCards(), intel.getVira())){
            return CardToPlay.of(chooseSpadesInFirstRound(intel));
        }
        TrucoCard middle = chooseMiddleCart(intel.getCards(), intel.getVira(), intel.getRoundResults());
        if(middle!=null){
            return CardToPlay.of(middle);
        }
        return CardToPlay.of(tryToKillOpponentCard(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (acceptRaiseByOpponentIfFirstRoundWon(intel)) {
            return 0;
        }
        return -1;
    }

    public TrucoCard tryToKillOpponentCard(GameIntel intel) {
        TrucoCard cardToPlay = chooseWeakInFirstRound(intel.getCards(), intel.getVira());
        if (checkEnemyIsFirstPLayer(intel.getOpponentCard())) {
            for (TrucoCard card : intel.getCards()) {
                if (card.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0) {
                    if (card.compareValueTo(cardToPlay, intel.getVira()) < 0) {
                        return card;
                    }
                }
            }
        }
        return cardToPlay;
    }

    public Boolean acceptRaiseByOpponentIfFirstRoundWon(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) {
            if (intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)) {
                if (checkExistenceManilhaAndStronger(intel.getCards(), intel.getVira())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean playMaoDeOnzeCondition(GameIntel intel) {
        var count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                count += 2;
            }
            if (card.compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), intel.getVira()) >= 0) {
                count++;
            }
        }
        return count >= 3;
    }

    public boolean checkRaiseInFirstRound(List<TrucoCard> cards, TrucoCard vira){
        var count = 0;
        var hasTwo = false;
        for (TrucoCard card : cards) {
            if(card.compareValueTo(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), vira) > 0){
                count++;
                if(card.compareValueTo(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), vira) == 0){
                    hasTwo = true;
                }
                if(card.compareValueTo(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), vira) == 0 && hasTwo && !card.isManilha(vira)){
                    count--;
                }
            }
        }
        return count>=2;
    }

    public Boolean checkEnemyIsFirstPLayer(Optional<TrucoCard> enemyCard){
        return enemyCard.isPresent();
    }

    public Boolean checkOnlyZap(List<TrucoCard> cards, TrucoCard vira){
        var count = 0;
        for (TrucoCard card : cards) {
            if (card.isZap(vira)) {
                count++;
            }
            if(card.compareValueTo(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), vira) > 0){
                count++;
            }
        }
        return count <= 1;
    }

    public boolean checkExistenceDiamondManilha(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.isOuros(vira)) {
                return true;
            }
        }
        return false;
    }

    public TrucoCard chooseMiddleCart(List<TrucoCard> cards, TrucoCard vira, List<GameIntel.RoundResult> roundResults){
        TrucoCard weak = chooseWeakInFirstRound(cards, vira);
        TrucoCard middle = null;
        TrucoCard strong = null;

        for (TrucoCard card : cards) {
            if (strong == null && card.compareValueTo(weak,vira) > 0) {
                strong = card;
            }
            if(strong != null && card.compareValueTo(strong,vira) > 0){
                middle = strong;
                strong = card;
            }else{
                if(card.compareValueTo(weak,vira) != 0){
                    middle = card;
                }
            }
        }

        if(roundResults.isEmpty()){
            return middle;
        }

        return null;
    }

    public boolean checkExistenceSpadesManilha(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.isEspadilha(vira)) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkExistenceManilhaAndStronger(List<TrucoCard> cards, TrucoCard vira) {
        var count = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                if (card.isZap(vira) || card.isCopas(vira)) {
                    count++;
                }
            }
            else {
                if (card.compareValueTo(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),vira) >= 0 || card.compareValueTo(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),vira) > 0) {
                    count++;
                }
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

    public TrucoCard chooseSpadesInFirstRound(GameIntel intel){
        var count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isEspadilha(intel.getVira()) || card.compareValueTo(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),intel.getVira()) >= 0) {
                count++;
            }
        }
        if (count == 3) {
            for (TrucoCard card : intel.getCards()) {
                if (card.isEspadilha(intel.getVira())) {
                    return card;
                }
            }
        }
        return tryToKillOpponentCard(intel);
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