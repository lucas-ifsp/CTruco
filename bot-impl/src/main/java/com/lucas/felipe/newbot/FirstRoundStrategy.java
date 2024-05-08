package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            } else {
                if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0){
                    if (ordendedCards.get(1).isManilha(vira)) return 1;
                    if (ordendedCards.get(1).relativeValue(vira) >= 9) return 0;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        setCards(intel);
        int opponentScore = intel.getOpponentScore();
        boolean isPowerfull = defaultFunctions.isPowerfull(ordendedCards);
        boolean isMedium = defaultFunctions.isMedium(ordendedCards);
        if (opponentScore <= 6) return isMedium;
        return isPowerfull;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        setCards(intel);
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        if (defaultFunctions.isPowerfull(ordendedCards)) {
            if (intel.getScore() == 0 && intel.getOpponentScore() == 0) return true;
            if (intel.getScore() - intel.getOpponentScore() >= 3) return true;
            if (intel.getScore() - intel.getOpponentScore() < 0) return false;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        // Oponente já jogou a carta?
        if (opponentCard.isEmpty()) {
            // Se o oponente não jogou a carta, jogamos nossa carta do meio
            return CardToPlay.of(ordendedCards.get(1));
        }
        if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(0));
        }
        else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(1));
        }
        else if (ordendedCards.get(2).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(2));
        } else return CardToPlay.of(ordendedCards.get(0));
    }

    private int getNumberOfManilhas(List<TrucoCard> ordendedCards) {
        return (int) ordendedCards.stream().filter(c -> c.isManilha(vira)).count();
    }

    private boolean hasCasalMaior(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 3) return ordendedCards.get(2).isZap(vira) && ordendedCards.get(1).isCopas(vira);
        else return ordendedCards.get(1).isZap(vira) && ordendedCards.get(0).isCopas(vira);
    }
}
