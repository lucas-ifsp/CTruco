package com.yuri.impl;

import com.bueno.spi.model.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotMadeInDescalvadoTest {
    // int getRaiseResponse(GameIntel intel);
    // Answers a point raise request in a truco hand
    // -1 quit
    //  0 accept
    //  1 re-raise/call
    // > Tem que retornar no escopo, não pode retornar >= 2 ou <= -2

    // boolean getMaoDeOnzeResponse(GameIntel intel)
    // Choose if bot plays a mão de onze
    // false -> quit
    // true  -> play

    // boolean decideIfRaises(GameIntel intel)
    // Choose if bot starts a point raise request
    // false -> nothing
    // true  -> raise

    // CardToPlay chooseCard(GameIntel intel)
    // Provided the card will be played or discarded in the current round
    // > Tem que retornar uma carta que tem na mão

    // First Round
    // Joga Primeiro
        // 2 manilha (zap copa) ? -> discarta
        // A mais forte

    @Test
    @DisplayName("\"chooseCard\" should discard card if it is the first round and plays first and has two manilhas")
    void chooseCardShouldDiscardCardIfFirstRoundAndPlaysFirstAndHasTwoManilhas() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

        TrucoCard expectedPlayedCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

        List<TrucoCard> cards = List.of(
            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), // Manilha
            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), // Manilha
            expectedPlayedCard
        );

        GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(
                new ArrayList<>(), // First round
                new ArrayList<>(), // First to play
                vira,
                1
            )
            .botInfo(cards, 0)
            .opponentScore(0)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertTrue(playedCard.isDiscard());
        assertEquals(playedCard.content(), expectedPlayedCard);
    }

    // Joga Segundo
        // Mais fraca que ganha
            // Mais fraca que empata
                // Mais fraca pra economizar

    // Second Round
    // Ganho, Joga Primeiro
        // Mais fraca para fazer gastar
    // Ganho, Joga Segundo
        // Se der pra ganhar ganha
            // Se não mais fraca para fazer gastar
    // Perdeu, Joga Primeiro
        // A mais forte
    // Perdeu, Joga Segundo
        // O minimo pra ganhar
            // Senão F

    // Third Round
        // Joga

    // Discartar a primeira -> 2 manilha ?
    // else -> mais forte
    // else -> se o cara já jogou a mais fraca q mata
    // else -> se nao empata
    // else -> discarta mais fraca








    // boolean getMaoDeOnzeResponse(GameIntel intel)
    // 1  - Se eu tiver 3 manilha = true
    // 2  - Se eu tiver 2 manilha = true
    // 3  - Se eu tiver 1 manilha e dois 3 = true
    // 4  - Se eu tiver 1 manilha e um 3 = true
    // 5  - Se eu tiver Tres 3 = true
    // 6  - Se eu tiver Dois 3 e um Dois = true
    // 7  - Se eu NÃO tiver nenhuma manilha = false
    // 8  - Se eu tiver uma manilha e nenhum 3 = false
    // 9  - Se eu tiver apenas um 3 = false
    // 10 - Se eu não tiver com manilha ou 3 = false
}
