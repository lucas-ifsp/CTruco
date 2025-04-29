package com.local.aah.refactor.me;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class RefactorMePleaseBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();

        if (opponentScore == 11) {
            return true;
        }
        int handPowerRank = getPowerRankFirstRound(hand, vira);

        return handPowerRank >= 3;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final int elevenHandPoints = 11;
        final int maxHandPoints = 12;
        List<GameIntel.RoundResult> rounds = intel.getRoundResults();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();

        if (intel.getScore() == elevenHandPoints ||
                intel.getOpponentScore() == elevenHandPoints ||
                intel.getHandPoints() == maxHandPoints) {
            return false;
        }

        if (rounds.isEmpty()) {
            int handPowerRank = getPowerRankFirstRound(hand, vira);

            return (handPowerRank == 4 || handPowerRank == 3);
        }

        if (rounds.get(0).equals(GameIntel.RoundResult.WON)) {
            int handPowerRank = getPowerRankSecondRound(hand, vira);
            return (handPowerRank < 3);
        }

        if (rounds.get(0).equals(GameIntel.RoundResult.DREW)) {
            int handPowerRank = getPowerRankSecondRound(hand, vira);
            return (handPowerRank > 3);
        }

        if (rounds.get(0).equals(GameIntel.RoundResult.LOST)) {
            if (hasCopasAndZap(hand, vira)) {
                return true;
            }

            int handPowerRank = getPowerRankSecondRound(hand, vira);
            return (handPowerRank >= 3);
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (isFirstRound(intel)) {
            return chooseCardFirstRound(intel);
        } else {
            return chooseCardSubsequentRound(intel);
        }
    }

    private boolean isFirstRound(GameIntel intel) {
        return intel.getRoundResults().isEmpty();
    }

    private CardToPlay chooseCardFirstRound(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (hasCopasAndZap(hand, vira)) {
            return CardToPlay.of(getWeakestCardInHand(intel, vira));
        }

        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            TrucoCard strongestCardInHand = getStrongestCardInHand(intel, vira);
            TrucoCard weakestCardInHand = getWeakestCardInHand(intel, vira);

            if (strongestCardInHand.compareValueTo(opponentCard, vira) > 0) {
                if (opponentCard.relativeValue(vira) < 8) {
                    return CardToPlay.of(weakestCapableOfWin(opponentCard, vira, hand));
                }
                return CardToPlay.of(strongestCardInHand);
            } else {
                return CardToPlay.of(weakestCardInHand);
            }
        }
        return CardToPlay.of(getStrongestCardInHand(intel, vira));
    }

    private CardToPlay chooseCardSubsequentRound(GameIntel intel) {
        List<GameIntel.RoundResult> rounds = intel.getRoundResults();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();

        if (opponentCardIsHiddenOrZap(intel)) {
            return CardToPlay.of(getWeakestCardInHand(intel, vira));
        }

        if (rounds.get(0).equals(GameIntel.RoundResult.DREW) || rounds.get(0).equals(GameIntel.RoundResult.LOST)) {
            return CardToPlay.of(getStrongestCardInHand(intel, vira));
        }

        if (rounds.get(0).equals(GameIntel.RoundResult.WON)) {
            TrucoCard strongestCardInHand = getStrongestCardInHand(intel, vira);
            TrucoCard weakestCardInHand = getWeakestCardInHand(intel, vira);

            if (strongestCardInHand.isZap(vira) && rounds.size() == 1) {
                return CardToPlay.of(weakestCardInHand);
            }
        }

        return CardToPlay.of(hand.get(0));
    }

    private boolean opponentCardIsHiddenOrZap(GameIntel intel) {
        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            TrucoCard vira = intel.getVira();
            return opponentCard.getRank().equals(CardRank.HIDDEN) || opponentCard.isZap(vira);
        }
        return false;
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        boolean isFirstRound = intel.getRoundResults().isEmpty();
        List<GameIntel.RoundResult> rounds = intel.getRoundResults();
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        int handPowerRank = getPowerRankFirstRound(hand, vira);

        if (isPair(intel)) return 0;

        if (!isFirstRound) {
            if (rounds.get(0).equals(GameIntel.RoundResult.WON)) {
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

    private int getHandPower(List<TrucoCard> hand, TrucoCard vira) {
        int power = 0;
        for (TrucoCard card : hand) {
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
        } else if (power >= 16 && power <= 20) {
            return 3;
        } else if (power >= 11 && power <= 15) {
            return 2;
        } else {
            return 1;
        }
    }

    private boolean isPair(GameIntel intel) {
        long pairCount = intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();

        return pairCount == 2;
    }

    public boolean hasCopasAndZap(List<TrucoCard> hand, TrucoCard vira) {
        boolean hasCopas = false;
        boolean hasZap = false;

        for (TrucoCard card : hand) {
            if (card.isCopas(vira)) {
                hasCopas = true;
            } else if (card.isZap(vira)) {
                hasZap = true;
            }

            if (hasCopas && hasZap) {
                break;
            }
        }

        return hasCopas && hasZap;
    }

    public TrucoCard weakestCapableOfWin(TrucoCard opponentCard, TrucoCard vira, List<TrucoCard> hand) {
        TrucoCard weakestCard = null;

        for (TrucoCard card : hand) {
            if (weakestCard == null || card.compareValueTo(opponentCard, vira) > 0) {
                if (weakestCard == null || card.compareValueTo(weakestCard, vira) < 0) {
                    weakestCard = card;
                }
            }
        }
        return weakestCard;
    }

}