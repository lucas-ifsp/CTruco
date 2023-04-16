package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FirstRound implements Strategy {

    private final TrucoCard vira;

    private final List<TrucoCard> cardList;

    private final List<TrucoCard> openCards;

    private final GameIntel intel;

    public FirstRound(GameIntel intel) {
        this.vira = intel.getVira();
        this.cardList = new ArrayList<>(intel.getCards());
        this.openCards = new ArrayList<>(intel.getOpenCards());
        this.intel = intel;
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
        Optional<TrucoCard> opponentCard = getWhichBotShouldPlayFirst();
        if (opponentCard.isPresent()) {
            TrucoCard opponentCardBot = opponentCard.get();
            int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(this.cardList, opponentCardBot, this.vira);
            switch (countCardsHigherOfOpponent) {
                case 1 -> {
                    CardToPlay.of(checkWhichCardHasHigherValue(this.cardList, this.vira));
                }
                case 2, 3 -> {
                    CardToPlay.of(chooseCardThatBeatsTheOpponent(this.cardList, this.vira, opponentCardBot));
                }
                default -> {
                    CardToPlay.of(checkWhichCardHasLowerValue(this.cardList, this.vira));
                }
            }
        }
        return null;
    }

    private TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(null);
    }

    private int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
    }

    private TrucoCard checkWhichCardHasLowerValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(null);
    }

    private TrucoCard checkWhichCardHasHigherValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(null);
    }

    public Optional<TrucoCard> getWhichBotShouldPlayFirst () {
        if (this.intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(this.intel.getOpponentCard().get());
    }
}
