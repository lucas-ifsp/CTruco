package com.Sigoli.Castro.PatoBot;

import java.util.List;
import java.util.Optional;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class PatoBot implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return checkIfAcceptMaoDeOnze(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        CardToPlay cardToPlay = null;
        if(getNumberOfCardsInHand(intel) == 3 && checkIfOpponentIsFirstToPlay(intel.getOpponentCard())){
            cardToPlay = CardToPlay.of(attemptToBeatOpponentCard(intel));
        }
        else if(getNumberOfCardsInHand(intel)== 3 && !checkIfOpponentIsFirstToPlay(intel.getOpponentCard())) {
            cardToPlay = CardToPlay.of(selectStrongerCardExcludingZapAndCopas(intel));
        } else if (getNumberOfCardsInHand(intel)==2 && !checkIfOpponentIsFirstToPlay(intel.getOpponentCard())) {
            cardToPlay = CardToPlay.of(selectLowestCard(intel.getCards(),intel.getVira()));
        }
        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }


    public Boolean checkIfOpponentIsFirstToPlay(Optional<TrucoCard> opponentCard) {
        return opponentCard.isPresent();
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        return cards.size();
    }

    public TrucoCard attemptToBeatOpponentCard(GameIntel intel) {
        TrucoCard cardToPlay = null;
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        for (TrucoCard card : hand) {
            if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) {
                    cardToPlay = card;
                }
            }
        }
        if (cardToPlay == null || cardToPlay.compareValueTo(opponentCard.get(), vira) <= 0) {
            cardToPlay = selectLowestCard(hand, vira);
        }
        return cardToPlay;
    }


    public TrucoCard selectLowestCard(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard lowestCard = null;
        for (TrucoCard card : hand) {
            if (lowestCard == null || card.compareValueTo(lowestCard, vira) < 0) {
                lowestCard = card;
            }
        }
        return lowestCard;
    }

    public TrucoCard selectStrongerCardExcludingZapAndCopas(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard strongestCard = null;

        for (TrucoCard card : hand) {
            if (!card.isZap(vira) && !card.isCopas(vira)) {
                if (strongestCard == null || card.relativeValue(vira) > strongestCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }
        }
        return strongestCard;
    }

    public boolean checkIfAcceptMaoDeOnze(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                count += 3;
            }
            if (card.compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), intel.getVira()) >= 0) {
                count++;
            }
        }

        int opponentPoints = intel.getOpponentScore();
        int threshold = 4;
        if (opponentPoints >= 8) {threshold = 6;}
        return count >= threshold;
    }

    public boolean checkIfRaiseGame(GameIntel intel){
        int count = 0;
        TrucoCard vira = intel.getVira();
        TrucoCard CardToCompare = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        List<TrucoCard> cards = intel.getCards();
        for (TrucoCard card: cards){
            if(card.compareValueTo(CardToCompare, vira) >= 0 || card.isManilha(vira) ){ count ++;}
        }
        if (cards.size() == 3) { return count >=2; }
        else if(cards.size() >= 1){return count >=1;}
        return count >0;
    };



}