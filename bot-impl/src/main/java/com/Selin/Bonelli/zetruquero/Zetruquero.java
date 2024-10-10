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
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import java.util.List;

public class Zetruquero implements BotServiceProvider
{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel)
    {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel)
    {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel)
    {
        //deve escolher a carta menor primeiro, caso tiver zap em maos
        //se nao tiver manilha deve abrir o jogo com a carta mais forte
        //se tiver feito um ponto, jogar a carta mais forte

        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel)
    {
        return 0;
    }

    public Boolean strongCardInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        //funcao ira analisar se de fato na mao do bot tem alguma carta considerada forte (sem considerar manilha)
        // A, 2, 3
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
}

