/*
 *  Copyright (C) 2023 Nathan Hermes Gonçalves da Silva, Pedro Augusto Correia Piassi - IFSP/SCL
 *  Contact: n <dot> hermes <at> aluno <dot> ifsp <dot> edu <dot> br, piassi <dot> pedro <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.CardRank.THREE;
import static com.bueno.spi.model.CardRank.TWO;
import static com.bueno.spi.model.CardSuit.SPADES;

public class MarrecoBot implements BotServiceProvider {
  @Override
  public int getRaiseResponse(GameIntel intel) {
    TrucoCard vira  = intel.getVira();
    List<TrucoCard> cards = intel.getCards();
    List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

    if (roundResults.isEmpty()) {
      List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

      if (manilhas.isEmpty()) {
        List<TrucoCard> wtdThree = cards.stream().filter(card -> card.getRank().equals(THREE)).toList();
        if (wtdThree.size() == 3) {
          return 0;
        }
        return -1;
      } else {
        if (manilhas.size() == 1) {
          Optional<TrucoCard> three = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
          if (three.isPresent()) {
            return 0;
          } else {
            Optional<TrucoCard> two = cards.stream().filter(card -> card.getRank().equals(TWO)).findFirst();
            if (intel.getOpponentScore() <= 7) {
              if (two.isPresent()) {
                return 0;
              }
            }

            return  -1;
          }
        }
        if (manilhas.size() == 2) return 0;
        if (manilhas.size() == 3) return 0;
      }
    } else if (roundResults.size() == 1) {
      if (roundResults.get(0).equals(GameIntel.RoundResult.LOST)) {
        List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();
        if (manilhas.isEmpty()) {
          return -1;
        }
        else if (manilhas.size() == 2) return 0;
        else if (manilhas.size() == 1) {
          Optional<TrucoCard> copas = cards.stream().filter(card -> card.isCopas(vira)).findFirst();
          if (copas.isPresent()) {
            Optional<TrucoCard> three = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
            if (three.isPresent()) return 0;
          }
        }
        else return -1;
      }
    } else if (roundResults.size() == 2) {
      if (roundResults.get(0).equals(GameIntel.RoundResult.WON)) {
        List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

        if (manilhas.isEmpty()) {
          Optional<TrucoCard> cardTwo = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
          if (cardTwo.isPresent()) return 0;
        }
      }
    }

    return -1;
  }

  @Override
  public boolean getMaoDeOnzeResponse(GameIntel intel) {
    TrucoCard vira  = intel.getVira();
    List<TrucoCard> cards = intel.getCards();
    List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

    if (manilhas.isEmpty())
      return false;
    else {
      if (manilhas.size() == 1) {
        Optional<TrucoCard> card3 = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
        if (card3.isPresent()) return true;

        Optional<TrucoCard> two = cards.stream().filter(card -> card.getRank().equals(TWO)).findFirst();
        if (two.isPresent()) {
          if (intel.getOpponentScore() < 4) {
            return true;
          }
        }
      }
      else if (manilhas.size() == 2) return true;
      else if (manilhas.size() == 3) return true;
      else return false;

    }
    return false;
  }

  @Override
  public boolean decideIfRaises(GameIntel intel) {
    TrucoCard vira = intel.getVira();
    List<GameIntel.RoundResult> roundResult = intel.getRoundResults();
    List<TrucoCard> cards = intel.getCards();
    List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

    if (roundResult.isEmpty()) {
      if (manilhas.size() == 1) {
        if (intel.getOpponentScore() < 5) return true;
      } else if (manilhas.size() == 2) {
        if (intel.getOpponentScore() < 6) return true;
      }
    }

    if (roundResult.size() == 1) {
      if (roundResult.get(0).equals(GameIntel.RoundResult.WON)) {
        if (manilhas.isEmpty()) {
          Optional<TrucoCard> cardThree = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
          return cardThree.isPresent();
        } else if (manilhas.size() == 1) {
          Optional<TrucoCard> zap = cards.stream().filter(card -> card.isZap(vira)).findFirst();
          if (zap.isPresent()) return true;

          Optional<TrucoCard> copas = cards.stream().filter(card -> card.isCopas(vira)).findFirst();
          if (copas.isPresent()) return true;

          Optional<TrucoCard> espadilha = cards.stream().filter(card -> card.isEspadilha(vira)).findFirst();
          if (espadilha.isPresent()) return true;

          Optional<TrucoCard> ouros = cards.stream().filter(card -> card.isOuros(vira)).findFirst();
          if (ouros.isPresent()) return true;
        } else if (manilhas.size() == 2) {
          return true;
        }
      } else if (roundResult.get(0).equals(GameIntel.RoundResult.LOST)) {
        if (manilhas.size() == 1) {
          Optional<TrucoCard> copas = cards.stream().filter(card -> card.isCopas(vira)).findFirst();
          if (copas.isPresent()) {
            Optional<TrucoCard> cardThree = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
            return cardThree.isPresent();
          }

          Optional<TrucoCard> espadilha = cards.stream().filter(card -> card.isEspadilha(vira)).findFirst();
          if (espadilha.isPresent()) {
            Optional<TrucoCard> cardThree = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
            return cardThree.isPresent();
          }
        }
        if (manilhas.size() == 2) return true;
      }
    }

    return false;
  }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> openCards = intel.getOpenCards();
        List<GameIntel.RoundResult> roundResult = intel.getRoundResults();

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

    List<TrucoCard> manilhas = cards.stream().filter(_card -> _card.isManilha(vira)).toList();
    if (!manilhas.isEmpty()) {
      Optional<TrucoCard> picaFumo = manilhas.stream().filter(_card -> _card.isOuros(vira)).findFirst();

      if (opponentCard.isPresent()) {
        if (opponentCard.get().isOuros(vira)) {
          if (manilhas.size() == 1) return CardToPlay.of(manilhas.get(0));
          else if (manilhas.size() == 2) {
            CardSuit suitFirstManilha = manilhas.get(0).getSuit();
            CardSuit suitSecondManilha = manilhas.get(1).getSuit();
            int lessSuit = suitFirstManilha.compareTo(suitSecondManilha);

            if (lessSuit < 0) return CardToPlay.of(manilhas.get(0));
            else return CardToPlay.of(manilhas.get(1));
          } else if (manilhas.size() == 3) {
            Optional<TrucoCard> zap = manilhas.stream().filter(_card -> _card.isZap(vira)).findFirst();
            if (zap.isPresent()) return CardToPlay.of(zap.get());
          }
        } else if (opponentCard.get().isEspadilha(vira)) {
          if (manilhas.size() == 1) {
            CardSuit suitManilha = manilhas.get(0).getSuit();
            if (suitManilha.compareTo(SPADES) > 0) return CardToPlay.of(manilhas.get(0));
          } else if (manilhas.size() == 2) {
            List<TrucoCard> notPicaFumo = manilhas.stream().filter(_manilha -> !_manilha.isOuros(vira)).toList();

            if (notPicaFumo.size() == 1) return CardToPlay.of(notPicaFumo.get(0));

            CardSuit suitFirstManilha = notPicaFumo.get(0).getSuit();
            CardSuit suitSecondManilha = notPicaFumo.get(1).getSuit();
            int lessSuit = suitFirstManilha.compareTo(suitSecondManilha);

            if (lessSuit < 0) return CardToPlay.of(manilhas.get(0));
            else return CardToPlay.of(manilhas.get(1));
          }
        } else if (opponentCard.get().isCopas(vira)) {
          Optional<TrucoCard> zap = manilhas.stream().filter(card -> card.isZap(vira)).findFirst();
          if (zap.isPresent()) return CardToPlay.of(zap.get());
        } else if (opponentCard.get().isZap(vira)) {
          List<TrucoCard> noManilhas = cards.stream().filter(card -> !card.isManilha(vira)).toList();

          if (noManilhas.size() == 2) {
            CardRank rankFirstNoManilha = noManilhas.get(0).getRank();
            CardRank rankSecondNoManilha = noManilhas.get(1).getRank();
            int weakRank = rankFirstNoManilha.compareTo(rankSecondNoManilha);

            if (weakRank <= 0) return CardToPlay.of(noManilhas.get(0));
            else return CardToPlay.of(noManilhas.get(1));
          } else if (noManilhas.size() == 1) return CardToPlay.of(noManilhas.get(0));
          else if (noManilhas.size() == 0 && picaFumo.isPresent()) return CardToPlay.of(picaFumo.get());
        } else if (!opponentCard.get().isManilha(vira)) {
          List<TrucoCard> noManilhas = cards.stream().filter(card -> !card.isManilha(vira)).toList();

          if (roundResult.isEmpty()) {
            if (manilhas.size() == 1) {
              Optional<TrucoCard> cardZap = cards.stream().filter(card -> card.isZap(vira)).findFirst();
              if (cardZap.isPresent()) {
                Optional<TrucoCard> cardEqualsOpponent = cards.stream()
                        .filter(card -> card.getRank().equals(opponentCard.get().getRank()))
                        .findFirst();
                if (cardEqualsOpponent.isPresent()) return CardToPlay.of(cardEqualsOpponent.get());
              }
            }
          }

          if (noManilhas.size() == 2) {
            if (picaFumo.isPresent()) {
              List<TrucoCard> greaterCards = noManilhas.stream()
                  .filter(noManilha -> noManilha.getRank().compareTo(opponentCard.get().getRank()) > 0).toList();

              if (greaterCards.isEmpty()) return CardToPlay.of(picaFumo.get());
              else {
                if (greaterCards.size() == 2) {
                  CardRank rankFirstGreaterCard = greaterCards.get(0).getRank();
                  CardRank rankSecondGreaterCard = greaterCards.get(1).getRank();
                  TrucoCard weakGreaterCard = rankFirstGreaterCard
                      .compareTo(rankSecondGreaterCard) <= 0 ? greaterCards.get(0) : greaterCards.get(1);

                  return CardToPlay.of(weakGreaterCard);
                }else if (greaterCards.size() == 1) return CardToPlay.of(greaterCards.get(0));
                else return CardToPlay.of(picaFumo.get());
              }
            }
          }
        }

        //if (picaFumo.isPresent() && !opponentCard.get().isManilha(vira)) return CardToPlay.of(picaFumo.get());
      } else {
        if (picaFumo.isPresent()) return CardToPlay.of(picaFumo.get());
      }
    } else {
      if (opponentCard.isPresent()) {
        if (opponentCard.get().isManilha(vira)) {
          TrucoCard firstCard = cards.get(0);
          TrucoCard lowCard = firstCard;
          for (TrucoCard card : cards) {
            if (firstCard.getRank().compareTo(card.getRank()) > 0) lowCard = card;
          }
          return CardToPlay.of(lowCard);
        } else {
            List<TrucoCard> cardsGreaterOpponentCard = cards.stream().filter(
                    card -> card.getRank().compareTo(opponentCard.get().getRank()) > 0
            ).toList();

            if (!cardsGreaterOpponentCard.isEmpty()) {
                if (cardsGreaterOpponentCard.size() == 1) {
                    return CardToPlay.of(cardsGreaterOpponentCard.get(0));
                }
                if (cardsGreaterOpponentCard.size() == 2) {
                    return CardToPlay.of(cardsGreaterOpponentCard.get(0));
                }
                TrucoCard lowCard = cardsGreaterOpponentCard.get(0);
                for (TrucoCard card : cardsGreaterOpponentCard) {
                    if (lowCard.getRank().compareTo(card.getRank()) >= 0) lowCard = card;
                }
                return CardToPlay.of(lowCard);
            } else {
              TrucoCard firstCard = cards.get(0);
              TrucoCard lowCard = firstCard;
              for (TrucoCard card : cards) {
                if (firstCard.getRank().compareTo(card.getRank()) > 0) lowCard = card;
              }
              return CardToPlay.of(lowCard);
            }
        }
      } else {
          if (openCards.size() == 1) {
              TrucoCard firstCard = cards.get(0);
              TrucoCard greaterCard = firstCard;
              for (TrucoCard card : cards) {
                  if (firstCard.getRank().compareTo(card.getRank()) <= 0) greaterCard = card;
              }
              return CardToPlay.of(greaterCard);
          }

          if (roundResult.isEmpty()) {
            TrucoCard firstCard = cards.get(0);
            TrucoCard greaterCard = firstCard;
            for (TrucoCard card : cards) {
              if (firstCard.getRank().compareTo(card.getRank()) <= 0) greaterCard = card;
            }
            return CardToPlay.of(greaterCard);
          }
      }
    }

    return CardToPlay.of(cards.get(0));
  }
}
