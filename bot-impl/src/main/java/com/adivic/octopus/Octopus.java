package com.adivic.octopus;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;
import java.util.stream.Collectors;

import static com.bueno.spi.model.GameIntel.RoundResult.*;

public class Octopus implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return numberOfStrongCards(intel) > 1 ||
                hasManilha(intel) ||
                (!hasManilha(intel) && intel.getOpponentScore() < 7 && numberOfStrongCards(intel) > 0);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        return !roundResults.isEmpty() && (
                numberOfStrongCards(intel) > 1 ||
                        hasManilha(intel) ||
                        (!hasManilha(intel) && roundResults.get(0) == GameIntel.RoundResult.WON && numberOfStrongCards(intel) > 1) ||
                        (numberOfManilhas(intel) > 1 && roundResults.get(0) == GameIntel.RoundResult.LOST) ||
                        numberOfStrongCards(intel) > 2
        );
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return (numberOfStrongCards(intel) >= 2 ||
                (numberOfStrongCards(intel) >= 1 && hasSixPointAdvantage(intel))) ? 1 :
                (numberOfStrongCards(intel) >= 1 || hasThreePointAdvantage(intel)) ? 0 :
                        -1;
    }

    public boolean checkIfWeAreFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }
    public List<TrucoCard> caseOneWhenNoneOfTheCardsAreStrong(GameIntel intel){
        List<TrucoCard> playSequence = sortCards(intel);
        Collections.reverse(playSequence);

        return playSequence;
    }

    public CardToPlay cardToPlayLastRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }

    public List<TrucoCard> caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> cards = sortCards(intel);
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if (roundResults.isEmpty())
            playSequence.addAll(List.of(cards.get(1), cards.get(0)));
         else
            playSequence.addAll(roundResults.get(0) == GameIntel.RoundResult.WON
                    ? List.of(cards.get(0), cards.get(1))
                    : List.of(cards.get(1), cards.get(0)));

        return playSequence;
    }

    public List<TrucoCard> caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();

        playSequence.add(cards.get(1));
        playSequence.add(roundResults.isEmpty() ? cards.get(2) : (roundResults.get(0) == WON ? cards.get(0) : cards.get(2)));
        playSequence.add(roundResults.isEmpty() ? cards.get(0) : (roundResults.get(0) == WON ? cards.get(2) : cards.get(0)));

        return playSequence;
    }

    public List<TrucoCard> caseThreeWhenTwoOfTheCardsAreManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if(roundResults.isEmpty()){
            playSequence.add(cards.get(2));
            playSequence.add(cards.get(0));
        }
        else if (roundResults.get(0) == WON) {
            playSequence.add(cards.get(0));
            playSequence.add(cards.get(1));
        }
        else if (roundResults.get(0) == LOST) {
            playSequence.add(cards.get(1));
            playSequence.add(cards.get(0));
        }
        return playSequence;
    }

    public List<TrucoCard> caseFourWhenAllOfTheCardsAreStrong(GameIntel intel) {
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if(roundResults.isEmpty()){
            playSequence.add(cards.get(2));
            playSequence.add(cards.get(0));
        }
        else if (roundResults.get(0) == WON) {
            playSequence.add(cards.get(0));
            playSequence.add(cards.get(1));
        }
        else if (roundResults.get(0) == LOST) {
            playSequence.add(cards.get(1));
            playSequence.add(cards.get(0));
        }

        return playSequence;
    }

    public CardToPlay cardToPlayFirstRoundWhenZeroStrongCards(GameIntel intel){
        return CardToPlay.of(caseOneWhenNoneOfTheCardsAreStrong(intel).get(0));
    }

    public CardToPlay cardToPlayFirstRoundWhenOneStrongCard(GameIntel intel) {
        return hasManilha(intel)
                ? CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(0))
                : CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(0));
    }

    public CardToPlay cardToPlayFirstRoundWhenTwoStrongCards(GameIntel intel) {
        return CardToPlay.of(
                numberOfManilhas(intel) == 1 ? caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(0) :
                        numberOfManilhas(intel) == 2 ? caseThreeWhenTwoOfTheCardsAreManilha(intel).get(0) :
                                caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(0)
        );
    }

    public CardToPlay cardToPlayFirstRoundWhenThreeStrongCards(GameIntel intel){
        return CardToPlay.of(caseFourWhenAllOfTheCardsAreStrong(intel).get(0));
    }

    public CardToPlay cardToPlaySecondRoundWhenZeroStrongCards(GameIntel intel) {
        return CardToPlay.of(caseOneWhenNoneOfTheCardsAreStrong(intel).get(1));
    }

    public CardToPlay cardToPlaySecondRoundWhenOneOrTwoStrongCards(GameIntel intel) {
        return hasManilha(intel)
                ? CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(1))
                : CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(1));
    }

    public CardToPlay cardToPlaySecondRoundWhenThreeStrongCards(GameIntel intel) {
        return CardToPlay.of(caseFourWhenAllOfTheCardsAreStrong(intel).get(1));
    }

    public CardToPlay cardToPlayFirstRoundIfOpponentPlayFirst(GameIntel intel){
        TrucoCard card = chooseBetterCardToWinTheRound(intel);
        return CardToPlay.of(card);
    }

    public boolean hasManilha(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return cards.stream()
                .limit(3)
                .anyMatch(card -> card.isManilha(vira));
    }

    public int numberOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(hasManilha(intel))
            return (int) cards.stream()
                    .filter(card -> card.isManilha(vira))
                    .count();

        return 0;
    }

    public List<TrucoCard> listOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(hasManilha(intel))
            return cards.stream()
                    .filter(card -> card.getRank() == vira.getRank()
                    .next()).collect(Collectors.toList());

        return Collections.emptyList();
    }

    public boolean hasThree(GameIntel intel){

        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.THREE);
    }

    public int numberOfThreeCards(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        if(hasThree(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.THREE)
                    .count();
        return 0;
    }
    public boolean hasTwo(GameIntel intel){

        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.TWO);
    }

    public int numberOfTwoCards(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        if (hasTwo(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.TWO)
                    .count();
        return 0;
    }

    public boolean hasThreePointAdvantage(GameIntel intel){
        int scoreDifference = intel.getScore() - intel.getOpponentScore();
        if(scoreDifference >= 3 && scoreDifference <= 6)
            return true;
        return false;
    }

    public boolean hasSixPointAdvantage(GameIntel intel){
        int scoreDifference = intel.getScore() - intel.getOpponentScore();
        if(scoreDifference > 6)
            return true;
        return false;
    }

    public boolean hasAce(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.ACE);
    }

    public int numberOfAceCards(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        if (hasAce(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.ACE)
                    .count();
        return 0;
    }

    public TrucoCard chooseBetterCardToWinTheRound(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> ourCards = sortCards(intel);
        List<TrucoCard> openCards = intel.getOpenCards();
        List<TrucoCard> myCardsToWin = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        if (!openCards.isEmpty()) {
            TrucoCard opponentCard = openCards.get(openCards.size() - 1);
            for (TrucoCard card : ourCards) {
                if (card.compareValueTo(opponentCard, vira) > 0) {
                    myCardsToWin.add(card);
                }
            }
            if (!myCardsToWin.isEmpty())
                return myCardsToWin.get(0);
            return ourCards.get(0);
        }
        if(ourCards.get(2).isManilha(vira))
            return ourCards.get(1);
        return ourCards.get(2);
    }

    public int numberOfStrongCards(GameIntel intel) {
        int manilhasCount = numberOfManilhas(intel);
        int threeCount = numberOfThreeCards(intel);
        int twoCount = numberOfTwoCards(intel);
        int aceCount = numberOfAceCards(intel);

        return manilhasCount + threeCount + twoCount + aceCount;
    }

    public List<TrucoCard> sortCards(GameIntel intel) {
        List<TrucoCard> ourCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return ourCards.stream()
                .sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .collect(Collectors.toList());
    }

    public GameIntel.RoundResult whoWonTheRound(GameIntel intel) {
        List<TrucoCard> ourCards = sortCards(intel);
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new IllegalArgumentException("Opponent card is not present"));

        if (hasManilha(intel) && !opponentCard.equals(vira.getRank().next()))
            return WON;

        if (!hasManilha(intel) && opponentCard.equals(vira.getRank().next()))
            return LOST;

        if (hasManilha(intel) && opponentCard.equals(vira.getRank().next()))
            return DREW;

        TrucoCard bestCard = chooseBetterCardToWinTheRound(intel);
        int comparison = bestCard.compareValueTo(opponentCard, vira);

        if (comparison > 0)
            return WON;
        else if (comparison < 0)
            return LOST;
        else
            return DREW;
    }
}
