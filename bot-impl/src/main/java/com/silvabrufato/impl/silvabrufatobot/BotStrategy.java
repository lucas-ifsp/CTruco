package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;
import com.bueno.spi.model.GameIntel.RoundResult;

import java.util.ArrayList;
import java.util.List;

public enum BotStrategy {

    FIRST_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (isOpponentThatStartTheRound())
                return chooseCardToWinOrToDrawTheRoundIfPossible(false);
            return chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne(false);
        }
    },

    OTHERS_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (isOpponentThatStartTheRound()) return chooseCardToWinTheRoundIfPossible(true);
            return chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne(false);
        }
    };

    private static GameIntel gameIntel;
    private static List<TrucoCard> sortedCards;

    private static void setUpStrategy(GameIntel gameIntel) {
        setTheGameIntel(gameIntel);
        sortCards();
    }

    public static int firstRoundStrategy(GameIntel gameIntel){
        setUpStrategy(gameIntel);
        TrucoCard vira = gameIntel.getVira();

        if(checksIfThereIsZAPAndThree(vira) || checksIfThereIsCOPASAndThree(vira) || countManilhas(gameIntel) >= 2) return 0; //aceita
        return -1;
    }

    public static int secondRoundStrategy(GameIntel gameIntel){
        setUpStrategy(gameIntel);
        TrucoCard vira = gameIntel.getVira();

        if (gameIntel.getRoundResults().get(0) == RoundResult.LOST){
            if(checksIfThereIsCopasAndSpades(vira)) return 1;
            if(checksIfThereIsCopasAndOuros(vira)) return 0;
        }
        return -1;
    }

    private static boolean drewThePreviousRound() {
        return gameIntel.getRoundResults().get(gameIntel.getRoundResults().size() - 1) == RoundResult.DREW;
    }

    private static void setTheGameIntel(GameIntel gameIntel) {
        BotStrategy.gameIntel = gameIntel;
    }

    private static void sortCards() {
        ArrayList<TrucoCard> arrayOfCards = new ArrayList<>();
        arrayOfCards.addAll(gameIntel.getCards());
        arrayOfCards.sort((card1, card2) -> card1.compareValueTo(card2, gameIntel.getVira()));
        BotStrategy.sortedCards = List.copyOf(arrayOfCards);
    }

    private static boolean isOpponentThatStartTheRound() {
        return BotStrategy.gameIntel.getOpponentCard().isPresent();
    }

    private static CardToPlay chooseTheLowestCardToPlay(boolean discard) {
        if (discard)
            return CardToPlay.discard(sortedCards.get(0));
        return CardToPlay.of(sortedCards.get(0));
    }

    private static CardToPlay chooseCardToWinOrToDrawTheRoundIfPossible(boolean discard) {
        for (TrucoCard card : sortedCards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) > 0)
                return CardToPlay.of(card);
        for (TrucoCard card : sortedCards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) == 0)
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(discard);
    }

    private static CardToPlay chooseCardToWinTheRoundIfPossible(boolean discard) {
        for (TrucoCard card : sortedCards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) > 0)
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(discard);
    }

    private static CardToPlay chooseTheLowestCardToPlayOrAManilhaIfYouHaveOne(boolean discard) {
        for (TrucoCard card : sortedCards)
            if (card.isManilha(gameIntel.getVira()))
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(discard);
    }

    public static int countManilhas(GameIntel gameIntel) {
        int count = 0;
        for (TrucoCard card : gameIntel.getCards())
            if (card.isManilha(gameIntel.getVira()))
                count++;
        return count;
    }

    public static int countCardsEqualOrHigherThanAce(GameIntel gameIntel) {
        int count = 0;
        for (TrucoCard card : gameIntel.getCards())
            if (card.getRank().value() >= CardRank.ACE.value())
                count++;
        return count;
    }

    private static boolean checksIfThereIsZAPAndThree(TrucoCard vira){
        boolean flagZap = false;
        boolean flagThree = false;

        for (TrucoCard card:sortedCards) {
            if (card.getRank().value() == 10) flagThree = true;
        }

        for (TrucoCard card:sortedCards) {
            if (card.isZap(vira)) flagZap = true;
        }

        if (flagThree == true && flagZap == true) return true;

        return false;
    }

    private static boolean checksIfThereIsCOPASAndThree(TrucoCard vira){
        boolean flagCopas = false;
        boolean flagThree = false;

        for (TrucoCard card:sortedCards) {
            if (card.getRank().value() == 10) flagThree = true;
        }

        for (TrucoCard card:sortedCards) {
            if (card.isCopas(vira)) flagCopas = true;
        }

        if (flagThree == true && flagCopas == true) return true;
        return false;
    }

    private static boolean checksIfThereIsCopasAndSpades(TrucoCard vira){
        boolean flagCopas = false;
        boolean flagSpades = false;

        for (TrucoCard card:sortedCards) {
            if (card.isEspadilha(vira)) flagSpades = true;
        }

        for (TrucoCard card:sortedCards) {
            if (card.isCopas(vira)) flagCopas = true;
        }

        if (flagSpades == true && flagCopas == true) return true;

        return false;
    }

    private static boolean checksIfThereIsCopasAndOuros(TrucoCard vira){
        boolean flagCopas = false;
        boolean flagOuros = false;

        for (TrucoCard card:sortedCards) {
            if (card.isOuros(vira)) flagOuros = true;
        }

        for (TrucoCard card:sortedCards) {
            if (card.isCopas(vira)) flagCopas = true;
        }

        if (flagOuros == true && flagCopas == true) return true;

        return false;
    }

    public abstract CardToPlay throwCard(GameIntel gameIntel);

}
