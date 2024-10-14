package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class TrucoUtils {

    public static boolean isHighChangesOpponentRunFromTruco(GameIntel intel) {
        return false;
    }

    public static boolean opponentPlayedInvincibleCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        if (intel.getOpponentCard().isPresent()) {
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            return !cards.stream()
                    .filter(
                         card -> card.getRank().value() > opponentCard.get().getRank().value()
                    )
                    .toList().isEmpty();
        }
        return false;
    }

    public static TrucoCard getLowestCardToWinHand(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        if (opponentPlayedInvincibleCard(intel)) return null;
        List<TrucoCard> possibleCardsToWin = cards.stream()
                .filter(
                        card -> card.getRank().value() > opponentCard.get().getRank().value()
                )
                .toList();
        return getLowestCard(possibleCardsToWin, vira);
    }

    public static TrucoCard getGreatestCard(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard greatestCard = cards.get(0);
        for (TrucoCard card : cards ) {
            if (card.compareValueTo(greatestCard, vira) > 0) greatestCard = card;
        }
        return greatestCard;
    }

    public static TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard lowestCard = cards.get(0);
        for (TrucoCard card : cards ) {
            if (card.compareValueTo(lowestCard, vira) < 0) lowestCard = card;
        }
        return lowestCard;
    }

    public static int numberOfManilhas(List<TrucoCard> cards, TrucoCard vira) {
        int numberOfManilhas;
        numberOfManilhas = cards.stream().filter(card -> card.isManilha(vira)).toList().size();
        return numberOfManilhas;
    }

    public static int getNumberOfHighRankCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfHighCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()>8 || handCard.isManilha(vira)) numberOfHighCards++;
        }
        return numberOfHighCards;
    }
    public static boolean isWinning(int myScore, int opponentScore){
        return myScore > opponentScore;
    }

    public static Float getProbabilityOfAbsolutVictoryHand(List<TrucoCard> playersHand, TrucoCard vira) {
        // absolut victory ( when a players cards play againts an opponent hand´s
        // that can´t win or draw in any possible Hand´s played in a Round)

        // is when the median value player´s card is greater or
        // equal than the greater than the greater card of the opponent
        // and the highest player card is greater than the media

        TrucoCard medianCard = getMedianValue(playersHand, vira);

        int numberOfCard = getNumberOfCardWorstThanMedianCard(medianCard, vira);

        return ((float) (numberOfCard * (numberOfCard - 1) * (numberOfCard - 2)) /(38*37*36));
    }

    public static TrucoCard getMedianValue(List<TrucoCard> playersHand, TrucoCard vira){
        TrucoCard greatestCard = getGreatestCard(playersHand, vira);
        TrucoCard lowestCard = getLowestCard(playersHand, vira);
        if(playersHand.get(0).equals(greatestCard) && playersHand.get(1).equals(lowestCard))
            return playersHand.get(2);
        if(playersHand.get(0).equals(greatestCard) && playersHand.get(2).equals(lowestCard))
            return playersHand.get(1);
        return playersHand.get(0);
    }

    public static int getNumberOfCardWorstThanMedianCard(TrucoCard medianCard, TrucoCard vira) {
        int medianCardRank = medianCard.relativeValue(vira);
        if (medianCard.isManilha(vira)) {
            switch (medianCardRank) {
                case 12: return 38;
                case 11: return 37;
                case 10: return 36;
            }
        }
        if(medianCardRank == 1)
            return 0;
        if (vira.relativeValue(vira) >= medianCardRank) {
            return ( medianCardRank - 1) * 4 ;
        }
        return ( medianCardRank * 4) - 5;
    }

    public static boolean theBotPlaysFirst(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    public static boolean isTheFirstRound(GameIntel intel) {
        return intel.getRoundResults().isEmpty();
    }

    public static boolean isTheSecondRound(GameIntel intel) {
        return intel.getRoundResults().size() == 1;
    }

    public static List<TrucoCard> haveStrongestCard(GameIntel intel, List<TrucoCard> myCards) {
        TrucoCard opponentCard = intel.getOpponentCard().get();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> strongestCards = new java.util.ArrayList<>(List.of());
        for (TrucoCard myCard : myCards) {
            if(myCard.compareValueTo(opponentCard, vira) > 0) strongestCards.add(myCard);
        }
        return strongestCards;
    }

    public static int getNumberOfMediumRankCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfMediumCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()<=7 && handCard.getRank().value()>4 && !handCard.isManilha(vira)) numberOfMediumCards++;
        }
        return numberOfMediumCards;
    }

    public static int getNumberOfLowRankCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfLowCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()<=4 && !handCard.isManilha(vira)) numberOfLowCards++;
        }
        return numberOfLowCards;
    }

    public static boolean winFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public static boolean drewFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW;
    }

    public static boolean lostFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
    }

    public static boolean winSecondRound(GameIntel intel) {
        return intel.getRoundResults().get(1) == GameIntel.RoundResult.WON;
    }

    public static boolean lostSecondRound(GameIntel intel) {
        return intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST;
    }
}
