/*
 *  Copyright (C) 2024 Lucas Jundi Hikazudani - IFSP/SCL
 *            (C) 2024 Priscila de Luna Farias - IFSP/SCL
 *
 *  Contact: h <dot> jundi <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: luna <dot> p <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.luna.jundi.jokerBot.HandClassification;
import com.luna.jundi.jokerBot.exceptions.WithoutCardsToPlayException;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;
import static com.luna.jundi.jokerBot.HandClassification.*;
import static com.luna.jundi.jokerBot.utils.RoundUtils.getRoundNumber;
import static com.luna.jundi.jokerBot.utils.RoundUtils.jokerBotStartsTheRound;

public sealed interface RoundState
        permits FirstToPlayRoundOneState, SecondToPlayRoundOneState,
        FirstToPlayRoundTwoState, SecondToPlayRoundTwoState,
        RoundThreeState {


    CardToPlay cardChoice();

    boolean raiseDecision();

    int raiseResponse();

    default int defaultRaiseResponse(GameIntel intel) {
        int handPoints = intel.getHandPoints();
        boolean myCardsAreExcellent = EXCELLENT.equals(getHandEvaluation(intel));
        boolean myCardsAreGood = GREAT.equals(getHandEvaluation(intel));
        if (hasZap(intel)) return 1;
        if (handPoints < 6 && (myCardsAreGood || myCardsAreExcellent)) return 0;
        if (myCardsAreExcellent) return 1; //raise condition
        if (getNumberOfManilhas().apply(intel.getCards(), intel.getVira()) == 0) return -1; // run conditions
        if (isTiedHand(intel) && getHandEvaluation(intel).value() >= 3) return 0; //accept conditions
        if (jokerBotStartsTheRound().test(intel) && isNotLoosingHand(intel)) return 0;

        return -1;
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    default boolean maoDeOnzeDecision(GameIntel intel) {

        boolean myCardsAreExcellent = EXCELLENT.equals(getHandEvaluation(intel));
        boolean myCardsAreGood = GREAT.equals(getHandEvaluation(intel));

        if (intel.getOpponentScore() == 11 || intel.getOpponentScore() <= 6) return true;
        if (getNumberOfManilhas().apply(intel.getCards(), intel.getVira()) > 1 && (myCardsAreGood || myCardsAreExcellent))
            return true;
        if (intel.getOpponentScore() >= 9)
            return myCardsAreGood || myCardsAreExcellent;

        return false;
    }

    default BiPredicate<GameIntel, Integer> isValidRoundState() {
        return (intel, roundNumber) -> intel.getRoundResults().size() + 1 == roundNumber;
    }

    default Function<GameIntel, CardToPlay> secondRoundsChoicesCard() {
        return (intel) -> intel.getCards().stream()
                .filter(card -> hasBiggerCard().test(card, intel) || hasEqualCard().test(card, intel))
                .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .map(CardToPlay::of)
                .orElse(getWorstCard().apply(intel));
    }

    private BiPredicate<TrucoCard, GameIntel> hasBiggerCard() {
        return (botCard, intel) -> intel.getOpponentCard()
                .map(opponentCard -> botCard.relativeValue(intel.getVira()) > opponentCard.relativeValue(intel.getVira()))
                .orElse(false);
    }

    private BiPredicate<TrucoCard, GameIntel> hasEqualCard() {
        return (botCard, intel) -> intel.getOpponentCard()
                .map(opponentCard -> botCard.relativeValue(intel.getVira()) == opponentCard.relativeValue(intel.getVira()))
                .orElse(false);
    }

    default Function<GameIntel, CardToPlay> getBestCard() {
        return (intel) -> getMyCardsSorted().apply(intel).stream()
                .map(CardToPlay::of)
                .findFirst()
                .orElseThrow(() -> new WithoutCardsToPlayException("JokerBot have no cards to play :(\n"));
    }

    private Function<GameIntel, List<TrucoCard>> getMyCardsSorted() {
        return (intel) -> intel.getCards().stream()
                .sorted(Comparator.comparingInt(card -> ((TrucoCard) card).relativeValue(intel.getVira())).reversed())
                .collect(Collectors.toList());
    }

    default Function<GameIntel, CardToPlay> getWorstCard() {
        return (intel) -> getMyCardsSorted().apply(intel).stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .map(CardToPlay::of)
                .orElseThrow(() -> new WithoutCardsToPlayException("JokerBot have no cards to play, even a worse :'\n"));
    }

    default boolean isWinningHand(GameIntel intel) {
        if (isTiedHand(intel)) return false;
        return !isFirstRound().test(intel);
    }

    private Predicate<GameIntel> isFirstRound() {
        return intel -> intel.getRoundResults().isEmpty();
    }

    private boolean isTiedHand(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int roundNumber = getRoundNumber(intel);
        if (isFirstRound().test(intel)) return false;
        if (roundNumber == 2 && DREW.equals(roundResults.get(0))) return true;
        return roundNumber == 3 && DREW.equals(roundResults.get(0)) && DREW.equals(roundResults.get(1));
    }

    default boolean isNotLoosingHand(GameIntel intel) {                     //PREDICATE
        return (isWinningHand(intel) || isTiedHand(intel) || isFirstRound().test(intel));
    }

    private Function<GameIntel, Double> averageRelativeValueOfCards() {
        return intel -> intel.getCards().stream()
                .mapToDouble(card -> card.relativeValue(intel.getVira()))
                .average().orElse(0.00);
    }

    default HandClassification getHandEvaluation(GameIntel intel) {
        double average = averageRelativeValueOfCards().apply(intel);
        if (average >= 10) return EXCELLENT;
        if (average >= 8) return GREAT;
        if (average >= 6 || getNumberOfManilhas().apply(intel.getCards(), intel.getVira()) > 0) return GOOD;
        if (average >= 4) return NOT_BAD;
        return BAD;
    }

    private BiFunction<List<TrucoCard>, TrucoCard, Integer> getNumberOfManilhas() {
        return (trucoCards, vira) -> (int) trucoCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    default boolean raiseHandByOpponentCard(GameIntel intel) {
        CardToPlay possibleBiggerCard = secondRoundsChoicesCard().apply(intel);
        int myBiggerCardValue = possibleBiggerCard.value().relativeValue(intel.getVira());
        int opponentCardValue = intel.getOpponentCard().map(card -> card.relativeValue(intel.getVira())).orElse(0);
        return myBiggerCardValue >= opponentCardValue;
    }
}