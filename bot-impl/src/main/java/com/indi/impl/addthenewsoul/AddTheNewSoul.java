package com.indi.impl.addthenewsoul;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class AddTheNewSoul implements BotServiceProvider {
    private final List<CardRank> attackCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

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
        // Quem guarda ouro é pirata 🏴‍☠️
        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira()))
                return CardToPlay.of(card);
        }

        // Forca a primeira se tiver 2 cartas de ataque
        if(intel.getRoundResults().isEmpty()){
            TrucoCard smallestAttackCard = getSmallestAttackCard(intel);
            if(smallestAttackCard != null)
                return CardToPlay.of(smallestAttackCard);
        }

        TrucoCard smallestCardCapableOfWinning = chooseSmallestCardCapableOfWinning(intel);
        if(smallestCardCapableOfWinning == null)
            if(intel.getOpponentCard().isPresent())
                return intel.getRoundResults().isEmpty() ? CardToPlay.of(getSmallestCardOnHand(intel)) : CardToPlay.discard(getSmallestCardOnHand(intel));
            else
                return CardToPlay.of(getSmallestCardOnHand(intel));

        return CardToPlay.of(smallestCardCapableOfWinning);
    }

    private TrucoCard getSmallestAttackCard(GameIntel intel){
        int countAttackCards = (int) intel.getCards().stream().filter(card -> attackCards.contains(card.getRank())).count();
        if(countAttackCards >= 2)
            return intel.getCards().stream().filter(card -> attackCards.contains(card.getRank())).min(TrucoCard::relativeValue).get();
        return null;
    }
    private TrucoCard chooseSmallestCardCapableOfWinning(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard smallestCardCapableOfWinning = null;
        TrucoCard vira = intel.getVira();

        if (opponentCard.isPresent()) {
            TrucoCard opponentCardValue = opponentCard.get();
            for (TrucoCard card : intel.getCards()) {
                if (card.relativeValue(vira) > opponentCardValue.relativeValue(vira)) {
                    if(smallestCardCapableOfWinning == null)
                        smallestCardCapableOfWinning = card;
                    else if(card.relativeValue(vira) < smallestCardCapableOfWinning.relativeValue(vira))
                        smallestCardCapableOfWinning = card;
                }
            }
        }

        return smallestCardCapableOfWinning;
    }

    private TrucoCard getSmallestCardOnHand(GameIntel intel) {
        TrucoCard smallestCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (smallestCard == null || card.relativeValue(intel.getVira()) < smallestCard.relativeValue(intel.getVira())) {
                smallestCard = card;
            }
        }
        return smallestCard;
    }
}
