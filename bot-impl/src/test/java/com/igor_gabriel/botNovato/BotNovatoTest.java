package com.igor_gabriel.botNovato;

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
        TrucoCard vira = new TrucoCard(Naipe.COPAS, Valor.QUATRO);

        TrucoCard c1 = new TrucoCard(Naipe.PAUS, Valor.REI);   // valor alto
        TrucoCard c2 = new TrucoCard(Naipe.ESPADAS, Valor.DOIS); // valor médio
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = new GameIntel(mao, vira);

        BotNovato bot = new BotNovato();
        boolean response = bot.getMaoDeOnzeResponse(intel);
        assertTrue(response);
    }

    @Test
    void testGetMaoDeOnzeResponse_returnsFalseWhenStrengthBelowThreshold() {
        TrucoCard vira = new TrucoCard(Naipe.COPAS, Valor.AS);

        TrucoCard c1 = new TrucoCard(Naipe.PAUS, Valor.DOIS);
        TrucoCard c2 = new TrucoCard(Naipe.ESPADAS, Valor.TRES);
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = new GameIntel(mao, vira);

        BotNovato bot = new BotNovato();
        boolean response = bot.getMaoDeOnzeResponse(intel);
        assertFalse(response);
    }

    @Test
    void testDecideIfRaises_returnsTrueIfHasManilha() {
        Valor viraValor = Valor.REI;
        TrucoCard vira = new TrucoCard(Naipe.COPAS, viraValor); // manilhas: A, 2, 3, 4 após rei
        Valor manilhaValor = Valor.AS; // supondo AS é manilha após REI

        TrucoCard manilhaCard = new TrucoCard(Naipe.ESPADAS, manilhaValor);
        List<TrucoCard> mao = List.of(manilhaCard);

        GameIntel intel = new GameIntel(mao, vira);

        BotNovato bot = new BotNovato();
        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    void testDecideIfRaises_returnsFalseIfNoManilha() {
        TrucoCard vira = new TrucoCard(Naipe.COPAS, Valor.DEZ);
        TrucoCard c1 = new TrucoCard(Naipe.PAUS, Valor.DOIS);
        TrucoCard c2 = new TrucoCard(Naipe.ESPADAS, Valor.TRES);
        List<TrucoCard> mao = Arrays.asList(c1, c2);

        GameIntel intel = new GameIntel(mao, vira);

        BotNovato bot = new BotNovato();
        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    void testChooseCard_returnsFirstCard() {
        TrucoCard c1 = new TrucoCard(Naipe.COPAS, Valor.CINCO);
        TrucoCard c2 = new TrucoCard(Naipe.PAUS, Valor.NOVE);

        List<TrucoCard> mao = Arrays.asList(c1, c2);
        TrucoCard vira = new TrucoCard(Naipe.ESPADAS, Valor.SEIS);

        GameIntel intel = new GameIntel(mao, vira);

        BotNovato bot = new BotNovato();
        CardToPlay cardToPlay = bot.chooseCard(intel);

        assertEquals(CardToPlay.of(c1), cardToPlay);
    }

    @Test
    void testGetRaiseResponse_returnsMinusOne() {
        GameIntel intel = new GameIntel(List.of(), null);
        BotNovato bot = new BotNovato();
        assertEquals(-1, bot.getRaiseResponse(intel));
    }

    @Test
    void testGetName_returnsClassName() {
        BotNovato bot = new BotNovato();
        assertEquals("BotNovato", bot.getName());
    }
}