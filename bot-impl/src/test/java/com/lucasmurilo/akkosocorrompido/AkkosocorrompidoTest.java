package com.lucasmurilo.akkosocorrompido;

import org.junit.jupiter.api.Test;

public class AkkosocorrompidoTest {

    @Test
    public void testGetMaoDeOnzeResponse_ShouldCallUnsupportedOperationException() {
        BotServiceProvider bot = new Akkosocorrompido();
        assertThrows(UnsupportedOperationException.class, () -> bot.getMaoDeOnzeResponse(null));
    }

    @Test
    public void testDecideIfRaises_ShouldCallUnsupportedOperationException() {
        BotServiceProvider bot = new Akkosocorrompido();
        assertThrows(UnsupportedOperationException.class, () -> bot.decideIfRaises(null));
    }

    @Test
    public void testChooseCard_ShouldCallUnsupportedOperationException() {
        BotServiceProvider bot = new Akkosocorrompido();
        assertThrows(UnsupportedOperationException.class, () -> bot.chooseCard(null));
    }

    @Test
    public void testGetRaiseResponse_ShouldCallUnsupportedOperationException() {
        BotServiceProvider bot = new Akkosocorrompido();
        assertThrows(UnsupportedOperationException.class, () -> bot.getRaiseResponse(null));
    }
}