package com.meima.skoltable;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class SkolTable implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        boolean isFirstRound = intel.getRoundResults().isEmpty();
        boolean existsOpponentCard = intel.getOpponentCard().isPresent();

        TrucoCard vira = intel.getVira();
        TrucoCard strongestCardInHand = getStrongestCardInHand(intel, vira);
        TrucoCard weakestCardInHand = getWeakestCardInHand(intel, vira);
        TrucoCard opponentCard;

        if(isFirstRound){
            if(existsOpponentCard) {
                opponentCard = intel.getOpponentCard().get();
                if(strongestCardInHand.compareValueTo(opponentCard, vira) > 0){
                    return CardToPlay.of(strongestCardInHand);
                } else {
                    return CardToPlay.of(weakestCardInHand);
                }
            }
            return CardToPlay.of(strongestCardInHand);
        }
        List<TrucoCard> hand = intel.getCards();
        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        boolean isFirstRound = intel.getRoundResults().isEmpty();
        List<GameIntel.RoundResult> rounds = intel.getRoundResults();
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        int handPowerRank = getPowerRankFirstRound(hand, vira);

        if(!isFirstRound){
            if(rounds.get(0).equals(GameIntel.RoundResult.WON)){
                handPowerRank = getPowerRankSecondRound(hand, vira);
                return switch (handPowerRank) {
                    case 4 -> 1;
                    case 3 -> 0;
                    default -> -1;
                };
            }
            return -1;
        }

        return switch (handPowerRank) {
            case 4 -> 1;
            case 3 -> 0;
            default -> -1;
        };
    }

    private TrucoCard getStrongestCardInHand(GameIntel intel, TrucoCard vira) {
        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira))).get();
    }

    private TrucoCard getWeakestCardInHand(GameIntel intel, TrucoCard vira) {
        List<TrucoCard> cards = intel.getCards();

        return cards.stream().min(Comparator.comparingInt(card -> card.relativeValue(vira))).get();
    }

    private int getHandPower(List<TrucoCard> hand, TrucoCard vira){
        int power = 0;
        for (TrucoCard card: hand) {
            power += card.relativeValue(vira);
        }
        return power;
    }


    public int getPowerRankFirstRound(List<TrucoCard> hand, TrucoCard vira) {
        int power = getHandPower(hand, vira);

        if (power >= 28 && power <= 36) {
            return 4;
        } else if (power >= 20 && power <= 27) {
            return 3;
        } else if (power >= 13 && power <= 19) {
            return 2;
        } else {
            return 1;
        }
    }

    public int getPowerRankSecondRound(List<TrucoCard> hand, TrucoCard vira) {
        int power = getHandPower(hand, vira);

        if (power >= 21 && power <= 25) {
            return 4;
        } else if (power >= 16&& power <= 20) {
            return 3;
        } else if (power >= 11&& power <= 15) {
            return 2;
        } else {
            return 1;
        }
    }



}