/*
 *  Copyright (C) 2023 João Pedro da Silva and Renan Brufato
 *  Contact: jps <dot> spj <at> gmail <dot> com 
 *  Contact: brufato17 <dot> renan <at> gmail <dot> com
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */
package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.*;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.silvabrufato.impl.silvabrufatobot.BotBluff.Probability;

import java.util.ArrayList;
import java.util.List;

public enum BotStrategy {

    FIRST_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            if (isOpponentThatStartTheRound(gameIntel))
                return chooseCardToWinOrToDrawTheRoundIfPossible(gameIntel, false);
            return chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(gameIntel, false);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            if (countManilhas(gameIntel) >= 2 || hasZapAndThree(gameIntel) ||
                    hasCopasAndThree(gameIntel))
                return 0;
            return -1;
        }

        @Override
        public boolean raisePoints(GameIntel gameIntel) {
            if (countManilhas(gameIntel) >= 2 && hasZap(gameIntel))
                return true;
            if (hasZap(gameIntel) || hasCopas(gameIntel) || hasEspadilha(gameIntel))
                return true;
            if (countManilhas(gameIntel) >= 1)
                return BotBluff.of(Probability.P40).bluff();
            if (countThree(gameIntel) >= 2 || countCardsEqualOrHigherThanAce(gameIntel) >= 2)
                return BotBluff.of(Probability.P20).bluff();
            return false;
        }
    },

    SECOND_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            if (drewThePreviousRound(gameIntel)) {
                if (isOpponentThatStartTheRound(gameIntel))
                    return chooseCardToWinTheRoundIfPossible(gameIntel, false);
                return chooseTheStrongestManilhaIfHaveOneOrTheLowesCardToPlay(gameIntel, true);
            }
            if (isOpponentThatStartTheRound(gameIntel))
                return chooseCardToWinTheRoundIfPossible(gameIntel, true);
            return chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(gameIntel, false);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            if (gameIntel.getRoundResults().get(0) == RoundResult.LOST) {
                if (hasCopasAndEspadilha(gameIntel))
                    return 1;
                if (hasCopasAndOuros(gameIntel))
                    return 0;
            }
            if (gameIntel.getRoundResults().get(0) == RoundResult.WON) {
                if (hasCopas(gameIntel) || countManilhas(gameIntel) >= 2)
                    return 1;
                if (hasManilhaAndThree(gameIntel) || countThree(gameIntel) == 2)
                    return 0;
            }
            return -1;
        }

        @Override
        public boolean raisePoints(GameIntel gameIntel) {
            if (gameIntel.getRoundResults().get(0) == RoundResult.LOST) {
                if (countManilhas(gameIntel) == 2 || hasZap(gameIntel))
                    return true;
            }
            if (countManilhas(gameIntel) >= 2)
                return true;
            if (hasZap(gameIntel) || hasCopas(gameIntel) || hasEspadilha(gameIntel))
                return true;
            if (hasOuros(gameIntel))
                return BotBluff.of(Probability.P60).bluff();
            if (countThree(gameIntel) >= 2)
                return BotBluff.of(Probability.P20).bluff();
            return false;
        }
    },

    THIRD_ROUND_STRATEGY {
        @Override
        public CardToPlay throwCard(GameIntel gameIntel) {
            return SECOND_ROUND_STRATEGY.throwCard(gameIntel);
        }

        @Override
        public int responseToRaisePoints(GameIntel gameIntel) {
            if (gameIntel.getRoundResults().get(0) == RoundResult.LOST) {
                if (hasEspadilha(gameIntel) || hasOuros(gameIntel))
                    return 0;
            }
            if (gameIntel.getRoundResults().get(0) == RoundResult.WON) {
                if (hasEspadilha(gameIntel) || hasOuros(gameIntel) || countThree(gameIntel) > 0)
                    return 0;
            }
            return -1;
        }

        @Override
        public boolean raisePoints(GameIntel gameIntel) {
            if (BotStrategy.hasEspadilha(gameIntel))
                return true;
            if (BotStrategy.hasOuros(gameIntel))
                return true;
            return BotBluff.of(Probability.P20).bluff();
        }
    };

    private static boolean drewThePreviousRound(GameIntel gameIntel) {
        return gameIntel.getRoundResults().get(gameIntel.getRoundResults().size() - 1) == RoundResult.DREW;
    }

    private static List<TrucoCard> sortCards(GameIntel gameIntel) {
        ArrayList<TrucoCard> arrayOfCards = new ArrayList<>();
        arrayOfCards.addAll(gameIntel.getCards());
        arrayOfCards.sort((card1, card2) -> card1.compareValueTo(card2, gameIntel.getVira()));
        return List.copyOf(arrayOfCards);
    }

    private static boolean isOpponentThatStartTheRound(GameIntel gameIntel) {
        return gameIntel.getOpponentCard().isPresent();
    }

    private static CardToPlay chooseTheLowestCardToPlay(GameIntel gameIntel, boolean discard) {
        List<TrucoCard> cards = sortCards(gameIntel);
        if (discard)
            return CardToPlay.discard(cards.get(0));
        return CardToPlay.of(cards.get(0));
    }

    private static CardToPlay chooseCardToWinOrToDrawTheRoundIfPossible(GameIntel gameIntel, boolean discard) {
        List<TrucoCard> cards = sortCards(gameIntel);
        for (TrucoCard card : cards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) > 0)
                return CardToPlay.of(card);
        for (TrucoCard card : cards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) == 0)
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(gameIntel, discard);
    }

    private static CardToPlay chooseCardToWinTheRoundIfPossible(GameIntel gameIntel, boolean discard) {
        List<TrucoCard> cards = sortCards(gameIntel);
        for (TrucoCard card : cards)
            if (card.compareValueTo(gameIntel.getOpponentCard().get(), gameIntel.getVira()) > 0)
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(gameIntel, discard);
    }

    private static CardToPlay chooseTheStrongestManilhaIfHaveOneOrTheLowesCardToPlay(GameIntel gameIntel,
            boolean discard) {
        if (countManilhas(gameIntel) > 0) {
            List<TrucoCard> cards = sortCards(gameIntel);
            return CardToPlay.of(cards.get(cards.size() - 1));
        }
        return chooseTheLowestCardToPlay(gameIntel, discard);
    }

    private static CardToPlay chooseTheLowestManilhaIfHaveOneOrTheLowestCardToPlay(GameIntel gameIntel,
            boolean discard) {
        List<TrucoCard> cards = sortCards(gameIntel);
        for (TrucoCard card : cards)
            if (card.isManilha(gameIntel.getVira()))
                return CardToPlay.of(card);
        return chooseTheLowestCardToPlay(gameIntel, discard);
    }

    public static int countManilhas(GameIntel gameIntel) {
        int count = 0;
        for (TrucoCard card : gameIntel.getCards())
            if (card.isManilha(gameIntel.getVira()))
                count++;
        return count;
    }

    public static int countThree(GameIntel gameIntel) {
        int count = 0;
        for (TrucoCard card : gameIntel.getCards())
            if (card.getRank().value() == CardRank.THREE.value())
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

    private static boolean hasZap(GameIntel gameIntel) {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isZap(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasCopas(GameIntel gameIntel) {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isCopas(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasEspadilha(GameIntel gameIntel) {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isEspadilha(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasOuros(GameIntel gameIntel) {
        for (TrucoCard card : gameIntel.getCards()) {
            if (card.isOuros(gameIntel.getVira()))
                return true;
        }
        return false;
    }

    private static boolean hasZapAndThree(GameIntel gameIntel) {
        return hasZap(gameIntel) && countThree(gameIntel) > 0;
    }

    private static boolean hasCopasAndThree(GameIntel gameIntel) {
        return hasCopas(gameIntel) && countThree(gameIntel) > 0;
    }

    private static boolean hasCopasAndEspadilha(GameIntel gameIntel) {
        return hasCopas(gameIntel) && hasEspadilha(gameIntel);
    }

    private static boolean hasCopasAndOuros(GameIntel gameIntel) {
        return hasCopas(gameIntel) && hasOuros(gameIntel);
    }

    private static boolean hasManilhaAndThree(GameIntel gameIntel) {
        return countManilhas(gameIntel) > 0 && countThree(gameIntel) > 0;
    }

    public abstract CardToPlay throwCard(GameIntel gameIntel);

    public abstract int responseToRaisePoints(GameIntel gameIntel);

    public abstract boolean raisePoints(GameIntel gameIntel);

}
