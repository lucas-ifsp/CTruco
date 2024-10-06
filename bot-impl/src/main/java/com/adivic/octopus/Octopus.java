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
        if(numberOfStrongCards(intel) > 1 || hasManilha(intel))
            return true;
        else if(!hasManilha(intel) && intel.getOpponentScore() < 7 && numberOfStrongCards(intel) > 0)
            return true;

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if(roundResults.isEmpty())
            return false;
        else if(numberOfStrongCards(intel) > 1 || hasManilha(intel))
            return true;
        else if(!hasManilha(intel) && roundResults.get(0) == WON && numberOfStrongCards(intel) > 1)
            return true;
        else if (numberOfManilhas(intel) > 1 && roundResults.get(0) == LOST)
            return true;
        else if(numberOfStrongCards(intel) > 2)
            return true;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(numberOfStrongCards(intel) >= 2 || (numberOfStrongCards(intel) >= 1 && hasSixPointAdvantage(intel)))
            return 1;
        else if(numberOfStrongCards(intel) >=1 || hasThreePointAdvantage(intel))
            return 0;
        return -1;
    }

    public List<TrucoCard> caseOneWhenNoneOfTheCardsAreStrong(GameIntel intel){
        List<TrucoCard> playSequence = sortCards(intel);
        Collections.reverse(playSequence);

        return playSequence;
    }


    public List<TrucoCard> caseTwoWhenOneOfTheCardsAreStrongAndIsNotManilha(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> cards = sortCards(intel);
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if(roundResults.get(0) == WON) {
            playSequence.add(cards.get(0));
            playSequence.add(cards.get(1));
        } else if(roundResults.get(0) == LOST) {
            playSequence.add(cards.get(1));
            playSequence.add(cards.get(0));
        }
        return playSequence;
    }

    public List<TrucoCard> caseTwoWhenOneOfTheCardsAreStrongAndIsManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(1));
        if (roundResults.get(0) == WON) {
            playSequence.add(cards.get(0));
            playSequence.add(cards.get(2));
        }
        else if (roundResults.get(0) == LOST) {
            playSequence.add(cards.get(2));
            playSequence.add(cards.get(0));
        }

        return playSequence;
    }

    public List<TrucoCard> caseRhreeWhenTwoOfTheCardsAreManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if (roundResults.get(0) == WON) {
            playSequence.add(cards.get(0));
            playSequence.add(cards.get(1));
        }
        else if (roundResults.get(0) == LOST) {
            playSequence.add(cards.get(1));
            playSequence.add(cards.get(0));
        }
        return playSequence;
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

    public TrucoCard chooseBetterCardToWinFirstRound(GameIntel intel) {
        List<TrucoCard> ourCards = sortCards(intel);
        List<TrucoCard> openCards = intel.getOpenCards();
        List<TrucoCard> myCardsToWin = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        if (!openCards.isEmpty()) {
            TrucoCard opponentCard = openCards.get(0);
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

        TrucoCard bestCard = chooseBetterCardToWinFirstRound(intel);
        int comparison = bestCard.compareValueTo(opponentCard, vira);

        if (comparison > 0)
            return WON;
        else if (comparison < 0)
            return LOST;
        else
            return DREW;
    }
}
