package com.brunotiago.jeckiechanbot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;
import java.util.List;
import java.util.Optional;

public final class StrategyRoundThree extends Strategy {
    GameIntel intel;
    List<TrucoCard> cards;
    Optional<TrucoCard> opponentCard;
    TrucoCard vira;
    List<TrucoCard> manilhas;
    RoundResult firstRoundResult;
    RoundResult secondRoundResult;

    public StrategyRoundThree(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = sortCards(intel.getCards(), vira);
        this.manilhas = getManilhas(cards, vira);
        this.opponentCard = intel.getOpponentCard();
        this.firstRoundResult = intel.getRoundResults().get(0);
        this.secondRoundResult = intel.getRoundResults().get(1);
    }

    @Override
    CardToPlay chooseCard() {
        return CardToPlay.of(cards.get(0));
    }

    @Override
    int getRaiseResponse() {
        if (firstRoundResult == RoundResult.WON && secondRoundResult == RoundResult.LOST){
            if (getCardValue(cards.get(0), vira) >= 10) return 0;
            if (getCardValue(cards.get(0), vira) >= 12) return 1; // verificar depois
        }

        if (firstRoundResult == RoundResult.LOST && secondRoundResult == RoundResult.WON) {
            TrucoCard playedCard = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
            if (getCardValue(playedCard, vira) >= 12) return 1;
            if (getCardValue(playedCard, vira) >= 10) return 0;
        }

        return -1;
    }

    @Override
    boolean decideIfRaises() {
        if (firstRoundResult == RoundResult.WON && secondRoundResult == RoundResult.LOST) {
            if (opponentCard.isPresent()){
                return opponentCard.get().compareValueTo(cards.get(0), vira) < 0;
            }
        }
        if (firstRoundResult == RoundResult.LOST && secondRoundResult == RoundResult.WON) {
            return getCardValue(cards.get(0), vira) >= CardRank.TWO.value();
        }
        // DRAW, DRAW
        return opponentCard.map(trucoCard -> trucoCard.compareValueTo(cards.get(0), vira) < 0)
                .orElseGet(() -> getCardValue(cards.get(0), vira) >= CardRank.TWO.value());
    }
}
