package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SecondRoundStrategy implements StrategyByRound{
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
        setCards(intel);
        Optional<TrucoCard> opponentCard = Optional.empty();
        if (intel.getOpponentCard().isPresent()) opponentCard = intel.getOpponentCard();
        // Ganhamos ?
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            // Oponente jogou a carta ?
            if (opponentCard.isPresent()) {
                // Tentamos ganhar com as cartas mais fracas
                if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(0));
                } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(1));
                } else return CardToPlay.discard(ordendedCards.get(0));
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
                if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(0));
                }
                else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(1));
                } else return CardToPlay.discard(ordendedCards.get(0));
            }
            // se o oponente não jogou, jogamos nossa carta mais forte
            return CardToPlay.of(ordendedCards.get(1));
        }
    }
}
