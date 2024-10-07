/*
 *  Copyright (C) 2024 Breno Augusto de Oliveira - IFSP/SCL
 *  Copyright (C) 2024 Fernando Candido Rodrigues - IFSP/SCL
 *  Contact: Breno <dot> Oliveira <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: Fernando <dot> Rodrigues <at> aluno <dot> ifsp <dot> edu <dot> br
 *
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

package com.fernando.breno.trucomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;

public class TrucoMarreco implements BotServiceProvider {

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


        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        boolean temManilhas = temManilhas(intel);
     /*
        // Se tiver um casal maior
        if (CasalMaior(intel)) return 1;

        // Se já houve uma rodada e o bot ganhou ou empatou com uma carta Zap
        if (!intel.getRoundResults().isEmpty()) {
            if (temZap(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) return 1;
            if (temZap(intel) && intel.getRoundResults().get(0) == DREW) return 1;
        }

        // Se tem manilhas e carta forte tem
        if (temManilhas && cartaMaisForteSemManilha(intel).relativeValue(intel.getVira()) >= 11) return 1;

        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0) == DREW) return 1;



        if(!t
        // Se nenhuma condição for satisfeita
        return -1;

      */


        int qtdManilha = contarManilhas(intel.getCards(), intel.getVira());

        // Se tem um Casal Maior, aumentar
        if (CasalMaior(intel)) return 1;

        // Se o bot ganhou a última rodada e tem Zap, aumentar
        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0)
                == GameIntel.RoundResult.WON) {
            return 1;
        }

        // Se a última rodada empatou e tem Zap, aumentar
        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0)
                == GameIntel.RoundResult.DREW) {
            return 1;
        }

        // Se tem manilhas e a carta mais forte for maior ou igual a 11, aumentar
        if (qtdManilha > 0 && cartaMaisForteSemManilha(intel).relativeValue(intel.getVira()) >= 11) {
            return 1;
        }

        // Se o bot ganhou a última rodada e tem manilha rank 12
        if (!intel.getRoundResults().isEmpty() && encontrarRanksDasManilhas(intel).contains(12) &&
                intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {

            return 1;
        }

        // Se o bot ganhou a última rodada e tem manilha rank 11
        if (!intel.getRoundResults().isEmpty() && encontrarRanksDasManilhas(intel).contains(11) &&
                intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {

            return 1;
        }


        // Se tem 1 manilhas não, aceitar
        if (qtdManilha  <= 1  ) {
            return 0;
        }



        //  correr
        return -1;
    }


    private boolean CasalMaior(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        boolean encontrouZap = false;
        boolean encontrouCopas = false;

        // Itera sobre as cartas para verificar se tem Zap e Copas
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(cardVira)) {
                encontrouZap = true;
            } else if (card.isCopas(cardVira)) {
                encontrouCopas = true;
            }

            // Se encontrar ambos, já pode retornar true
            if (encontrouZap && encontrouCopas) {
                return true;
            }
        }


        return false;
    }

    boolean temManilhas(GameIntel intel) {

        for (TrucoCard carta : intel.getCards()) {
            // Verifica se a carta atual é uma manilha em relação à carta vira
            if (carta.isManilha(intel.getVira())) {
                return true;
            }
        }
        return false;
    }

    private int avaliarForcaDaMao(GameIntel intel) {
        int forca = 0;
        TrucoCard vira = intel.getVira();

        for (TrucoCard carta : intel.getCards()) {
            // Adiciona a força da carta com base no valor relativo em relação à vira
            forca += carta.relativeValue(vira);
        }



        if (temManilhas(intel)) {
            forca += 10;
        }

        return forca;
    }

    private boolean deveAceitarTruco(GameIntel intel) {
        int forcaMao = avaliarForcaDaMao(intel);


        return forcaMao >= 21 || temManilhas(intel);
    }
    private Boolean temZap(GameIntel intel){
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) // verifica zap
                return true;
        }
        return false;
    }


    private TrucoCard cartaMaisForteSemManilha(GameIntel intel) {
        return encontrarCarta(intel, (carta1, carta2) -> {
            int valorRelativo1 = carta1.relativeValue(intel.getVira());
            int valorRelativo2 = carta2.relativeValue(intel.getVira());
            return Integer.compare(valorRelativo2, valorRelativo1); // Inverte a comparação para encontrar a carta mais forte
        });
    }

    private TrucoCard cartaMaisFracaSemManilha(GameIntel intel) {
        return encontrarCarta(intel, (carta1, carta2) -> {
            int valorRelativo1 = carta1.relativeValue(intel.getVira());
            int valorRelativo2 = carta2.relativeValue(intel.getVira());
            return Integer.compare(valorRelativo2, valorRelativo1); // Inverte a comparação
        });
    }


    private TrucoCard encontrarCarta(GameIntel intel, Comparator<TrucoCard> comparador) {
        List<TrucoCard> cartas = intel.getCards();
        TrucoCard melhorCarta = null;

        TrucoCard vira = intel.getVira();

        for (TrucoCard carta : cartas) {

            if (!carta.isManilha(vira)) {
                // Se não houver melhor carta ou a carta atual satisfaz o comparador, atualiza
                if (melhorCarta == null || comparador.compare(carta, melhorCarta) > 0) {
                    melhorCarta = carta;
                }
            }
        }

        return melhorCarta;
    }


    private List<Integer> encontrarRanksDasManilhas(GameIntel intel) {
        List<TrucoCard> cartas = intel.getCards();
        List<Integer> ranksDasManilhas = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        for (TrucoCard carta : cartas) {
            // Verifica se a carta é uma manilha
            if (carta.isManilha(vira)) {
                // Adiciona o rank relativo da manilha à lista
                int rankRelativo = carta.relativeValue(vira);
                ranksDasManilhas.add(rankRelativo);
            }
        }

        return ranksDasManilhas; // Retorna a lista de ranks das manilhas
    }


    private int contarManilhas(List<TrucoCard> cartas, TrucoCard vira) {
        int qtdManilha = 0;
        for (TrucoCard carta : cartas) {
            if (carta.isManilha(vira)) qtdManilha++; // Incrementa se for manilha
        }
        return qtdManilha;
    }

    private TrucoCard cartaFraca(GameIntel intel) {
        List<TrucoCard> cartas = intel.getCards();
        TrucoCard cartaFraca = null;
        int minValue = Integer.MAX_VALUE; // Define o valor mínimo como o maior inteiro possível

        for (TrucoCard carta : cartas) {
            int valorRelativo = carta.isManilha(intel.getVira()) ?
                    carta.relativeValue(intel.getVira()) :
                    carta.getRank().value();

            // Atualiza a carta mais fraca se o valor atual for menor
            if (valorRelativo < minValue) {
                minValue = valorRelativo;
                cartaFraca = carta;
            }
        }

        return cartaFraca;
    }




}
