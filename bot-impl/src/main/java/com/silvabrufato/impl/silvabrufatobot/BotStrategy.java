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
            return chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(false);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (hasZapAndThree() || hasCopasAndThree() || countManilhas(gameIntel) >= 2)
                return 0;
            return -1;
        }
    },

    SECOND_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (drewThePreviousRound()) {
                if (isOpponentThatStartTheRound())
                    return chooseCardToWinTheRoundIfPossible(false);
                return chooseTheStrongestManilhaIfHaveOneOrTheLowesCardToPlay(true);
            }
            if (isOpponentThatStartTheRound())
                return chooseCardToWinTheRoundIfPossible(true);
            return chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(false);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (gameIntel.getRoundResults().get(0) == RoundResult.LOST) {
                if (hasCopasAndEspadilha())
                    return 1;
                if (hasCopasAndOuros())
                    return 0;
            }
            if (gameIntel.getRoundResults().get(0) == RoundResult.WON) {
                if (hasCopas(gameIntel) || countManilhas(gameIntel) >= 2)
                    return 1;
                if (hasManilhaAndThree() || countThree(gameIntel) == 2)
                    return 0;
            }
            return -1;
        }
    },

    THIRD_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return SECOND_ROUND_STRATEGY.throwCard(gameIntel);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            setUpStrategy(gameIntel);
            if (gameIntel.getRoundResults().get(0) == RoundResult.LOST) {
                if (hasEspadilha() || hasOuros())
                    return 0;
            }
            if (gameIntel.getRoundResults().get(0) == RoundResult.WON) {
                if (hasEspadilha() || hasOuros() || countThree(gameIntel) > 0)
                    return 0;
            }
            return -1;
        }
    };

    private static GameIntel gameIntel;
    private static List<TrucoCard> sortedCards;

    private static void setUpStrategy(GameIntel gameIntel) {
        setTheGameIntel(gameIntel);
        sortCards();
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

    private static CardToPlay chooseTheStrongestManilhaIfHaveOneOrTheLowesCardToPlay(boolean discard) {
        if (countManilhas(gameIntel) > 0)
            return CardToPlay.discard(sortedCards.get(sortedCards.size() - 1));
        return chooseTheLowestCardToPlay(discard);
    }

    private static CardToPlay chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(boolean discard) {
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

    public static boolean hasCopas(GameIntel gameIntel) {
        for (TrucoCard card : gameIntel.getCards())
            if (card.isCopas(gameIntel.getVira()))
                return true;
        return false;
    }

    public static int countThree(GameIntel gameIntel) {
        int count = 0;
        for (TrucoCard card : gameIntel.getCards())
            if (card.getRank().value() == 10)
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

    private static boolean hasZap() {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isZap(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasCopas() {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isCopas(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasEspadilha() {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isEspadilha(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasOuros() {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isOuros(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasZapAndThree() {
        return hasZap() && countThree(gameIntel) > 0;
    }

    private static boolean hasCopasAndThree() {
        return hasCopas() && countThree(gameIntel) > 0;
    }

    private static boolean hasCopasAndEspadilha() {
        return hasCopas() && hasEspadilha();
    }

    private static boolean hasCopasAndOuros() {
        return hasCopas() && hasOuros();
    }

    private static boolean hasManilhaAndThree() {

        return countManilhas(gameIntel) > 0 && countThree(gameIntel) > 0;
    }

    public abstract CardToPlay throwCard(GameIntel gameIntel);

    public abstract int responseToRaisePoints(GameIntel gameIntel);

}
