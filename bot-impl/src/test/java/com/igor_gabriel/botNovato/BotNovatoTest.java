package com.igor_gabriel.botNovato;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotNovatoTest {

    @Test
    void testGetMaoDeOnzeResponse_returnsTrueWhenStrengthAbove21() {
        // Vira = 4 de Copas (FOUR, HEARTS) → manilha = FIVE
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        // Mão: Rei de Paus (KING, CLUBS) + Dois de Espadas (TWO, SPADES)
        TrucoCard c1 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
        TrucoCard c2 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(mao, 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        assertTrue(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    void testGetMaoDeOnzeResponse_returnsFalseWhenStrengthBelowThreshold() {
        // Vira = Ás de Copas (ACE, HEARTS) → manilha = TWO
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);

        // Mão: Quatro de Paus (FOUR, CLUBS) + Cinco de Espadas (FIVE, SPADES)
        TrucoCard c1 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard c2 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(mao, 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        assertFalse(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    void testDecideIfRaises_returnsTrueIfHasManilha() {
        // Vira = Rei de Copas (KING, HEARTS) → manilha = ACE
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        // Cria carta de manilha (ACE, SPADES)
        TrucoCard manilhaCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        List<TrucoCard> mao = List.of(manilhaCard);

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(mao, 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    void testDecideIfRaises_returnsFalseIfNoManilha() {
        // Vira = Dez de Copas (TEN, HEARTS) → manilha = QUEEN
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        TrucoCard c1 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard c2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(mao, 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    void testChooseCard_returnsFirstCard() {
        TrucoCard c1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        TrucoCard c2 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        List<TrucoCard> mao = Arrays.asList(c1, c2);
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(mao, 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        CardToPlay cardToPlay = bot.chooseCard(intel);


    }

    @Test
    void testGetRaiseResponse_returnsMinusOne() {
        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(), null, 1)
                .botInfo(List.of(), 0)
                .opponentScore(0)
                .build();

        BotNovato bot = new BotNovato();
        assertEquals(-1, bot.getRaiseResponse(intel));
    }

    @Test
    void testGetName_returnsClassName() {
        BotNovato bot = new BotNovato();
        assertEquals("BotNovato", bot.getName());
    }

}
