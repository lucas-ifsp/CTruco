/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

/* 'zeTruquero' bot with didactic propose. Code by Lucas Selin and Pedro Bonelli */

package com.Selin.Bonelli.zetruquero;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;
import java.util.List;

public class Zetruquero implements BotServiceProvider
{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel)
    {
        //decidir jogar para 2 manilhas
        //decidir jogar com zap
        //decidir jogar com 1 manilha e duas cartas fortes
        //Deve recusar mão de onze se tiver duas cartas fracas
        //Deve recusar mão de onze com uma carta baixa e uma manilha fraca
        //Deve aceitar mão de onze com duas manilhas fortes e uma carta alta
        //Deve recusar mão de onze com duas cartas medianas e sem manilha
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel)
    {
        //deve aumentar a pedida de pontos caso tenha ganho um round e seja o ultimo, com carta forte
        //deve nao aumentar a pedida de pontos caso nao tenha cartas fortes ou manilhas
        //Deve aumentar a pedida se tiver uma manilha e uma carta alta
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel)
    {
        //deve escolher a carta menor primeiro, caso tiver zap em maos
        //se nao tiver manilha deve abrir o jogo com a carta mais forte
        //se tiver feito um ponto, jogar a carta mais forte
        //se tiver casal maior, jogar sempre a mais fraca
        //Deve escolher a carta intermediária se for a segunda rodada e tiver ganho a primeira
        //Deve jogar a carta mais forte se não houver manilha e tiver duas cartas fortes
        //Deve jogar a manilha mais fraca se tiver as duas manilhas mais fortes
        //Deve escolher a carta mais baixa com cartas medianas e sem manilha
        //Deve jogar a carta intermediária com uma manilha fraca e uma carta alta
        //Deve jogar o 3 no primeiro round
        //Deve jogar carta intermediária quando o adversário joga manilha
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel)
    {
        //nao deve aceitar o aumento de pontos para caso nao tenha uma manilha ou 2 cartas bem fortes
        //nao aceitar a pedida se ja tenha perdido o primeiro round
        //aumentar a pedida caso tenha uma vitoria e o zap
        //aumentar a pedida caso seja round 2 e tenha uma vitoria e duas manilhas
        // Deve recusar aumentar a pedida com cartas medianas e já perdeu o primeiro round
        //Deve aceitar aumentar a pedida se tiver zap e manilha
        //Deve aumentar a pedida com um Zap e uma carta forte
        //Deve aceitar truco no último round com um 3
        //Deve aceitar aumento com manilhas fracas e uma carta alta
        return 0;
    }

    public Boolean strongCardInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se de fato na mao do bot tem alguma carta considerada forte (sem considerar manilha)
        // A, 2, 3
        //Deve pedir truco no último round se tiver A, 2 ou 3 na mão
        //Deve aceitar truco no último round se tiver A, 2 ou 3 na mão
        return false;
    }

    public Boolean royaltyCardInHand(List<TrucoCard> cards)
    {
        //funcao ira analisar se na mao do bot tem alguma carta rei, rainha ou valete
        return false;
    }

    public Boolean zapInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao tem o ZAPZAO
        return false;
    }

    public Boolean manilhaInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao do bot tem alguma manilha
        return false;
    }

    public Boolean twoStrongestManilhas(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao do bot temos o casal maior
        return false;
    }

    public Boolean twoweakerManilhas(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao do bot temos o casal menor
        return false;
    }

    public Boolean weakHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao do bot ta horrivel - FUGIR DE TUDO
        return false;
    }

    public Boolean strongHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se na mao do bot ta perfeita - CHAMAR TUDO
        // pelo menos 2 manilha ou 1 zap ou duas cartas acima de K
        return false;
    }


    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}

