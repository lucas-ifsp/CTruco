/*
 *  Copyright (C) 2024 Lucas V. Sigoli and João Castro
 *  Contact: Lucas <dot> Sigoli <at> ifsp <dot> edu <dot> br
 *  Contact: João <dot> Castro <at> ifsp <dot> edu <dot> br
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
package com.Sigoli.Castro.PatoBot;

import java.util.List;
import java.util.Optional;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;



public class PatoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return checkIfAcceptMaoDeOnze(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (isMaoDeOnze(intel)) return false;
        if (getGameResults(intel)) {
            return checkIfRaiseGame(intel);
        }
        return false;
    }

    private static boolean isMaoDeOnze(GameIntel intel) {
        return intel.getScore() == 11 || intel.getOpponentScore() == 11;
    }

    private static boolean getGameResults(GameIntel intel) {
        return intel.getRoundResults().isEmpty() || intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        boolean opponentPlayedFirst = checkIfOpponentIsFirstToPlay(intel.getOpponentCard());
        if (getNumberOfCardsInHand(intel) == 3) {
            return chooseCardForFirstRound(intel, opponentPlayedFirst);
        } else if (getNumberOfCardsInHand(intel) == 2) {
            return chooseCardForSecondRound(intel, opponentPlayedFirst);
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    private CardToPlay chooseCardForFirstRound(GameIntel intel, boolean opponentPlayedFirst) {
        if (opponentPlayedFirst) {
            return CardToPlay.of(attemptToBeatOpponentCard(intel));
        } else {
            return CardToPlay.of(selectStrongerCardExcludingZapAndCopas(intel));
        }
    }

    private CardToPlay chooseCardForSecondRound(GameIntel intel, boolean opponentPlayedFirst) {
        if (opponentPlayedFirst) {
            return CardToPlay.of(attemptToBeatOpponentCard(intel));
        } else {
            return CardToPlay.of(selectLowestCard(intel.getCards(), intel.getVira()));
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {return checkIfAcceptRaise(intel);}

    private Boolean checkIfOpponentIsFirstToPlay(Optional<TrucoCard> opponentCard) {return opponentCard.isPresent();}

    private int getNumberOfCardsInHand(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        return cards.size();
    }

    private TrucoCard attemptToBeatOpponentCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        TrucoCard cardToPlay = selectBetterCardToPlay(hand, opponentCard, vira);

        if (isCardIneffective(cardToPlay, opponentCard, vira)) cardToPlay = selectLowestCard(hand, vira);

        return cardToPlay;
    }

    private static boolean isCardIneffective(TrucoCard cardToPlay, Optional<TrucoCard> opponentCard, TrucoCard vira) {
        return cardToPlay == null || cardToPlay.compareValueTo(opponentCard.orElse(null), vira) <= 0;
    }

    private static TrucoCard selectBetterCardToPlay(List<TrucoCard> hand, Optional<TrucoCard> opponentCard, TrucoCard vira) {
        TrucoCard cardToPlay = null;
        for (TrucoCard card : hand) {
            if (card.compareValueTo(opponentCard.orElse(null), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) cardToPlay = card;
            }
        }
        return cardToPlay;
    }


    private TrucoCard selectLowestCard(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard lowestCard = null;
        for (TrucoCard card : hand) {
            if (lowestCard == null || card.compareValueTo(lowestCard, vira) < 0) lowestCard = card;
        }
        return lowestCard;
    }

    private TrucoCard selectStrongerCardExcludingZapAndCopas(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard strongestCard = intel.getCards().get(0);

        for (TrucoCard card : hand) {
            if (!card.isZap(vira) && !card.isCopas(vira)) {
                if (card.relativeValue(vira) > strongestCard.relativeValue(vira)) {
                    strongestCard = card;
                }
            }
        }
        return strongestCard;
    }

    private boolean checkIfAcceptMaoDeOnze(GameIntel intel) {
        int count = calculateCardScore(intel);
        int threshold = determineThreshold(intel.getOpponentScore());
        return count >= threshold;
    }

    private int calculateCardScore(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                count += 3;
            }
            if (isCardThree(intel, card)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isCardThree(GameIntel intel, TrucoCard card) {
        return card.compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), intel.getVira()) == 0;
    }

    private int determineThreshold(int opponentPoints) {
        return opponentPoints >= 8 ? 6 : 4;
    }

    private boolean checkIfStrongerCardIsThree(GameIntel intel) {
        TrucoCard ThreeToCompare = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        TrucoCard testCard = selectStrongerCardExcludingZapAndCopas(intel);
        return !testCard.isManilha(intel.getVira()) && testCard.compareValueTo(ThreeToCompare, intel.getVira()) == 0;

    }

    private boolean checkIfRaiseGame(GameIntel intel) {
        int count = calculateValidCardsCount(intel);
        if (checkIfStrongerCardIsThree(intel)) count--;

        return shouldRaiseBasedOnCount(intel, count);
    }

    private int calculateValidCardsCount(GameIntel intel) {
        int count = 0;
        TrucoCard vira = intel.getVira();
        TrucoCard CardToCompare = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(vira) || card.compareValueTo(CardToCompare, vira) >= 0) count++;
        }
        return count;
    }

    private boolean shouldRaiseBasedOnCount(GameIntel intel, int count) {
        if (getNumberOfCardsInHand(intel) == 3) return count >= 2;
        return count >= 1;
    }

    private int checkIfAcceptRaise(GameIntel intel) {
        int score = 0;
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (getNumberOfCardsInHand(intel) == 3) return 1;

        if (!hand.isEmpty()) {
            score = calculateCardScore(intel);
            if (checkIfStrongerCardIsThree(intel)) score--;
        }

        return raiseBasedOnRaoundResult(roundResults, score, hand, vira);
    }

    private int raiseBasedOnRaoundResult(List<GameIntel.RoundResult> roundResults, int score, List<TrucoCard> hand, TrucoCard vira) {
        if (roundResults.contains(GameIntel.RoundResult.WON)) {
            if (score >= 3 && hasZapOuCopas(hand, vira)) return 1;
            else if (score >= 4) return 0;
            else if (score >= 1) return 0;
            else return -1;
        } else {
            if (hasZapOuCopas(hand, vira))
                if (score >= 4) return 1;
            return -1;
        }
    }

    private boolean hasZapOuCopas(List<TrucoCard> hand, TrucoCard vira) {
        for (TrucoCard card : hand) {
            if (card.isZap(vira) || card.isCopas(vira)) return true;
        }
        return false;
    }

}