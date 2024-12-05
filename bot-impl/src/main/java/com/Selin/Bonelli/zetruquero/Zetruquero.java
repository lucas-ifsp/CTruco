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
import java.util.Optional;

public class Zetruquero implements BotServiceProvider
{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel)
    {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        if (strongCardInHand(cards, vira))
        {
            return true;
        }

        if (weakHand(cards, vira))
        {
            return false;
        }

        if (strongHand(cards, vira))
        {
            return true;
        }

        if(royaltyCardInHand(cards, vira))
        {
            return true;
        }

        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel)
    {
        int currentRound = intel.getRoundResults().size() + 1;
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        List<GameIntel.RoundResult> RoundsList = intel.getRoundResults();

        if(strongHand(cards, vira) && manilhaInHand(cards, vira))
        {
            return true;
        }

        if (currentRound == 1 && (strongHand(cards, vira) ||  manilhaInHand(cards, vira)))
        {
            return true;
        }

        if (currentRound == 2 && RoundsList.contains(GameIntel.RoundResult.LOST))
        {
            return zapInHand(cards, vira);
        }

        if (currentRound == 3 && RoundsList.contains(GameIntel.RoundResult.WON))
        {
            return strongHand(cards, vira);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel)
    {
        int currentRound = intel.getRoundResults().size() + 1;
        Boolean isFirst = intel.getOpponentCard().isEmpty();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        List<GameIntel.RoundResult> RoundsList = intel.getRoundResults();

        CardToPlay cardToDiscart = CardToPlay.of(weakerCardtoUse(cards, vira));
        CardToPlay strongCard = CardToPlay.of(strongestCardtoUse(cards, vira));

        if(isFirst && zapInHand(cards, vira))
        {
            return cardToDiscart;
        }

        if(isFirst && weakHand(cards, vira))
        {
            return strongCard;
        }

        if(manilhaInHand(cards, vira) && isFirst)
        {
            return cardToDiscart;
        }

        if(isFirst)
        {
            return strongCard;
        }

        if (currentRound == 1)
        {
            return strongCard;
        }

        if (currentRound == 3 && !RoundsList.isEmpty() && RoundsList.get(0).equals(GameIntel.RoundResult.WON))
        {
            return strongCard;
        }

        if(twoStrongestManilhas(cards, vira))
        {
            return cardToDiscart;
        }

        if(isStrongerThanAll(opponentCard.get(), cards, vira))
        {
            return cardToDiscart;
        }

        if (currentRound == 2 && !RoundsList.isEmpty() && RoundsList.get(0).equals(GameIntel.RoundResult.WON))
        {
            return cardToDiscart;
        }

        if (manilhaInHand(cards, vira) && currentRound == 2 && !RoundsList.isEmpty() && RoundsList.get(0).equals(GameIntel.RoundResult.WON))
        {
            return cardToDiscart;
        }

        if(twoStrongestManilhas(cards,vira))
        {
            return CardToPlay.of(getWeakCardThatWin(opponentCard.get(), cards, vira));
        }

        return cardToDiscart;
    }

    @Override
    public int getRaiseResponse(GameIntel intel)
    {
        int currentRound = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> currentRoundList = intel.getRoundResults();
        Boolean isFirst = intel.getOpponentCard().isEmpty();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (!isFirst)
        {
            if(isStrongerThanAll(opponentCard.get(), cards, vira))
            {
                return -1;
            }
        }

        if(strongHand(cards, vira) && manilhaInHand(cards, vira))
        {
            return 0;
        }

        if(zapInHand(cards, vira) && strongHand(cards, vira))
        {
            return 0;
        }

        if(currentRound == 2 && currentRoundList.contains(GameIntel.RoundResult.LOST) && (!manilhaInHand(cards, vira) || weakHand(cards, vira)))
        {
            return -1;
        }

        if(currentRound == 2 && currentRoundList.contains(GameIntel.RoundResult.WON) && (zapInHand(cards, vira) || manilhaInHand(cards, vira)))
        {
            return 1;
        }

        if(twoweakerManilhas(cards, vira) || strongHand(cards, vira))
        {
            return 0;
        }

        return 0;
    }

    public Boolean strongCardInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        boolean hasManilha =  manilhaInHand(cards, vira);

        boolean twoStrongestManilhas =  twoStrongestManilhas(cards, vira);

        boolean hasZap =  zapInHand(cards, vira);

        boolean superStrongHand =  manilhaInHand(cards, vira) & strongHand(cards, vira);

        if(hasZap)
        {
            return true;
        }

        if (hasManilha || twoStrongestManilhas)
        {
            return true;
        }

        if(superStrongHand)
        {
            return true;
        }

        return false;
    }

    public TrucoCard weakerCardtoUse(List<TrucoCard> cards, TrucoCard vira)
    {
        TrucoCard wekeastCard = cards.get(0);
        for (TrucoCard card : cards)
        {
            if (card.compareValueTo(wekeastCard, vira) < 0)
            {
                wekeastCard = card;
            }
        }

        return wekeastCard;
    }

    public TrucoCard strongestCardtoUse(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard strongestCard = cards.get(0);

        for (TrucoCard card : cards)
        {
            if (card.compareValueTo(strongestCard, vira) > 0)
            {
                strongestCard = card;
            }
        }

        return strongestCard;
    }

    public TrucoCard getWeakCardThatWin(TrucoCard opponentCard, List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard weakCardThatWin = null;

        for (TrucoCard card : cards)
        {
            if (card.compareValueTo(opponentCard, vira) > 0)
            {
                if (weakCardThatWin == null || card.compareValueTo(weakCardThatWin, vira) < 0) {
                    weakCardThatWin = card;
                }
            }
        }

        return weakCardThatWin;
    }

    public boolean isStrongerThanAll(TrucoCard opponentCard, List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards)
        {
            if (card.compareValueTo(opponentCard, vira) >= 0)
            {
                return false;
            }
        }

        return true;
    }

    public Boolean royaltyCardInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        return cards.stream().anyMatch(card -> card.getRank() == CardRank.KING || card.getRank() == CardRank.QUEEN);
    }

    public Boolean zapInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        return cards.stream().anyMatch(card -> card.isZap(vira));
    }

    public Boolean manilhaInHand(List<TrucoCard> cards, TrucoCard vira)
    {
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }

    public Boolean twoStrongestManilhas(List<TrucoCard> cards, TrucoCard vira)
    {
        boolean hasCopasManilha = cards.stream().anyMatch(card -> card.isManilha(vira) && card.isCopas(vira));

        boolean hasZap = cards.stream().anyMatch(card -> card.isZap(vira));

        return hasCopasManilha && hasZap;
    }

    public Boolean twoweakerManilhas(List<TrucoCard> cards, TrucoCard vira)
    {
        boolean hasEspadilhaManilha = cards.stream().anyMatch(card -> card.isManilha(vira) && card.isEspadilha(vira));

        boolean hasOurosManilha = cards.stream().anyMatch(card -> card.isManilha(vira) && card.isOuros(vira));

        return hasEspadilhaManilha && hasOurosManilha;
    }

    public Boolean weakHand(List<TrucoCard> cards, TrucoCard vira)
    {
        boolean hasZap = cards.stream().anyMatch(card -> card.isZap(vira));

        return !strongHand(cards, vira) && !hasZap && !royaltyCardInHand(cards, vira);
    }

    public Boolean strongHand(List<TrucoCard> cards, TrucoCard vira)
    {
        boolean hasZap = cards.stream().anyMatch(card -> card.isZap(vira));

        boolean hasRoyalty = royaltyCardInHand(cards, vira);

        boolean hasStrongThanKing = cards.stream().anyMatch(card ->
                card.getRank() == CardRank.KING
                || card.getRank() == CardRank.ACE
                || card.getRank() == CardRank.THREE
                || card.getRank() == CardRank.TWO);

        if (hasZap || (hasStrongThanKing && hasRoyalty) || (hasRoyalty && manilhaInHand(cards, vira)))
        {
            return true;
        }

        return false;
    }

    @Override
    public String getName()
    {
        return BotServiceProvider.super.getName();
    }
}

