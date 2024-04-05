/*
 *  Copyright (C) 2023 Eduardo Aguliari and Ramon Peixe
 *  Contact: eduardo <dot> aguliari <at> ifsp <dot> edu <dot> br
 *  Contact: ramon <dot> peixe <at> ifsp <dot> edu <dot> br
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
package com.peixe.aguliari.perdenuncabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class PerdeNuncaBot implements BotServiceProvider {
    private static final List<CardRank> offCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentCard().stream().anyMatch(c -> c.isZap(intel.getVira()))) {
            return true;
        }

        if (intel.getOpponentScore() == 11) {
            return true;
        }

        if (intel.getOpponentScore() >= 9 && hasManilhaAndHighRank(intel)) {
            return true;
        }

        if (intel.getOpponentScore() <= 6) {
            return true;
        }

        if (hasManilhaAndHighRank(intel)) {
            return true;
        }

        return hasAboveAverageValue(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        // If the opponent has a card, then:
        if (intel.getOpponentCard().isPresent()) {
            // If our card is higher than the opponent's card, then:
            if (getBiggerCardValue(intel) < intel.getOpponentCard().get().relativeValue(intel.getVira())) {
                // Don't raise.
                return false;
            }
        }

        // If the opponent has no points, then:
        if (intel.getOpponentScore() == 0) {
            // Raise.
            return true;
        }

        // If our score is greater than or equal to the opponent's score plus 3, then:
        if (intel.getScore() >= intel.getOpponentScore() + 3) {
            // Raise.
            return true;
        }

        // If the opponent's score is greater than or equal to 9, then:
        if (intel.getOpponentScore() >= 9) {
            // Don't raise.
            return false;
        }

        // If our hand is above average, then:
        if (hasAboveAverageValue(intel)) {
            // Raise.
            return true;
        }

        // If our hand is strong, then:
        if (hasManilhaAndHighRank(intel)) {
            // Raise.
            return true;
        }
        // Otherwise, don't raise.
        return false;
    }

    private boolean hasManilhaAndHighRank(GameIntel intel) {
        // Gets the player's cards.
        List<TrucoCard> cards = intel.getCards();

        // Gets the vira card.
        TrucoCard vira = intel.getVira();

        // Checks if the player has a manilha.
        boolean hasManilha = cards.stream().anyMatch(card -> card.isManilha(vira));

        // Checks if the player has a card with a high value.
        Optional<TrucoCard> highRankCard = cards.stream().filter(card -> !card.isManilha(vira)).filter(card -> card.getRank().value() > 4).findFirst();

        // Returns true if the player has a shackle and a card with a high value.
        return hasManilha && highRankCard.isPresent();
    }

    private boolean hasAboveAverageValue(GameIntel intel) {
        // Gets the player's cards.
        List<TrucoCard> cards = intel.getCards();

        // Gets the vira card.
        TrucoCard vira = intel.getVira();

        // Calculates the value of the hand.
        int handValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        // Returns true if the hand value is greater than or equal to 18.
        return handValue >= 18;
    }

    private int getBiggerCardValue(GameIntel intel) {
        // Gets the player's cards.
        List<TrucoCard> cards = intel.getCards();

        // Gets the vira card.
        TrucoCard vira = intel.getVira();

        // Returns the value of the highest card.
        return cards.stream().mapToInt(card -> card.relativeValue(vira)).max().orElse(0);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira())) {
                return CardToPlay.of(card);
            }
        }

        if (intel.getRoundResults().isEmpty()) {
            TrucoCard smallestAttackCard = getLowestAttackCard(intel);
            if (smallestAttackCard != null) {
                return CardToPlay.of(smallestAttackCard);
            }
        }

        // Choose the card with the lowest relative value to the opponent's card.
        TrucoCard smallestCardThatCanWin = chooseSmallestCardThatCanWin(intel);

        // If there is no card that can win, choose the card with the lowest relative value to the vira card.
        if (smallestCardThatCanWin == null) {
            smallestCardThatCanWin = getSmallestCardInHand(intel);
        }

        // Return the chosen card.
        return CardToPlay.of(smallestCardThatCanWin);
    }

    // This method gets the lowest attack card in the player's hand.
    private static TrucoCard getLowestAttackCard(GameIntel intel) {
        // Get a list of all attack cards in the player's hand.
        List<TrucoCard> attackCards = intel.getCards().stream().filter(card -> offCards.contains(card.getRank())).toList();

        // If the player has at least two attack cards, return the lowest attack card.
        if (attackCards.size() >= 2) {
            return attackCards.stream().min(TrucoCard::relativeValue).get();
        }

        // If the player has no attack cards, return null.
        return null;
    }

    private TrucoCard chooseSmallestCardThatCanWin(GameIntel intel) {
        // Get the opponent's card, if any.
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        // If there is no opponent's card, return null.
        if (opponentCard.isEmpty()) {
            return null;
        }

        // Get the vira card.
        TrucoCard vira = intel.getVira();

        // Initialize the card with the lowest relative value to the vira card.
        TrucoCard smallestCardThatCanWin = null;

        // Iterate over all the cards in the player's hand.
        for (TrucoCard card : intel.getCards()) {
            // If the current card is greater than the opponent's card, update the card with the lowest relative value to the vira card.
            if (card.relativeValue(vira) > opponentCard.get().relativeValue(vira)) {
                smallestCardThatCanWin = card;
            }
        }

        // Return the card with the lowest relative value to the vira card.
        return smallestCardThatCanWin;
    }

    private TrucoCard getSmallestCardInHand(GameIntel intel) {
        // Initialize the card with the lowest relative value to the vira card.
        TrucoCard smallestCard = null;

        // Get the vira card.
        TrucoCard vira = intel.getVira();

        // Iterate over all the cards in the player's hand.
        for (TrucoCard card : intel.getCards()) {
            // If the current card is less than the card with the lowest relative value to the vira card, update the card with the lowest relative value to the vira card.
            if (smallestCard == null || card.relativeValue(vira) < smallestCard.relativeValue(vira)) {
                smallestCard = card;
            }
        }

        // Return the card with the lowest relative value to the vira card.
        return smallestCard;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // If we have zap accept
//        if (intel.getCards().get(0).isZap(intel.getVira())) {
//            return 1;
//        }
        // If we have Manilha accept
        if (getManilha(intel)) {
            return 1;
        }
        // If we have 2 or 3 accept
        if (get2or3(intel)) {
            return 1;
        }
        // If both scores are 9, deny
        if (intel.getScore() == 9 && intel.getOpponentScore() == 9) {
            return 0;
        }
        // If our score is 9 and opponent score is 6 or less, accept
        if (intel.getScore() == 9 && intel.getOpponentScore() <= 6) {
            return 1;
        }
        // If our score is 6 and opponent score is 9 or higher, deny
        if (intel.getScore() == 6 && intel.getOpponentScore() >= 9) {
            return 0;
        }
        // If our score is 6 and opponent score is 6 or less, accept
        if (intel.getScore() == 6 && intel.getOpponentScore() <= 6) {
            return 1;
        }
        // If our score is 3 and opponent score is 6 or higher, deny
        if (intel.getScore() == 3 && intel.getOpponentScore() >= 6) {
            return 0;
        }
        // If our score is 3 and opponent score is 3 or less, accept
        if (intel.getScore() == 3 && intel.getOpponentScore() <= 3) {
            return 1;
        }
        // If opponent score is 0, accept
        if (intel.getOpponentScore() == 0) {
            return 1;
        }
        // If our score is 3 or higher than opponent score, accept
        if (intel.getScore() >= intel.getOpponentScore() + 3) {
            return 1;
        }
        // If opponent score is 9 or higher, deny
        if (intel.getOpponentScore() >= 9) {
            return 0;
        }

        return -1;
    }

    private boolean getManilha(GameIntel intel) {
        // Getting manilha
        return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isManilha(intel.getVira()));
    }

    public boolean get2or3(GameIntel intel) {
        // Getting 2 or 3
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.TWO || card.getRank() == CardRank.THREE);
    }
}
