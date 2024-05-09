package com.joao.alexandre.jormungandrbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class JormungandrBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(getManilhaCountInHand(intel) > 0)
            return true;

        if(intel.getOpponentScore() <= 5)
            return getCardCountInHandHigherThanRelativeValue(intel, 6) >= 2;

        return getCardCountInHandHigherThanRelativeValue(intel, 7) >= 2;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        //se empatar rodada passada e for o segundo truca
        if(getCurrentRoundNumber(intel) > 1 &&
                getLastRoundResult(intel) == GameIntel.RoundResult.DREW &&
                isSecondToPlay(intel))
            return true;

        //segundo a jogar na primeira rodada e nao consegue matar fica quieto
        if(isSecondToPlay(intel) &&
                getCurrentRoundNumber(intel) == 1 &&
                getLowestCardToBeatOpponentsCard(intel).isEmpty())
            return false;

        //se tem manilha truca
        if(getManilhaCountInHand(intel) > 0)
            return true;

        //se tiver mao boa 8= dois 2 em média
        if(intel.getOpponentScore() <= 5)
            return true;

        return getCardCountInHandHigherThanRelativeValue(intel, 7) >= 2;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (getCurrentRoundNumber(intel)) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> null;
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(getManilhaCountInHand(intel) > 0 ||
                getCardCountInHandHigherThanRelativeValue(intel, 7) >= 2)
            return 1;

        if(getCardCountInHandHigherThanRelativeValue(intel, 6) >= 2)
            return 0;

        return -1;
    }

    @Override
    public String getName() {
        return "Jörmungandr";
    }

    CardToPlay chooseCardFirstRound(GameIntel intel) {
        if(isSecondToPlay(intel))
            return CardToPlay.of(
                    getLowestCardToBeatOpponentsCard(intel)
                            .orElse(getCardToTieOpponentsCard(intel)
                                    .orElse(getLowestCardInHand(intel)))
            );

        if(getManilhaCountInHand(intel) == 1 &&
                getCardCountInHandHigherThanRelativeValue(intel, 7) >= 2) {

            return CardToPlay.of(
                    getHighestNonManilhaCardInHand(intel)
                            .orElse(getLowestCardInHand(intel))
            );
        }

        return CardToPlay.of(getHighestCardInHand(intel));
    }

    CardToPlay chooseCardSecondRound(GameIntel intel) {
        GameIntel.RoundResult lastRoundResult = getLastRoundResult(intel);

        if(lastRoundResult == GameIntel.RoundResult.LOST)
            return CardToPlay.of(
                    getLowestCardToBeatOpponentsCard(intel)
                            .orElse(getHighestCardInHand(intel))
            );

        if(lastRoundResult == GameIntel.RoundResult.DREW)
            return CardToPlay.of(getHighestCardInHand(intel));

        if(getManilhaCountInHand(intel) == 1)
            return CardToPlay.of(getHighestCardInHand(intel));

        return CardToPlay.of(getLowestCardInHand(intel));
    }

    CardToPlay chooseCardThirdRound(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    TrucoCard getHighestCardInHand(GameIntel intel) {
        TrucoCard currentHighestCard = intel.getCards().get(0);
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentHighestCard, vira) > 0)
                currentHighestCard = card;
        }

        return currentHighestCard;
    }

    TrucoCard getLowestCardInHand(GameIntel intel) {
        TrucoCard currentLowestCard = intel.getCards().get(0);
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentLowestCard, vira) < 0)
                currentLowestCard = card;
        }

        return currentLowestCard;
    }

    Optional<TrucoCard> getLowestCardToBeatOpponentsCard(GameIntel intel) {
        TrucoCard currentLowestCard = getHighestCardInHand(intel);
        TrucoCard opponentsCard = intel.getOpponentCard()
                .orElseThrow(() -> new NoSuchElementException("Opponent doesn't have a card to beat"));
        TrucoCard vira = intel.getVira();

        Optional<TrucoCard> optionalTrucoCard = Optional.empty();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentLowestCard, vira) <= 0 &&
                    card.compareValueTo(opponentsCard, vira) > 0) {

                optionalTrucoCard = Optional.of(card);
                currentLowestCard = card;
            }
        }

        return optionalTrucoCard;
    }

    Optional<TrucoCard> getCardToTieOpponentsCard(GameIntel intel) {
        TrucoCard opponentsCard = intel.getOpponentCard()
                .orElseThrow(() -> new NoSuchElementException("Opponent doesn't have a card to tie"));
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(opponentsCard, vira) == 0)
                return Optional.of(card);
        }

        return Optional.empty();
    }

    Optional<TrucoCard> getHighestNonManilhaCardInHand(GameIntel intel) {
        TrucoCard currentHighestCard = getLowestCardInHand(intel);
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> cardToReturn = Optional.empty();

        for (TrucoCard card : intel.getCards()) {
            if(card.compareValueTo(currentHighestCard, vira) >= 0 && !card.isManilha(vira)){
                currentHighestCard = card;
                cardToReturn = Optional.of(card);
            }
        }

        return cardToReturn;
    }

    boolean isSecondToPlay(GameIntel intel){
        return intel.getOpponentCard().isPresent();
    }

    int getCurrentRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    boolean hasPlayedACard(GameIntel intel){int cardsInHand = intel.getCards().size();

        switch(getCurrentRoundNumber(intel)) {
            case 1:
                if(cardsInHand < 3)
                    return true;

            case 2:
                if(cardsInHand < 2)
                    return true;

            case 3:
                if(cardsInHand < 1)
                    return true;

            default:
                return false;
        }
    }

    Optional<TrucoCard> getSelfCardPlayed(GameIntel intel) {
        if (hasPlayedACard(intel)){
            TrucoCard card = intel.getOpenCards().get(
                    (getCurrentRoundNumber(intel) * 2) - 1
            );

            return Optional.of(card);
        }

        return Optional.empty();
    }

    int getManilhaCountInHand(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int manilhaCount = 0;

        for(TrucoCard card : intel.getCards()) {
            if(card.isManilha(vira))
                manilhaCount++;
        }

        if(hasPlayedACard(intel) &&
                intel.getOpenCards().get(intel.getOpenCards().size()-1).isManilha(vira))
            manilhaCount++;

        return manilhaCount;
    }

    int getCardCountInHandHigherThanRelativeValue(GameIntel intel, int relativeValue) {
        TrucoCard vira = intel.getVira();
        int cardCount = 0;

        for(TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) > relativeValue)
                cardCount++;
        }

        // if bot has already played a card, account for the card played
        if(getSelfCardPlayed(intel)
                .orElse(TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN))
                .relativeValue(vira) > relativeValue)
            cardCount++;

        return cardCount;
    }

    double getAverageValueOfTwoHighestCards(GameIntel intel) {
        List<Integer> valores = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        for(TrucoCard card : intel.getCards()) {
            valores.add(card.relativeValue(vira));
        }

        Optional<TrucoCard> selfCardPlayed = getSelfCardPlayed(intel);

        if(selfCardPlayed.isPresent())
            valores.add(
                    getSelfCardPlayed(intel)
                            .orElse(TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN))
                            .relativeValue(vira)
            );

        if(valores.size() > 2)
            valores.remove(Collections.min(valores));

        int sum = valores.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return (double) sum / valores.size();
    }

    GameIntel.RoundResult getLastRoundResult(GameIntel intel) {
        if(intel.getRoundResults().isEmpty())
            throw new ArrayIndexOutOfBoundsException("There is no last round to get the result of");

        return intel.getRoundResults().get(
                intel.getRoundResults().size() - 1
        );
    }

    double getAverageValueOfHand(GameIntel intel) {
        List<Integer> valores = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        for(TrucoCard card : intel.getCards()) {
            valores.add(card.relativeValue(vira));
        }

        Optional<TrucoCard> selfCardPlayed = getSelfCardPlayed(intel);

        if(selfCardPlayed.isPresent())
            valores.add(
                    getSelfCardPlayed(intel)
                            .orElse(TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN))
                            .relativeValue(vira)
            );

        int sum = valores.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return (double) sum / valores.size();
    }

    boolean hasCopasAndZapInHand(GameIntel intel) {return false;}
}
