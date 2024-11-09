package com.caua.felipe.cfbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class CFBot implements BotServiceProvider {


    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        myCards.sort((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
        TrucoCard vira = intel.getVira();

        if(isBiggestCoupleOrBlack(intel, myCards)) return 1;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isManilha(vira))) return 1;
        if (whatRound(intel) == 3 && myCards.stream().anyMatch(card -> card.isZap(intel.getVira()))) return 1;
        if (whatRound(intel) == 3 && toTwoOrThree(intel, myCards)) return 0;

        return -1;
    }


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        if (isBiggestCoupleOrBlack(intel, myCards)) return true;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isZap(intel.getVira()))) return true;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isCopas(intel.getVira()))) return true;

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        if (isBiggestCoupleOrBlack(intel, myCards)) return true;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isManilha(intel.getVira()))) return true;
        if (myCards.stream().filter(card -> card.getRank().value() < 7).count() > 1) return false;
        if (intel.getOpponentScore() > 8) return false;
        if (myCards.stream().anyMatch(card -> card.isManilha(intel.getVira())) &&
                myCards.stream().anyMatch(card -> card.relativeValue(intel.getVira()) > 6)) return true;
        return false;
    }


    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        cards.sort((card1, card2) -> card1.compareValueTo(card2, vira));

        TrucoCard cardMenor = cards.get(0);
        TrucoCard cardMaior = cards.get(cards.size() - 1);

        if (whatRound(intel) == 1 && intel.getOpponentCard().isEmpty()){
            return CardToPlay.of(cardMenor);
        }

        if (whatRound(intel) == 1){
            return getLessToKill(intel, cards, vira);
        }

        if (whatRound(intel) == 2){
            if (intel.getOpponentCard().isPresent()) return getLessToKill(intel, cards, vira);
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) return CardToPlay.of(cardMaior);
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    private CardToPlay getLessToKill(GameIntel intel, List<TrucoCard> cards, TrucoCard vira) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        List<TrucoCard> winningCards = cards.stream()
                .filter(card -> opponentCard.isPresent() && card.compareValueTo(opponentCard.get(), vira) > 0)
                .sorted((card1, card2) -> card1.compareValueTo(card2, vira))
                .toList();

        TrucoCard cardToPlay = winningCards.isEmpty() ? cards.get(0) : winningCards.get(0);

        return CardToPlay.of(cardToPlay);
    }


    public boolean isBiggestCoupleOrBlack(GameIntel gameIntel, List<TrucoCard> myCards) {

        boolean isManilhaClubs = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.CLUBS);
        boolean isManilhaHearts = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.HEARTS);
        boolean isManilhaSpades = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.SPADES);

        if (isManilhaClubs && isManilhaHearts) return true;
        return isManilhaClubs && isManilhaSpades;
    }

    public boolean toTwoOrThree(GameIntel gameIntel, List<TrucoCard> myCards){
        return myCards.stream().anyMatch(card -> card.getRank().value() == 9 || card.getRank().value() == 10 );
    }

    public int whatRound(GameIntel gameIntel){
        List<TrucoCard> cards = gameIntel.getCards();


        if (cards.size() == 3) return 1;
        if (cards.size() == 2) return 2;
        return 3;
    }

}
