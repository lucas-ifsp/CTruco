package com.francisco.bruno.pedrohenriquebot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedroHenriqueBot implements BotServiceProvider {

    protected int opponentRaiseCount = 0;

    @Override
    public String getName() {
        return "PedroHenrique";
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int manilhas = countManilhas(intel);
        int highCards = countHighCards(intel);
        double handStrengthAvg = handStrengthAverage(intel);

        if (intel.getOpponentScore() >= 9 && intel.getOpponentScore() < 11) {
            return handStrengthAvg >= 9;
        }

        return (manilhas >= 2 || (manilhas == 1 && highCards >= 2) || highCards == 3);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int manilhas = countManilhas(intel);
        int highCards = countHighCards(intel);
        double handStrengthAvg = handStrengthAverage(intel);

        int roundNumber = intel.getRoundResults().size();

        return switch (roundNumber) {
            case 0 -> decideIfRaisesFirstRound(intel, manilhas, highCards, handStrengthAvg);
            case 1 -> decideIfRaisesSecondRound(intel, manilhas, highCards, handStrengthAvg);
            default -> decideIfRaisesThirdRound(intel, manilhas, highCards, handStrengthAvg);
        };
    }

    private boolean decideIfRaisesFirstRound(GameIntel intel, int manilhas, int highCards, double handStrengthAvg) {
        int opponentScore = intel.getOpponentScore();
        int botScore = intel.getScore();

        if (manilhas >= 1 && highCards >= 1) {
            return true;
        }
        if (opponentScore >= 10) {
            return true;
        }
        if (botScore >= 10 && botScore > opponentScore) {
            return false;
        }

        return decideToBluff(intel, handStrengthAvg);
    }

    private boolean decideIfRaisesSecondRound(GameIntel intel, int manilhas, int highCards, double handStrengthAvg) {
        int botScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();

        if (botScore >= 10) {
            return false;
        }
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && highCards >= 1) {
            return true;
        }
        if (manilhas >= 1 || highCards >= 1) {
            return true;
        }
        if (opponentScore >= 10) {
            return true;
        }

        return decideToBluff(intel, handStrengthAvg);
    }

    private boolean decideIfRaisesThirdRound(GameIntel intel, int manilhas, int highCards, double handStrengthAvg) {
        int botScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();

        if (botScore >= 10) {
            return false;
        }
        if (highCards >= 1) {
            return true;
        }
        if (opponentScore >= 10) {
            return true;
        }
        return decideToBluff(intel, handStrengthAvg);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size();

        return switch (roundNumber) {
            case 0 -> chooseCardFirstRound(intel);
            case 1 -> chooseCardSecondRound(intel);
            default -> chooseCardThirdRound(intel);
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int manilhas = countManilhas(intel);
        int highCards = countHighCards(intel);

        incrementOpponentRaiseCount();

        double handStrengthAvg = handStrengthAverage(intel);
        if (countHighCards(intel) == 0 && countManilhas(intel) == 0)
            return -1;

        if (countManilhas(intel) == 1)
            return 0;

        if (manilhas >= 2 || handStrengthAvg >= 10) {
            return 1;
        }

        return 1;
    }

    private CardToPlay chooseCardFirstRound(GameIntel intel) {
        List<TrucoCard> sortedCards = sortCardsByStrength(intel.getCards(), intel.getVira());

        if (handStrengthAverage(intel) >= 8) {
            return CardToPlay.of(sortedCards.get(0));
        }
        if (intel.getOpponentCard().isEmpty()) {
            return CardToPlay.of(sortedCards.get(1));
        } else {
            return playMinCardToWin(intel, sortedCards);
        }
    }

    private CardToPlay chooseCardSecondRound(GameIntel intel) {
        List<TrucoCard> sortedCards = sortCardsByStrength(intel.getCards(), intel.getVira());

        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            return CardToPlay.of(sortedCards.get(sortedCards.size() - 1));
        } else {
            return playMinCardToWin(intel, sortedCards);
        }
    }

    private CardToPlay chooseCardThirdRound(GameIntel intel) {
        List<TrucoCard> sortedCards = sortCardsByStrength(intel.getCards(), intel.getVira());
        return CardToPlay.of(sortedCards.get(0));
    }

    private boolean decideToBluff(GameIntel intel, double handStrengthAvg) {
        return opponentIsAggressive() && intel.getScore() <= 8 && handStrengthAvg < 7;
    }

    private void incrementOpponentRaiseCount() {
        opponentRaiseCount++;
    }

    private boolean opponentIsAggressive() {
        return opponentRaiseCount > 3;
    }

    private CardToPlay playMinCardToWin(GameIntel intel, List<TrucoCard> sortedCards) {
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()) {
            for (int i = sortedCards.size() - 1; i >= 0; i--) {
                TrucoCard card = sortedCards.get(i);
                if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(card);
                }
            }
            return CardToPlay.of(sortedCards.get(sortedCards.size() - 1));
        } else {
            return CardToPlay.of(sortedCards.get(0));
        }
    }

    private List<TrucoCard> sortCardsByStrength(List<TrucoCard> cards, TrucoCard vira) {
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((card1, card2) -> Integer.compare(card2.relativeValue(vira), card1.relativeValue(vira)));
        return sortedCards;
    }

    public int countManilhas(GameIntel intel){
        return (int) intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    private int countHighCards(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return (int) intel.getCards().stream()
                .filter(card -> !card.isManilha(vira))
                .filter(card -> card.getRank() == CardRank.THREE ||
                        card.getRank() == CardRank.TWO ||
                        card.getRank() == CardRank.ACE)
                .count();
    }

    private double handStrengthAverage(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(vira))
                .average()
                .orElse(0);
    }
}
