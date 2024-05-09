package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class FirstRoundStrategy implements BotServiceProvider {
    private List<TrucoCard> roundCards;
    private List<TrucoCard> ordendedCards; // ascending order
    private TrucoCard vira;
    private DefaultFunctions defaultFunctions;

    public FirstRoundStrategy() {}

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

        if (hasCasalMaior(ordendedCards)) return 1;
        if (defaultFunctions.isPowerfull(ordendedCards)) return 0;
        if (opponentCard.isPresent()){
            if (ordendedCards.size() == 3){
                if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0){
                    if (ordendedCards.get(2).isManilha(vira)) return 1;
                    if (ordendedCards.get(2).relativeValue(vira) >= 9) return 0;
                }
            }
        } else {
            if (ordendedCards.size() == 3) {
                if (defaultFunctions.isPowerfull(ordendedCards)) return 1;
                if (defaultFunctions.isMedium(ordendedCards)) return 0;
            } else {
                TrucoCard lastCardPlayed = intel.getOpenCards().get(intel.getOpenCards().size()-1);
                if (lastCardPlayed.isManilha(vira)) return 1; // Nesse caso teremos jogado nossa carta do meio, se ela for manilha, nossa maior carta também será
                if (lastCardPlayed.relativeValue(vira) >= 7 && ordendedCards.get(2).relativeValue(vira) >= 9) return 0;
            }
        }
        return 0;
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
            int indexOfCardThatCanWin = indexOfCardThatCanWin(ordendedCards, maybeOpponentCard);
            if (indexOfCardThatCanWin != -1){
                if (defaultFunctions.isMedium(ordendedCards)) return true;
            }
            return defaultFunctions.isPowerfull(ordendedCards);
        }

        Random random = new Random();
        int numero = random.nextInt(100);
        if (numero > 0 && numero <= 20 && defaultFunctions.isMedium(ordendedCards)) return true;

        if (defaultFunctions.isPowerfull(ordendedCards)) {
            if (intel.getScore() == 0 && intel.getOpponentScore() == 0) return true;
            if (intel.getScore() - intel.getOpponentScore() >= 3) return true;
            if (intel.getScore() - intel.getOpponentScore() <= 0) return false;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isEmpty()) {
            // Se o oponente não jogou a carta, jogamos nossa carta do meio
            return CardToPlay.of(ordendedCards.get(1));
        }
        int indexOfCardThatCanWin = indexOfCardThatCanWin(ordendedCards, opponentCard);
        if (indexOfCardThatCanWin != -1) return CardToPlay.of(ordendedCards.get(indexOfCardThatCanWin));
        else return CardToPlay.of(ordendedCards.get(0));
    }

    private int getNumberOfManilhas(List<TrucoCard> ordendedCards) {
        return (int) ordendedCards.stream().filter(c -> c.isManilha(vira)).count();
    }

    private boolean hasCasalMaior(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 3) return ordendedCards.get(2).isZap(vira) && ordendedCards.get(1).isCopas(vira);
        else return ordendedCards.get(1).isZap(vira) && ordendedCards.get(0).isCopas(vira);
    }

    private int indexOfCardThatCanWin(List<TrucoCard> ordendedCards, Optional<TrucoCard> opponentCard){
        if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) return 0;
        else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) return 1;
        else if (ordendedCards.get(2).compareValueTo(opponentCard.get(), vira) > 0) return 2;
        return -1;
    }
}
