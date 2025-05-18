package com.grupo.firsts;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class PeNaCova implements BotServiceProvider {
  @Override
  public boolean getMaoDeOnzeResponse(GameIntel intel) {
    long highCards = intel.getCards().stream()
        .filter(
            card->card.getRank() == CardRank.JACK
                || card.getRank() == CardRank.QUEEN
                || card.getRank() == CardRank.KING
                || card.getRank() == CardRank.ACE
                || card.isManilha(intel.getVira()))
        .count();
    return highCards>=2;
  }

  @Override
  public boolean decideIfRaises(GameIntel intel) {
    return false;
  }

  @Override
  public CardToPlay chooseCard(GameIntel intel) {
    List<TrucoCard> hand = intel.getCards();

    TrucoCard worstCard = hand.stream()
        .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
        .orElse(hand.get(0));
    return CardToPlay.of(worstCard);

  }

  @Override
  public int getRaiseResponse(GameIntel intel) {
    return 0;
  }
}
