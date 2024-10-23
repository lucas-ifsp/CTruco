package com.campos.turazzi.reidozap;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ReiDoZap implements BotServiceProvider {
    private List<TrucoCard> myCards;
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard worstCard;
    private TrucoCard secondBestCard;


    public ReiDoZap() {
        myCards = new ArrayList<>();
    }

    private void updateBotCards(GameIntel intel) {
        this.vira = intel.getVira();
        this.myCards = Objects.requireNonNullElse(intel.getCards(), new ArrayList<>());
        updateCardRankings();
    }

    private double calculateRisk(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        if (opponentScore >= 10) {
            return 1.0;
        }
        return opponentScore / 12.0;
    }

    private boolean hasZap(GameIntel intel) {
        boolean zap = false;

        for(TrucoCard card: myCards) {
            if(card.isZap(vira)) zap = true;
        }

        return zap;
    }
    private boolean analyzeCardStrengths(GameIntel intel) {
        updateBotCards(intel);
        int strongCards = countStrongCards();

        if (hasZap(intel) && strongCards > 1) {
            return true;
        }

        double risk = calculateRisk(intel);

        if (intel.getOpponentScore() > intel.getScore() && risk > 0.5) {
            return true;
        }

       return shouldBluff(intel);
    }

    private boolean favorableHistory(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int wins = 0;
        int losses = 0;

        for (GameIntel.RoundResult result : roundResults) {
            if (result.equals(GameIntel.RoundResult.WON)) {
                wins++;
            } else if (result.equals(GameIntel.RoundResult.LOST)) {
                losses++;
            }
        }

        if (wins > losses) {
            return true;
        } else if (losses > wins) {
            return false;
        }


        return analyzeCardStrengths(intel);
    }

    private boolean shouldBluff(GameIntel intel) {
        double bluffThreshold = 0.5;

        if(intel.getScore() >= 10) {
            return false;
        }

        int strongCards = countStrongCards();

        if(strongCards >= 2) {
            return false;
        }

        if(intel.getScore() < 6) {
            return true;
        }

        return calculateRisk(intel) > bluffThreshold;

    }

    private CardToPlay chooseCardWithBluff(GameIntel intel) {
        if (shouldBluff(intel)) {
            return CardToPlay.of(worstCard);
        }
        updateBotCards(intel);
        int secondBestCardValue = secondBestCard.relativeValue(vira);
        int worstCardValue = worstCard.relativeValue(vira);

        List<GameIntel.RoundResult> results = intel.getRoundResults();

        if(results.get(0) == GameIntel.RoundResult.WON) {
            if(intel.getOpponentCard().isPresent()) {
                int opponentCardValue = intel.getOpponentCard().get().relativeValue(vira);
                return chooseWinningCard(intel, opponentCardValue, worstCardValue,
                        secondBestCardValue, 0);
            }
            else {
                decideIfRaises(intel);
            }
        }

        if(results.get(0) == GameIntel.RoundResult.DREW) {
            decideIfRaises(intel);
        }

        return chooseCardBasedOnQuality(secondBestCardValue);

    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        updateBotCards(intel);
        if (intel.getOpponentScore() == 11) {
            return true;
        }
        if (intel.getOpponentScore() < 9 && analyzeCardStrengths(intel)) {
            return true;
        }
        return hasStrongCards();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        updateBotCards(intel);
        double risk = calculateRisk(intel);

        if (intel.getRoundResults().isEmpty() && countStrongCards() >= 2 && risk < 0.5) {
            return true;
        } else if (intel.getRoundResults().size() == 1 && intel.getScore() > intel.getOpponentScore() && risk < 0.7) {
            return true;
        } else return favorableHistory(intel);
    }

    private void identifyBestCard() {
        bestCard = myCards.get(0);
        for (TrucoCard card : myCards) {
            if (card.relativeValue(vira) > bestCard.relativeValue(vira)) {
                bestCard = card;
            }
        }
    }

    private void identifyWorstCard() {
        worstCard = myCards.get(0);
        for (TrucoCard card : myCards) {
            if (card.relativeValue(vira) < worstCard.relativeValue(vira)) {
                worstCard = card;
            }
        }
    }

    private void identifySecondBestCard() {
        secondBestCard = null;
        for (TrucoCard card : myCards) {
            if (card != bestCard && (secondBestCard == null || card.relativeValue(vira) > secondBestCard.relativeValue(vira))) {
                secondBestCard = card;
            }
        }
        if (secondBestCard == null) {
            secondBestCard = worstCard;
        }
    }
    private void updateCardRankings() {
        if (!myCards.isEmpty()) {
            identifyBestCard();
            identifyWorstCard();
            identifySecondBestCard();
        } else {
            initializeCardValuesAsClosed();
        }
    }

    private void initializeCardValuesAsClosed() {
        bestCard = TrucoCard.closed();
        secondBestCard = TrucoCard.closed();
        worstCard = TrucoCard.closed();
    }

    private CardToPlay ChooseFirstCard(GameIntel intel) {
        int bestCardValue = bestCard.relativeValue(vira);
        int secondBestCardValue = secondBestCard.relativeValue(vira);
        int worstCardValue = worstCard.relativeValue(vira);

        if (intel.getOpponentCard().isPresent()) {
            int opponentCardValue = intel.getOpponentCard().get().relativeValue(vira);
            return chooseWinningCard(intel, opponentCardValue, worstCardValue, secondBestCardValue, bestCardValue);
        }

        if(hasZap(intel) && hasStrongCards()) {
            decideIfRaises(intel);
        }

        return chooseCardBasedOnQuality(secondBestCardValue);
    }

    private CardToPlay chooseWinningCard(GameIntel intel, int opponentCardValue, int worstCardValue, int secondBestCardValue, int bestCardValue) {

        if (worstCardValue >= opponentCardValue) {
            decideIfRaises(intel);
            return CardToPlay.of(worstCard);
        } else if (secondBestCardValue >= opponentCardValue) {
            decideIfRaises(intel);
            return CardToPlay.of(secondBestCard);
        } else if (bestCardValue > opponentCardValue) {
            return CardToPlay.of(bestCard);
        }
        return CardToPlay.of(worstCard);
    }

    private CardToPlay chooseCardBasedOnQuality(int secondBestCardValue) {
        if (secondBestCardValue >= 9) {
            return CardToPlay.of(secondBestCard);
        }
        return CardToPlay.of(bestCard);
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        updateBotCards(intel);

        if (intel.getRoundResults().isEmpty()) {
            return ChooseFirstCard(intel);
        } else if (intel.getRoundResults().size() == 1) {
            return chooseCardWithBluff(intel);
        } else {
            return CardToPlay.of(bestCard);
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        updateBotCards(intel);
        List <GameIntel.RoundResult> results = intel.getRoundResults();
        if(!results.isEmpty()) {
            if(hasZap(intel) && results.get(0) == GameIntel.RoundResult.WON) return 1;
            if(countStrongCards() > 0 && results.get(0) == GameIntel.RoundResult.WON) return 1;
        }
        if(hasZap(intel) && countStrongCards() > 0) {
            return 1;
        }
        if (countStrongCards() > 0) {
            return 0;
        }
        return -1;
    }
    private boolean hasStrongCards() {
        return countStrongCards() > 1;
    }

    private int countStrongCards() {
        int qtdCards = 0;
        if (worstCard.relativeValue(vira) > 7 && !worstCard.isZap(vira)) qtdCards++;
        if (secondBestCard.relativeValue(vira) > 7 && !secondBestCard.isZap(vira)) qtdCards++;
        if (bestCard.relativeValue(vira) > 7 && !bestCard.isZap(vira)) qtdCards++;
        return qtdCards;
    }

}
