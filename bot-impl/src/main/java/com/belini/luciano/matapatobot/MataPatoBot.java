package com.belini.luciano.matapatobot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class MataPatoBot implements BotServiceProvider{

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
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
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
    public boolean verifyGreatHand(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard cardToCompare = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        List<TrucoCard> cards = intel.getCards();
        int count = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira) || card.compareValueTo(cardToCompare, vira) >= 0) {
                count++;
            }
        }
        return (cards.size() == 3) ? count >= 2 : count >= 1;
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        List <TrucoCard> cards = intel.getCards();
        return cards.size(); //retorna tamanho da mão
    }
}
