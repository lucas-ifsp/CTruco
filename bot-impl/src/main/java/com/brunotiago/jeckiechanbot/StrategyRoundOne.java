package com.brunotiago.jeckiechanbot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import java.util.List;
import java.util.Optional;


public final class StrategyRoundOne extends Strategy {
    GameIntel intel;
    List<TrucoCard> cards;
    Optional<TrucoCard> opponentCard;
    TrucoCard vira;
    List<TrucoCard> manilhas;

    public StrategyRoundOne(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = sortCards(intel.getCards(), vira);
        this.manilhas = getManilhas(cards, vira);
        this.opponentCard = intel.getOpponentCard();
    }

    @Override
    public CardToPlay chooseCard() {

        if (opponentCard.isPresent()) {
            List<TrucoCard> highestCards = getAllHighestCardsThanOpponentCard(cards, opponentCard.get(), vira);
            Optional<TrucoCard> drawCard = drawCard(cards, opponentCard.get());
            if (getCardValue(cards.get(1), vira) >= CardRank.ACE.value()) return CardToPlay.of(cards.get(0));
            if (!highestCards.isEmpty()) return CardToPlay.of(highestCards.get(0));
            if (!manilhas.isEmpty() && drawCard.isPresent()) return CardToPlay.of(drawCard.get());
            return CardToPlay.of(lowestCard(cards, vira));
        } else {
            if (getCardValue(cards.get(1), vira) >= CardRank.ACE.value()) return CardToPlay.of(cards.get(0));
            return CardToPlay.of(cards.get(2));
        }
    }

    @Override
    public int getRaiseResponse() {
        Optional<TrucoCard> playedCard = intel.getOpenCards().size() > 1 ?
                Optional.ofNullable(intel.getOpenCards().get(1)) : Optional.empty();

        int relativeCardsValue = getCardsValues(cards, vira);

        if (playedCard.isPresent()) {
            if (playedCard.get().isManilha(vira)) {
                if (getCardValue(cards.get(cards.size() - 1), vira) > CardRank.ACE.value()) return 1;
            }
            relativeCardsValue += playedCard.get().relativeValue(vira);
        }

        if (!manilhas.isEmpty() && getCardValue(cards.get(cards.size() - 1), vira) > CardRank.ACE.value()) return 1;

        return relativeCardsValue >= 20 ? 0 : -1;
    }

    @Override
    public boolean decideIfRaises() {
        if (intel.getOpponentScore() > 0) return false; // evita pedir aumento nos casos em que a oferta de aumento do oponente já foi recusada

        int relativeCardsValue = getCardsValues(cards, vira);

        if (opponentCard.isPresent()) {
            List<TrucoCard> highestCard = getAllHighestCardsThanOpponentCard(cards, opponentCard.get(), vira);
            if (getCardValue(cards.get(1), vira) >= CardRank.TWO.value() && !highestCard.isEmpty()) return true;
        }
        if (!manilhas.isEmpty() && getCardValue(cards.get(1), vira) >= CardRank.ACE.value()) return true;
        return !manilhas.isEmpty() && relativeCardsValue >= 20;
    }
}
