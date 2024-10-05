package com.daniel.mateus.theroverbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class TheRover implements BotServiceProvider {

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

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "The Rover";
    }

    public boolean isPlayingFirst(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    public int getCurrentRound (GameIntel intel) {
        int cardsInHand = intel.getCards().size();
        if (cardsInHand == 3) return 1;
        if (cardsInHand == 2) return 2;
        if (cardsInHand == 1) return 3;
        return -1;
    }

    public TrucoCard chooseCardFirstHand (GameIntel intel) {

        if (isPlayingFirst(intel)) {
            int manilhaQtd = 0;
            for(int i = 0; i < intel.getCards().size(); i++) {
                if(intel.getCards().get(i).isManilha(intel.getVira())) {
                    manilhaQtd++;
                }
            }

            if(manilhaQtd >= 2) {
                TrucoCard lowestManilha = intel.getCards().get(0);
                for(int i = 0; i < intel.getCards().size(); i++) {
                    TrucoCard currentCard = intel.getCards().get(i);
                    if ((currentCard.relativeValue(intel.getVira()) < lowestManilha.relativeValue(intel.getVira()) &&
                            currentCard.isManilha(intel.getVira())) || !lowestManilha.isManilha(intel.getVira()) )  {
                        lowestManilha = currentCard;
                    }

                }
                return lowestManilha;
            }

            TrucoCard highestCard = intel.getCards().get(0);
            for(int i = 0; i < intel.getCards().size(); i++) {
                TrucoCard currentCard = intel.getCards().get(i);
                if ((currentCard.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira()) &&
                        !currentCard.isManilha(intel.getVira())) || highestCard.isManilha(intel.getVira())) {
                    highestCard = currentCard;
                }

            }
            return highestCard;
        }

        return null ;
    }

    public TrucoCard getLowestCardInHandThatBeatOpponentCard (GameIntel intel) {
        return null;
    }


}
