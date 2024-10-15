package com.belini.luciano.matapatobot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class MataPatoBot implements BotServiceProvider{

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();
        long playableCards;
        if(opponentScore <= 3){
            return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isManilha(vira));
        }
        if(opponentScore < 6){
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) >= 7)
                    .count();

            return playableCards >= 2;

        }
        if (opponentScore <= 8) {
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) >= 8)
                    .count();

            return playableCards >= 2;

        }
        if (opponentScore < 11) {
            playableCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) > 9)
                    .count();

            return playableCards >= 1 && intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6);
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            if (countManilha(intel) >= 2 ){
                return 1;
            }
            if (countManilha(intel) >= 1 && handSValue(intel) >= 24) {
                return 0;
            }
        }

        return -1;
    }


    public Boolean checkFirstPlay (Optional<TrucoCard> opponentCard){
        return opponentCard.isPresent();
    }

    public TrucoCard KillingOpponentCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard cardToPlay = null;

        if (!opponentCard.isPresent()) {
            return null;
        }

        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(lowestCard, vira) < 0) {
                lowestCard = card;
            }
            if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) {
                    cardToPlay = card;
                }
            }
        }

        return cardToPlay != null ? cardToPlay : lowestCard;
    }
    public TrucoCard shouldPlayStrongCard(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard strongestCard = null;
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        boolean opponentPlayedManilha = opponentCard != null && opponentCard.isManilha(vira);

        for (TrucoCard card : hand) {
            if (!card.isZap(vira) && !card.isCopas(vira) && !card.isEspadilha(vira)) {
                if (strongestCard == null || card.relativeValue(vira) > strongestCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }

            if (opponentPlayedManilha && card.isManilha(vira)) {
                if (strongestCard == null || card.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }
        }

        return strongestCard;
    }

    public String RoundCheck(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        int cardCount = cards.size();

        return switch (cardCount) {
            case 3 -> "Round 1";
            case 2 -> "Round 2";
            case 1 -> "Round 3";
            default -> "No cards";
        };
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        List <TrucoCard> cards = intel.getCards();
        return cards.size();
    }

    public int handSValue (GameIntel intel){
        int handSValue = 0;
        for (TrucoCard card : intel.getCards()){
            handSValue += card.relativeValue(intel.getVira());
        }
        return handSValue;
    }
    public long countManilha (GameIntel intel) {
        long count = intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
        return count;
    }
}
