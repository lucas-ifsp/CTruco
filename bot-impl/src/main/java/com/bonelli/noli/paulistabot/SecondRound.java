package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SecondRound implements Strategy {

    public SecondRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getScore() == 11) return false;
        TrucoCard vira = intel.getVira();

        List<TrucoCard> openCards = new ArrayList<>(intel.getCards().subList(0, 2));
        openCards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (openCards.get(1).getRank().value() >= 8) {
                    return calculateCurrentHandValue(intel) <= 8;
                }
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (hasZap(intel)) return CardToPlay.discard(cards.get(0));
                else if (calculateCurrentHandValue(intel) >= 17) return CardToPlay.of(cards.get(0));
                else {
                    if (calculateCurrentHandValue(intel) >= 5 && calculateCurrentHandValue(intel) < 13) return CardToPlay.of(cards.get(1));
                    return CardToPlay.of(cards.get(0));
                }
            }
            case LOST, DREW -> {
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if (opponentCard.getRank() != CardRank.HIDDEN) {
                        int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(cards, opponentCard, vira);
                        if (countCardsHigherOfOpponent == 0) {
                            if (cardWithSameValueOfOpponent(cards, opponentCard)) return CardToPlay.of(getCardWithSameValueOfOpponent(cards, opponentCard)); //Corre risco de amarrar dessa forma
                            return CardToPlay.discard(cards.get(0));
                        }
                        else if (countCardsHigherOfOpponent == 1) return CardToPlay.of(cards.get(1));
                        else return CardToPlay.of(chooseCardThatBeatsTheOpponent(cards, opponentCard, vira));
                    } else return CardToPlay.of(cards.get(0));
                } else return CardToPlay.of(cards.get(1));
            }
            default -> {
                return CardToPlay.of(cards.get(0));
            }
        }
    }

    public TrucoCard getCardWithSameValueOfOpponent(List<TrucoCard> cards, TrucoCard opponentCard) {
        return cards.stream().filter(card -> card.getRank().value() == opponentCard.getRank().value()).findFirst().orElse(cards.get(0));
    }

    @Override
    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    public boolean cardWithSameValueOfOpponent(List<TrucoCard> cards, TrucoCard opponentCard) {
        return cards.stream().anyMatch(card -> card.getRank().value() == opponentCard.getRank().value());
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    public int calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }

    public int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
    }

    public TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(cardList.get(1));
    }
}
