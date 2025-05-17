package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ZecaTatuBot implements BotServiceProvider {
    private TrucoCard highCard;
    private TrucoCard lowCard;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int score = intel.getOpponentScore();
        int hand = handValue(intel);
        long manilhas = countManilha(intel);

        return switch (score) {
            case 0, 1, 2 -> hand >= 14;
            case 3, 4     -> hand >= 17;
            case 5, 6     -> hand >= 20;
            case 7, 8     -> hand >= 24;
            case 9, 10    -> hand >= 27 || manilhas >= 2;
            default       -> false;
        };
    }


    @Override
    public boolean decideIfRaises(GameIntel intel) {
        String round = roundCheck(intel);
        if (round.equals("Round 1")) {
            return handValue(intel) < 10;
        }
        if (round.equals("Round 2")) {
            if (drewFirstRound(intel)) return true;
            if (!wonFirstRound(intel) && countManilha(intel) == 2) return true;
            if (handValue(intel) > 16) return true;
        }
        if (round.equals("Round 3")) {
            return countManilha(intel) >= 1;
        }
        return false;
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        String round = roundCheck(intel);
        TrucoCard cardToPlay;

        switch (round) {
            case "Round 1":
                if (countManilha(intel) >= 2) {
                    cardToPlay = getLowCard(intel);
                    return CardToPlay.discard(cardToPlay);
                } else if (handValue(intel) > 20) {
                    cardToPlay = getHighCard(intel);
                    return CardToPlay.of(cardToPlay);
                } else if (handValue(intel) < 10) {
                    cardToPlay = getLowCard(intel);
                    return CardToPlay.discard(cardToPlay);
                } else {
                    cardToPlay = getMidCard(intel);
                    if (cardToPlay == null) cardToPlay = getLowCard(intel);
                    return CardToPlay.of(cardToPlay);
                }

            case "Round 2":
                if (wonFirstRound(intel)) {
                    cardToPlay = getLowCard(intel);
                    return CardToPlay.discard(cardToPlay);
                } else {
                    cardToPlay = getHighCard(intel);
                    return CardToPlay.of(cardToPlay);
                }

            default:
                cardToPlay = intel.getCards().get(0); // fallback
                return CardToPlay.of(cardToPlay);
        }
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getCards().size() == 3) {
            if (countManilha(intel) >= 2 ){
                return 1;
            }
            if (countManilha(intel) >= 1 && handValue(intel) >= 24) {
                return 0;
            }
        }
        else if (intel.getCards().size() == 2) {
            if (countManilha(intel) == 2) {
                return 1;
            }
            if (countManilha(intel) >= 1 || handValue(intel) >= 17) {
                return 0;
            }
        }
        else if (intel.getCards().size() == 1){
            if (handValue(intel) >= 12){
                return 1;
            }
            if (handValue(intel) >= 9){
                return 0;
            }
        }

        return -1;
    }

    public String roundCheck(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        int cardCount = cards.size();

        return switch (cardCount) {
            case 3 -> "Round 1";
            case 2 -> "Round 2";
            case 1 -> "Round 3";
            default -> "No cards";
        };
    }


    public long countManilha (GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    public int handValue(GameIntel intel){
        int handSValue = 0;
        for (TrucoCard card : intel.getCards()){
            handSValue += card.relativeValue(intel.getVira());
        }
        return handSValue;
    }

    public TrucoCard getHighCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        this.highCard = intel.getCards().stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
        return highCard;
    }

    public TrucoCard getLowCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        this.lowCard = intel.getCards().stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
        return lowCard;
    }


    public TrucoCard getMidCard(GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> !Objects.equals(card, highCard) && !Objects.equals(card, lowCard))
                .findFirst()
                .orElse(null);
    }

    public boolean wonFirstRound(GameIntel intel){
        if(intel.getRoundResults().isEmpty()) return false;
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public boolean drewFirstRound(GameIntel intel){
        if(intel.getRoundResults().isEmpty()) return false;
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW;
    }

}
