/*
 *  Copyright (C) 2023 Murilo Correa Soares Silva, João Pedro Piccino Marafiotti- IFSP/SCL
 *  Contact: coorea <dot> murilo <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: joao <dot> marafiotti <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.murilo.joao.jackbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class JackBot implements BotServiceProvider {

    // Verifica se o bot possui alguma mão para aceitar a mão de onze:
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return hasInvincibleHand(intel) || hasSuperHand(intel) ||
                hasExtremeHand(intel) || hasSuperPlusHand(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        long countThrees = countThrees(intel.getCards(), intel);
        TrucoCard vira = intel.getVira();
        if(intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        if(intel.getOpponentScore() >= 9){
            if(wonFirstRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;
            if(wonFirstRound(intel) && hasMajorPair(intel) || hasMinorPair(intel) || hasMiddlePair(intel) || hasBlackPair(intel) || hasRedPair(intel) || hasAnyPair(intel) || hasExtremeHand(intel) || hasInvincibleHand(intel) || hasSuperPlusHand(intel) ) return true;
            if(drewFirstRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;
            if (wonFirstRound(intel) && countThrees == 2) return true;
            if(wonSecondRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;
            if(hasInvincibleHand(intel) || hasExtremeHand(intel) || hasSuperPlusHand(intel) || hasSuperHand(intel)) return true;
            return false;
        }
        if(hasInvincibleHand(intel) || hasExtremeHand(intel) || hasSuperPlusHand(intel) || hasSuperHand(intel) || hasStrongHand(intel)) return true;
        if(wonFirstRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;

        if(wonFirstRound(intel) && hasMajorPair(intel) || hasMinorPair(intel) || hasMiddlePair(intel) || hasBlackPair(intel) || hasRedPair(intel) || hasAnyPair(intel) || hasExtremeHand(intel) || hasInvincibleHand(intel) || hasSuperPlusHand(intel) ) return true;
        if(drewFirstRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;
        if (wonFirstRound(intel) && countThrees == 2) return true;
        if(wonSecondRound(intel) && hasZap(intel) || hasCopas(intel) || hasEspadilha(intel) || hasOuros(intel)) return true;
        if (countCardsStrongerThanKing(intel) == 3) return true;
        if (wonFirstRound(intel) && getRoundNumber(intel) == 3) {
            int opponentCard = 1;
            if (intel.getOpponentCard().isPresent()) {
                opponentCard = intel.getOpponentCard().get().relativeValue(intel.getVira());
            }
            TrucoCard myLastCard = getHighestCard(intel);
            if (myLastCard.relativeValue(intel.getVira()) == opponentCard) return true;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        // Pegar as cartas da mão do jogador
        List<TrucoCard> cards = intel.getCards();

        // Pegar a carta vira
        TrucoCard vira = intel.getVira();

        // Verificar se o jogador é o primeiro a jogar na rodada atual
        if (isFirstToPlay(intel)) {
            // Verificar se a rodada anterior empatou
            if (drewFirstRound(intel)) {
                // Se for o primeiro a jogar na segunda rodada após empate:
                // Joga a carta mais forte da mão
                return CardToPlay.of(getHighestCard(intel));
            } else {
                // Se for a primeira rodada ou não for empate:
                // Verificar se possui Zap ou Copas
                if (intel.getRoundResults().isEmpty() && hasZap(intel) || hasCopas(intel)) {
                    // Se tiver, joga a segunda carta mais forte da mão
                    return CardToPlay.of(getMiddleCard(intel));
                } else {
                    // Se não tiver, joga a carta mais forte da mão
                    return CardToPlay.of(getHighestCard(intel));
                }
            }
        }  else {
            // Pegar a carta jogada pelo oponente
            int opponentCard = 1;
            if (intel.getOpponentCard().isPresent()) {
                opponentCard = intel.getOpponentCard().get().relativeValue(vira);
            }
            // Tenta encontrar a menor carta necessária para vencer o oponente
            TrucoCard smallestGreaterCard = getSmallestCardToBeatOpponent(cards, vira, opponentCard);

            // Se encontrar, joga a carta
            if (smallestGreaterCard != null) {
                return CardToPlay.of(smallestGreaterCard);
            } else {
                // Se não encontrar, joga a carta mais fraca da mão

                // Caso o bot tenha ganhado a primeira rodada e tenha um zap em sua mão:
                if (wonFirstRound(intel) && hasZap(intel)) {
                    // Encobrir a carta mais fraca na segunda mão
                    if (getRoundNumber(intel) == 2) {
                        TrucoCard weakestCard = getLowestCard(intel);
                        return CardToPlay.discard(weakestCard);
                    }
                    // Jogar normalmente em outras rodadas
                    else {
                        return CardToPlay.of(getHighestCard(intel));
                    }
                } else {
                    // Jogar a carta mais fraca da mão em outros casos
                    return CardToPlay.of(getHighestCard(intel));
                }
            }
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getInstanceByHandPoints(intel.getHandPoints()).getRaiseResponse(intel);
    }

    //Verifica se possui manilha na rodada atual
    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isManilha(intel.getVira()));
    }

    //Verifica quantidad de manilhas na rodada atual
    public int countManilhas(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    //Verifica se possui manilha zap na rodada atual
    public boolean hasZap(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isZap(intel.getVira()));
    }

    //Verifica se tem alguma carta maior que o rei na rodada atual
    public boolean hasCardsStrongerThanKing(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.relativeValue(intel.getVira()) > 7);
    }

    //Verifica a quantidade de cartas menores que a rainha na rodada atual
    public long countThrees(List<TrucoCard> myCards, GameIntel intel) {
        return myCards.stream()
                .filter(card -> card.getRank() == CardRank.THREE && !card.isManilha(intel.getVira()))
                .count();
    }

    //Verifica a quantidade de cartas menores que a rainha na rodada atual
    public int countCardsLessThanQueen(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) < 5 && !card.isManilha(intel.getVira()))
                .count();
    }

    //Verifica se possui manilha copas na rodada atual
    public boolean hasCopas(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isCopas(intel.getVira()));
    }

    //Verifica se possui manilha espadas na rodada atual
    public boolean hasEspadilha(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isEspadilha(intel.getVira()));
    }

    //Verifica se possui manilha ouros na rodada atual
    public boolean hasOuros(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isOuros(intel.getVira()));
    }

    //CASAL MAIOR nas nossas cartas da rodada. Um casal maior = 2 Manilhas (zap e copas)
    public boolean hasMajorPair(GameIntel intel) {
        return hasZap(intel) && hasCopas(intel);
    }

    //CASAL MENOR nas nossas cartas da rodada. Um casal maior = 2 Manilhas (espadas e ouros)
    public boolean hasMinorPair(GameIntel intel) {
        return hasEspadilha(intel) && hasOuros(intel);
    }

    //CASAL MEDIO nas nossas cartas da rodada. Um casal medio = 2 Manilhas (copas e espadas)
    public boolean hasMiddlePair(GameIntel intel) {
        return hasCopas(intel) && hasEspadilha(intel);
    }

    //CASAL PRETO nas nossas cartas da rodada. Um casal preto = 2 Manilhas (zap e espadas)
    public boolean hasBlackPair(GameIntel intel) {
        return hasZap(intel) && hasEspadilha(intel);
    }

    //CASAL VERMELHO nas nossas cartas da rodada. Um casal vermelho = 2 Manilhas (copas e ouros)
    public boolean hasRedPair(GameIntel intel) {
        return hasCopas(intel) && hasOuros(intel);
    }

    // Verifica se o bot possui um "casal" qualquer (duas manilhas)
    public boolean hasAnyPair(GameIntel intel) {
        return countManilhas(intel) == 2;
    }

    //verifica se temos uma MAO IMBATIVEL na rodada. MAO IMBATIVEL = 3 manilhas
    public boolean hasInvincibleHand(GameIntel intel) {
        return countManilhas(intel) == 3;
    }

    //verifica se temos uma MAO EXTREMA na rodada. (1 casal de Manilhas + uma carta mais forte que o Rei
    public boolean hasExtremeHand(GameIntel intel) {
        return hasAnyPair(intel) && hasCardsStrongerThanKing(intel);
    }

    public boolean hasSuperPlusHand(GameIntel intel) {
        // Verifica se a mão possui 2 manilhas e 1 carta menor ou igual ao Rei
        return countManilhas(intel) == 2 && countCardsLessOrEqualToKing(intel) == 1;
    }

    // Verifica se temos uma "MAO SUPER" (1 Manilha + 2 cartas mais fortes que o Rei)
    public boolean hasSuperHand(GameIntel intel) {
        return countManilhas(intel) == 1 && countCardsStrongerThanKing(intel) == 2;
    }

    // Verifica se o jogador possui uma "Mão Forte" (3 cartas mais fortes que o Rei)
    public boolean hasStrongHand(GameIntel intel) {
        return countCardsStrongerThanKing(intel) == 3;
    }

    // Verifica se o jogador possui uma "Mão Boa" (2 cartas mais fortes que o Rei)
    public boolean hasGoodHand(GameIntel intel) {
        return countCardsStrongerThanKing(intel) == 2;
    }

    // Verifica se o jogador possui uma "Mão Ok" (1 carta mais forte que o Rei)
    public boolean hasOkHand(GameIntel intel) {
        return countCardsStrongerThanKing(intel) == 1;
    }

    // Conta quantas cartas o jogador possui que são mais fortes que o Rei
    public int countCardsStrongerThanKing(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) > 7 && !card.isManilha(intel.getVira()))
                .count();
    }

    // Conta quantas cartas o jogador possui que são mais fracas ou igual ao Rei
    public int countCardsLessOrEqualToKing(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) <= 7 && !card.isManilha(intel.getVira()))
                .count();
    }

    // Verifica se uma carta é maior que a Rainha, mas menor que o Rei
    public boolean isBetweenQueenAndKing(TrucoCard card, GameIntel intel) {
        int relativeValue = card.relativeValue(intel.getVira());
        return relativeValue > 4 && relativeValue < 8; // Entre 5 (Rainha) e 7 (Rei)
    }

    // Verifica se o jogador possui uma "Mão Ruim"
    //(nenhuma carta mais forte que o Rei e pelo menos duas cartas maior ou igual a Rainha mas menor que o Rei)
    public boolean hasBadHand(GameIntel intel) {
        int cartasMaisFortesQueRei = countCardsStrongerThanKing(intel);
        int cartasMaioresQueRainhaMenorQueRei = 0;
        for (TrucoCard card : intel.getCards()) {
            if (isBetweenQueenAndKing(card, intel)) {
                cartasMaioresQueRainhaMenorQueRei++;
            }
        }
        return cartasMaisFortesQueRei == 0 && cartasMaioresQueRainhaMenorQueRei >= 2;
    }

    // Verifica se o jogador possui uma "Mão Péssima"
    // (todas as cartas da mão são menores que a Rainha)
    public boolean hasTheWorstHand(GameIntel intel) {
        return countCardsLessThanQueen(intel) == intel.getCards().size();
    }

    // Pega a carta mais forte da mao atual
    public TrucoCard getHighestCard(GameIntel intel) {
        TrucoCard highestCard = null;
        boolean isManilha = false;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                isManilha = true;
                if (highestCard == null || card.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira())) {
                    highestCard = card;
                }
            } else {
                if (!isManilha && (highestCard == null || card.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira()))) {
                    highestCard = card;
                }
            }
        }
        return highestCard;
    }

    // Pega a carta mais fraca da mao atual
    public TrucoCard getLowestCard(GameIntel intel) {
        TrucoCard lowestCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (lowestCard == null || (card != null && !card.isManilha(intel.getVira()) && card.relativeValue(intel.getVira()) < lowestCard.relativeValue(intel.getVira()))) {
                lowestCard = card;
            }
        }
        return lowestCard;
    }

    // Pega a carta media da mao atual
    public TrucoCard getMiddleCard(GameIntel intel) {
        TrucoCard highestCard = getHighestCard(intel);
        TrucoCard lowestCard = getLowestCard(intel);
        TrucoCard middleCard;

        for (TrucoCard card : intel.getCards()) {
            if (card != highestCard && card != lowestCard) {
                middleCard = card;
                return middleCard;
            }
        }
        throw new IllegalStateException("Nenhuma carta do meio encontrada na mão.");
    }

    //Ordena a mao em order decrescente (mais forte para mais fraca)
    public List<TrucoCard> orderHand(GameIntel intel) {
        TrucoCard highestCard = getHighestCard(intel);
        TrucoCard middleCard = getMiddleCard(intel);
        TrucoCard lowestCard = getLowestCard(intel);

        List<TrucoCard> cards = new ArrayList<>();
        cards.add(highestCard);
        cards.add(middleCard);
        cards.add(lowestCard);

        return cards;
    }

    // Verifica se o jogador vai ser o primeiro a jogar
    private boolean isFirstToPlay(GameIntel intel) {
        // Se não houver cartas jogadas ainda, o jogador é o primeiro a jogar
        return intel.getOpenCards().isEmpty();
    }

    //Ver qual a menor carta que ganha do oponente
    private TrucoCard getSmallestCardToBeatOpponent(List<TrucoCard> cards, TrucoCard vira, int opponentCard) {
        TrucoCard smallestGreaterCard = null;
        for (TrucoCard card : cards) {
            if (card.relativeValue(vira) > opponentCard) {
                if (smallestGreaterCard == null || card.relativeValue(vira) < smallestGreaterCard.relativeValue(vira)) {
                    smallestGreaterCard = card;
                }
            }
        }
        return smallestGreaterCard;
    }


    //Ver qual round estamos
    public int getRoundNumber(GameIntel intel) {
        // Obtemos a lista de resultados dos rounds até agora
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        // O número do round atual é igual ao tamanho da lista de resultados mais um
        return roundResults.size() + 1;
    }

    //ver se ganhamos primeiro round
    public boolean wonFirstRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            return false; // Não há resultados de round disponíveis
        }

        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
        return firstRoundResult == GameIntel.RoundResult.WON;
    }

    //ver se perdemos primeiro round
    public boolean lostFirstRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            return false; // Não há resultados de round disponíveis
        }

        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
        return firstRoundResult == GameIntel.RoundResult.LOST;
    }

    //ver se empatamos primeiro round
    public boolean drewFirstRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            return false; // Não há resultados de round disponíveis
        }

        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
        return firstRoundResult == GameIntel.RoundResult.DREW;
    }

    //ver se ganhamos segundo round
    public boolean wonSecondRound(GameIntel intel) {
        if (intel.getRoundResults().size() < 2) {
            return false; // Não há resultados suficientes para o segundo round
        }

        GameIntel.RoundResult secondRoundResult = intel.getRoundResults().get(1);
        return secondRoundResult == GameIntel.RoundResult.WON;
    }

    //ver se perdemos segundo round
    public boolean lostSecondRound(GameIntel intel) {
        if (intel.getRoundResults().size() < 2) {
            return false; // Não há resultados suficientes para o segundo round
        }

        GameIntel.RoundResult secondRoundResult = intel.getRoundResults().get(1);
        return secondRoundResult == GameIntel.RoundResult.LOST;
    }

    //ver se empatamos segundo round
    public boolean drewSecondRound(GameIntel intel) {
        if (intel.getRoundResults().size() < 2) {
            return false; // Não há resultados suficientes para o segundo round
        }

        GameIntel.RoundResult secondRoundResult = intel.getRoundResults().get(1);
        return secondRoundResult == GameIntel.RoundResult.DREW;
    }

    //Pegar card do oponente se ele jogou primeiro
    public Optional<TrucoCard> getOpponentCardIfTheyPlayedFirst(GameIntel intel) {
        if (intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(intel.getOpponentCard().get());
    }

    //Calcular valor da mao baseado no vira e soma
    public Integer getCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }

    public boolean hasOneManilhaAndReallyGoodCard(GameIntel intel){
        int manilhaValue = 0;
        int cardValue = 0;
        for (TrucoCard card : intel.getCards()){
            if (card.isManilha(intel.getVira())){
                manilhaValue = card.relativeValue(intel.getVira());
            } else {
                if (cardValue < card.relativeValue(intel.getVira())) {
                    cardValue = card.relativeValue(intel.getVira());
                }
            }
        }
        return manilhaValue >= 10 && cardValue >= 7;
    }

    //Switch que faz com que o raise response dependa dos state patterns
    private RaiseResponseStatePattern getInstanceByHandPoints(int handPoins) {
        return switch (handPoins) {
            case 1 -> new TrucoState();
            case 3 -> new SeisState();
            case 6 -> new NoveState();
            case 9 -> new DozeState();
            default -> throw new IllegalStateException("Unexpected value: " + handPoins);
        };
    }
}

