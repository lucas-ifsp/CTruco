package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SecondRoundStrategy implements BotServiceProvider {
    private List<TrucoCard> roundCards;
    private List<TrucoCard> ordendedCards; // ascending order
    private TrucoCard vira;
    private DefaultFunctions defaultFunctions;

    public SecondRoundStrategy() {}

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
            if (ordendedCards.size() == 2){
                if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0){
                    if (ordendedCards.get(1).isManilha(vira)) return 1;
                    if (ordendedCards.get(1).relativeValue(vira) >= 9) return 0;
                }
            }
        } else {
            if (ordendedCards.get(0).isManilha(vira)) return 1;
            if (ordendedCards.get(0).relativeValue(vira) >= 9) return 0;
        }
        return 1;
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
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (hasTwoManilhas(ordendedCards)) return true;
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        // Se ganhamos a primeira
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON || intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            if (opponentCard.isPresent()){
                int index = indexOfCardThatCanWin(ordendedCards, opponentCard);
                return index >= 0; // conseguimos ganhar a segunda se for >= 0
            }
            // Se o oponente ainda não jogou e temos carta maior que 3
            return hasThreeOrBetter(ordendedCards);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setCards(intel);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        // Ganhamos ?
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            // Oponente jogou a carta ?
            if (opponentCard.isPresent()) {
                lowestCardToWinOrDiscard(ordendedCards, opponentCard);
            }
            // se o oponente não jogou, jogamos nossa carta mais forte
            return CardToPlay.of(ordendedCards.get(1));
            // Caso primeiro round empatou, jogamos a mais forte
        } else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
            return CardToPlay.of(ordendedCards.get(1));
            // Caso perdemos o primeiro round
        } else {
            // Se conseguirmos, ganhamos com nossas cartas mais fracas, se não der, perdemos :(
            if (opponentCard.isPresent()){
                lowestCardToWinOrDiscard(ordendedCards, opponentCard);
            }
            // se o oponente não jogou, jogamos nossa carta mais forte
            return CardToPlay.of(ordendedCards.get(1));
        }
    }

    private CardToPlay lowestCardToWinOrDiscard(List<TrucoCard> ordendedCards, Optional<TrucoCard> opponentCard){
        if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(0));
        } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
            return CardToPlay.of(ordendedCards.get(1));
        }
        return CardToPlay.discard(ordendedCards.get(0));
    }

    private int indexOfCardThatCanWin(List<TrucoCard> ordendedCards, Optional<TrucoCard> opponentCard){
        if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
            return 0;
        } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
            return 1;
        }
        return -1;
    }
    private boolean hasThreeOrBetter(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 2) return ordendedCards.get(0).relativeValue(vira) >= 9 && ordendedCards.get(1).relativeValue(vira) >= 9;
        else return ordendedCards.get(0).relativeValue(vira) >= 9;
    }

    private boolean hasTwoManilhas(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 2) return ordendedCards.get(1).isManilha(vira) && ordendedCards.get(0).isManilha(vira);
        else return ordendedCards.get(0).isManilha(vira);
    }

    private boolean hasCasalMaior(List<TrucoCard> ordendedCards){
        if (ordendedCards.size() == 2) return ordendedCards.get(1).isZap(vira) && ordendedCards.get(0).isCopas(vira);
        return false;
    }
}
