/*
 *  Copyright (C) 2025 Michelle Dantas Luiz Tiburcio - IFSP/SCL - IFSP/SCL
 *  Contact: dantas <dot> michelle <at> aluno <dot> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */
package com.grupo.firsts;

import com.bueno.spi.model.*;
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
    List<TrucoCard> hand = intel.getCards();
    TrucoCard vira = intel.getVira();

    long manilhas = hand.stream().filter(card -> card.isManilha(vira)).count();

    long highCards = hand.stream()
        .filter(card -> {
          CardRank rank = card.getRank();
          return rank == CardRank.ACE || rank == CardRank.KING ||
              rank == CardRank.QUEEN || rank == CardRank.JACK;
        }).count();

    if(manilhas >= 2) return true;
    if (manilhas == 1 && highCards>=1 ) return true;
    if(highCards == 3) return true;

    return false;

  }

  @Override
  public CardToPlay chooseCard(GameIntel intel) {
    List<TrucoCard> hand = intel.getCards();
    List<TrucoCard> openCards = intel.getOpenCards();

    final TrucoCard opponentCard = openCards.size() > 1 ? openCards.get(1) : null;
    final TrucoCard vira = intel.getVira();

    if(opponentCard == null){
      TrucoCard bestCard = hand.stream()
          .max(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
          .orElse(hand.get(0));
      return CardToPlay.of(bestCard);
    } else {
      return hand.stream()
          .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
          .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
          .map(CardToPlay::of)
          .orElseGet( () -> CardToPlay.of(
              hand.stream()
                  .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                  .orElse(hand.get(0))
          ));
    }
  }

  @Override
  public int getRaiseResponse(GameIntel intel) {
    List<TrucoCard> hand = intel.getCards();
    TrucoCard vira = intel.getVira();

    long manilhas = hand.stream().filter(card -> card.isManilha(vira)).count();
    long highCards = intel.getCards().stream()
        .filter(
            card->card.getRank() == CardRank.JACK
                || card.getRank() == CardRank.QUEEN
                || card.getRank() == CardRank.KING
                || card.getRank() == CardRank.ACE
                || card.isManilha(intel.getVira()))
        .count();

    if (manilhas>=2) return 1;
    if (manilhas == 1 && highCards >= 1) return 1;
    if (highCards == 3) return 1;
    if (highCards == 2) return 1;
    return 0;
  }
}
