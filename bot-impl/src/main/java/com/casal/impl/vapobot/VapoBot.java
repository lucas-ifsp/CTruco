package com.casal.impl.vapobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class VapoBot implements BotServiceProvider {
    private final List<TrucoCard> opponentCardsThatHaveBeenPlayed = new ArrayList<>();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (getAmountOfManilhas(intel) > 0)
            return true;
        if (intel.getOpponentScore() > 7) {
            return getAverageCardValue(intel) > 7;
        }
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (getAmountOfManilhas(intel) > 0)
            return true;
        if (intel.getOpponentScore() > 7) {
            return getAverageCardValue(intel) > 7;
        }
        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        switch (getRoundNumber(intel)){
            case 1: return chooseCardFirstRound(intel);
            case 2: return chooseCardSecondRound(intel);
            case 3: return chooseCardLastRound(intel);
            default: return CardToPlay.of(intel.getCards().get(0));
        }

    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (getAmountOfManilhas(intel) > 0) return 1;
        if (getAverageCardValue(intel) > 8) return 1;
        if (getAverageCardValue(intel) > 6) return 0;
        return -1;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    private CardToPlay chooseCardFirstRound(GameIntel intel){
        if (checkIfWillBeTheFirstToPlay(intel)){
            if (getAmountOfManilhas(intel) >= 2) return CardToPlay.of(getHighestCard(intel));
            if (getAmountOfManilhas(intel) == 1){
                if (numberOfCardsThatHasARelativeValueGreaterThan(intel, 7) > 1)
                    return CardToPlay.of(getHighestCardThatIsNotAManilha(intel).orElse(getLowestCard(intel)));
                return CardToPlay.of(getHighestCardThatIsNotAManilha(intel).orElse(getHighestCard(intel)));
            }
        } else {
            return CardToPlay.of(getLowestCardToWin(intel).orElse(getLowestCard(intel)));
        }
        return CardToPlay.of(getHighestCard(intel));
    }

    private CardToPlay chooseCardSecondRound(GameIntel intel){
        if(checkIfWillBeTheFirstToPlay(intel)) {
            if (getLastRoundResult(intel).equals(GameIntel.RoundResult.WON)){
                if (getAmountOfManilhas(intel) == 2) return CardToPlay.of(getLowestCard(intel));
                if (getAmountOfManilhas(intel) == 1) return CardToPlay.of(getHighestCard(intel));
                return CardToPlay.of(getLowestCard(intel));
            }
            return CardToPlay.of(getHighestCard(intel));
        }
        return CardToPlay.of(getHighestCard(intel));
    }

    private CardToPlay chooseCardLastRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }


    public TrucoCard getHighestCard(GameIntel intel) {
        TrucoCard highestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards())
            if (card.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira()))
                highestCard = card;
        return highestCard;
    }

    public TrucoCard getLowestCard(GameIntel intel) {
        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards())
            if (lowestCard.relativeValue(intel.getVira()) > card.relativeValue(intel.getVira()))
                lowestCard = card;
        return lowestCard;
    }

    double getAverageCardValue(GameIntel intel) {
        int values = 0;

        for (TrucoCard card : intel.getCards())
            values += card.relativeValue(intel.getVira());

        double average = (double) values / intel.getCards().size();
        return average;
    }

    boolean hasZap(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        boolean zap = false;

        for (TrucoCard card : myCards)
            if (card.isZap(vira))
                zap = true;
        return zap;
    }

    GameIntel.RoundResult getLastRoundResult(GameIntel intel) {
        if (intel.getRoundResults().isEmpty())
            throw new ArrayIndexOutOfBoundsException("There is no last round played.");
        return intel.getRoundResults().get(intel.getRoundResults().size()-1);
    }

    int getRoundNumber (GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    int getAmountOfManilhas(GameIntel intel){
        int amount = 0;
        for (TrucoCard card: intel.getCards())
            if(card.isManilha(intel.getVira()))
                amount += 1;
        return amount;
    }

    boolean checkIfOpponentCardIsBad(GameIntel intel){
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new NoSuchElementException("O oponente ainda não jogou a carta dele"));
        return opponentCard.relativeValue(intel.getVira()) < 7;
    }

    Optional<TrucoCard> getLowestCardToWin(GameIntel intel) {
        TrucoCard comparisonCard = getHighestCard(intel);
        Optional<TrucoCard> cardToPlay = Optional.empty();
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new NoSuchElementException("O oponente ainda não jogou a carta dele"));
        int opponentCardRelativeValue = opponentCard.relativeValue(vira);
        for (TrucoCard card : intel.getCards()) {
            if (opponentCardRelativeValue < card.relativeValue(vira) && card.relativeValue(vira) <= comparisonCard.relativeValue(vira)) {
                comparisonCard = card;
                cardToPlay = Optional.of(card);
            }
        }
        return cardToPlay;
    }

    Optional<TrucoCard> getHighestCardThatIsNotAManilha(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        if(getAmountOfManilhas(intel) == 2) return Optional.of(getLowestCard(intel));
        if (getAmountOfManilhas(intel) == 1)
            for (TrucoCard card : intel.getCards())
                if (!card.isManilha(vira) && !card.equals(getLowestCard(intel)))
                    return Optional.of(card);
        return Optional.empty();
    }

    boolean checkIfWillBeTheFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    boolean hasAdvantage (GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        int botScore = intel.getScore();
        int difference = 0;

        if (botScore > opponentScore) {
            difference = botScore - opponentScore;
            return difference >= 3;
        }
        return false;
    }

    int numberOfCardsThatHasARelativeValueGreaterThan(GameIntel intel, int relativeValue) {
        int count = 0;

        for (TrucoCard card : intel.getCards())
            if (card.relativeValue(intel.getVira()) > relativeValue) count++;
        return count;
    }
}