package com.joao.alexandre.jormungandrbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.NoSuchElementException;
import java.util.Optional;

public class JormungandrBot implements BotServiceProvider {
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
        return switch (getCurrentRoundNumber(intel)) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> null;
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "Jörmungandr";
    }

    CardToPlay chooseCardFirstRound(GameIntel intel) {
        return null;
    }

    CardToPlay chooseCardSecondRound(GameIntel intel) {
        return null;
    }

    CardToPlay chooseCardThirdRound(GameIntel intel) {
        return null;
    }

    TrucoCard getHighestCardInHand(GameIntel intel) {
        TrucoCard currentHighestCard = intel.getCards().get(0);
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentHighestCard, vira) > 0)
                currentHighestCard = card;
        }

        return currentHighestCard;
    }

    TrucoCard getLowestCardInHand(GameIntel intel) {
        TrucoCard currentLowestCard = intel.getCards().get(0);
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentLowestCard, vira) < 0)
                currentLowestCard = card;
        }

        return currentLowestCard;
    }

    Optional<TrucoCard> getLowestCardToBeatOpponentsCard(GameIntel intel) {
        TrucoCard currentLowestCard = getHighestCardInHand(intel);
        TrucoCard opponentsCard = intel.getOpponentCard()
                .orElseThrow(() -> new NoSuchElementException("Opponent doesn't have a card to beat"));
        TrucoCard vira = intel.getVira();

        Optional<TrucoCard> optionalTrucoCard = Optional.empty();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentLowestCard, vira) <= 0 &&
                    card.compareValueTo(opponentsCard, vira) > 0) {

                optionalTrucoCard = Optional.of(card);
                currentLowestCard = card;
            }
        }

        return optionalTrucoCard;
    }

    Optional<TrucoCard> getCardToTieOpponentsCard(GameIntel intel) {
        TrucoCard opponentsCard = intel.getOpponentCard()
                .orElseThrow(() -> new NoSuchElementException("Opponent doesn't have a card to tie"));
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(opponentsCard, vira) == 0)
                return Optional.of(card);
        }

        return Optional.empty();
    }

    Optional<TrucoCard> getHighestNonManilhaCardInHand(GameIntel intel) {
        TrucoCard currentHighestCard = getLowestCardInHand(intel);
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> cardToReturn = Optional.empty();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentHighestCard, vira) >= 0 && !card.isManilha(vira)){
                currentHighestCard = card;
                cardToReturn = Optional.of(card);
            }
        }

        return cardToReturn;
    }

    boolean isSecondToPlay(GameIntel intel){
        return intel.getOpponentCard().isPresent();
    }

    int getCurrentRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    boolean hasPlayedACard(GameIntel intel){int cardsInHand = intel.getCards().size();

        switch(getCurrentRoundNumber(intel)) {
            case 1:
                if(cardsInHand < 3)
                    return true;

            case 2:
                if(cardsInHand < 2)
                    return true;

            case 3:
                if(cardsInHand < 1)
                    return true;

            default:
                return false;
        }
    }
}
