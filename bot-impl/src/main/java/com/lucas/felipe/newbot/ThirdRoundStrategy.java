package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class ThirdRoundStrategy implements BotServiceProvider {
    private List<TrucoCard> roundCards;
    private List<TrucoCard> ordendedCards; // ascending order
    private TrucoCard vira;
    private DefaultFunctions defaultFunctions;

    public ThirdRoundStrategy() {}

    private void setCards(GameIntel intel){
        this.vira = intel.getVira();
        this.roundCards = intel.getCards();
        this.defaultFunctions = new DefaultFunctions(roundCards, vira);
        this.ordendedCards = defaultFunctions.sortCards(roundCards);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        if (ordendedCards.size() == 1){
            if (ordendedCards.get(0).isZap(vira) || ordendedCards.get(0).isCopas(vira)) return 1;
            if (defaultFunctions.isPowerfull(ordendedCards)) return 0;
        } else {
            TrucoCard lastCardPlayed = intel.getOpenCards().get(intel.getOpenCards().size()-1);
            if (lastCardPlayed.isCopas(vira) || lastCardPlayed.isZap(vira)) return 1;
            if (lastCardPlayed.isManilha(vira)) return 0;
        }
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        setCards(intel);
        return defaultFunctions.maoDeOnzeResponse(ordendedCards, intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        setCards(intel);
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        Optional<TrucoCard> maybeOpponentCard = intel.getOpponentCard();

        if (maybeOpponentCard.isPresent()) {
            if (ordendedCards.get(0).compareValueTo(maybeOpponentCard.get(), vira) > 0) return true;
        } else {
            if (ordendedCards.size() == 1) {
                if (defaultFunctions.isPowerfull(ordendedCards)) return true;
                Random random = new Random();
                int numero = random.nextInt(100);
                if (numero > 0 && numero <= 30 && defaultFunctions.isMedium(ordendedCards)) return true;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setCards(intel);
        return CardToPlay.of(ordendedCards.get(0));
    }
}
