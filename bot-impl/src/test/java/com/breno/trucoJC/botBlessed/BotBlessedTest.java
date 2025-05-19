/*
 *  Copyright (C) 2025 Breno Augusto de Oliveira - IFSP/SCL
 *  Contact: oliveira <dot> breno <at> ifsp <dot> edu <dot> br
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

package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BotBlessedTest {

    private BotBlessed botBlessed;

    @BeforeEach
    public void setUp() {
        botBlessed = new BotBlessed();
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("maoDeOnzeTestCases")
    @DisplayName("Testes para getMaoDeOnzeResponse")
    void testGetMaoDeOnzeResponse(String description, int playerScore, int opponentScore, List<TrucoCard> hand, TrucoCard vira, boolean expected) {
        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(vira), vira, 3)
                .botInfo(hand, playerScore)
                .opponentScore(opponentScore)
                .build();

        boolean result = botBlessed.getMaoDeOnzeResponse(intel);
        assertThat(result).as(description).isEqualTo(expected);
    }

    private static Stream<Object[]> maoDeOnzeTestCases() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            return Stream.of(
                    new Object[]{"✅ 11x10 com dois 3 → Aceita", 11, 10, List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE)), vira, true},
                    new Object[]{"✅ 5x11 com uma manilha → Recusa", 5, 11, List.of(manilha(vira)), vira, false},
                    new Object[]{"✅ 9x11 com Zap e 3 → Aceita", 9, 11, List.of(zap(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, true},
                    new Object[]{"✅ 0x11 com uma manilha → Recusa", 0, 11, List.of(manilha(vira)), vira, false},
                    new Object[]{"✅ 11x11 com manilha e 3 → Aceita", 11, 11, List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE)), vira, true},
                    new Object[]{"❌ 0x5 sem manilha ou 3, mas não é mão de 11 → Recusa", 0, 5, List.of(card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX)), vira, false},
                    new Object[]{"✅ 6x11 com Zap e 3 → Aceita", 6, 11, List.of(zap(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, true},
                    new Object[]{"✅ 11x5 com manilha e dois 3 → Aceita", 11, 5, List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.THREE), card(CardSuit.CLUBS, CardRank.THREE)), vira, true},
                    new Object[]{"✅ 11x9 com duas manilhas → Aceita", 11, 9, List.of(manilha(vira), manilha(vira)), vira, true},
                    new Object[]{"✅ 0x11 com duas manilhas → Aceita", 0, 11, List.of(manilha(vira), manilha(vira)), vira, true},
                    new Object[]{"✅ 11x7 com manilha e 3 → Aceita", 11, 7, List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, true},
                    new Object[]{"✅ 11x6 com manilha e 3 → Aceita", 11, 6, List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE)), vira, true}
            );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("decideIfRaisesTestCases")
    @DisplayName("Testes para decideIfRaises")
    void testDecideIfRaises(String description, List<TrucoCard> hand, TrucoCard vira, boolean expected) {
        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(vira), vira, 3)
                .botInfo(hand, 0)
                .opponentScore(0)
                .build();

        boolean result = botBlessed.decideIfRaises(intel);
        assertThat(result).as(description).isEqualTo(expected);
    }

    private static Stream<Object[]> decideIfRaisesTestCases() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        return Stream.of(
                new Object[]{"DUAS MANILHAS → Deve aceitar o aumento",
                        List.of(manilha(vira), card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.FIVE)),
                        vira, true},

                new Object[]{"DOIS OU MAIS TRÊS → Deve aceitar o aumento",
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, true},

                new Object[]{"UM ZAP → Deve aceitar o aumento",
                        List.of(zap(vira), card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"CARTAS FRACAS → Não deve aceitar o aumento",
                        List.of(card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, false},

                new Object[]{"CARTAS FRACAS → Não deve aceitar o aumento",
                        List.of(card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.SPADES, CardRank.TWO), card(CardSuit.HEARTS, CardRank.SIX)),
                        vira, false},

                new Object[]{"UMA MANILHA → Deve aceitar o aumento",
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"DOIS TRÊS → Deve aceitar o aumento",
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"MANILHA e 3 → Deve aceitar o aumento",
                        List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SIX)),
                        vira, true},

                new Object[]{"ZAP e 3 → Deve aceitar o aumento",
                        List.of(zap(vira), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.CLUBS, CardRank.SIX)),
                        vira, true},

                new Object[]{"CARTAS MEDIANAS → Não deve aceitar o aumento",
                        List.of(card(CardSuit.CLUBS, CardRank.ACE), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.KING)),
                        vira, false}
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("chooseCardTestCases")
    @DisplayName("Testes para chooseCard")
    void testChooseCard(String description, List<TrucoCard> hand, Optional<TrucoCard> opponentCard,
                        List<GameIntel.RoundResult> roundResults, TrucoCard vira, TrucoCard expectedCard) {

        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, List.of(vira), vira, 3)
                .botInfo(hand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard.orElse(null))
                .build();

        CardToPlay cardToPlay = botBlessed.chooseCard(intel);
        TrucoCard chosenCard = cardToPlay.content();
        assertThat(chosenCard).as(description).isEqualTo(expectedCard);
    }

    private static Stream<Object[]> chooseCardTestCases() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        TrucoCard manilha2 = TrucoCard.of(vira.getRank().next(), CardSuit.HEARTS); // Manilha com outro naipe

        return Stream.of(
                new Object[]{"1ª rodada: oponente jogou intermediária, você tem Zap e 3 → joga Zap",
                        List.of(zap(vira), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FIVE)),
                        Optional.of(card(CardSuit.CLUBS, CardRank.SIX)),
                        List.of(),
                        vira,
                        zap(vira)},

                new Object[]{"1ª rodada: oponente jogou carta forte, você tem uma manilha e um 3 → joga manilha",
                        List.of(card(CardSuit.SPADES, CardRank.THREE), manilha(vira), card(CardSuit.HEARTS, CardRank.SIX)),
                        Optional.of(card(CardSuit.SPADES, CardRank.SEVEN)),
                        List.of(),
                        vira,
                        manilha(vira)},

                new Object[]{"2ª rodada: ganhou a 1ª, cartas fortes → joga a 2ª mais forte",
                        List.of(zap(vira), manilha(vira), card(CardSuit.SPADES, CardRank.THREE)),
                        Optional.empty(),
                        List.of(GameIntel.RoundResult.WON),
                        vira,
                        zap(vira)},

                new Object[]{"1ª rodada: oponente jogou um 3 e você tem manilha → joga a mais forte",
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.THREE)),
                        Optional.of(card(CardSuit.SPADES, CardRank.THREE)),
                        List.of(),
                        vira,
                        manilha(vira)},

                new Object[]{"3ª rodada: ganhou as duas anteriores → joga a última carta",
                        List.of(zap(vira)),
                        Optional.empty(),
                        List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.WON),
                        vira,
                        zap(vira)},

                new Object[]{"1ª rodada: você joga forte e oponente responde fraca → joga forte de novo",
                        List.of(zap(vira), card(CardSuit.HEARTS, CardRank.FIVE)),
                        Optional.of(card(CardSuit.SPADES, CardRank.FIVE)),
                        List.of(),
                        vira,
                        zap(vira)},

                new Object[]{"3ª rodada: perdeu 1ª e 2ª, mas carta restante é mais forte → joga a mais forte",
                        List.of(zap(vira)),
                        Optional.of(card(CardSuit.CLUBS, CardRank.FIVE)),
                        List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.LOST),
                        vira,
                        zap(vira)}
        );
    }


    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getRaiseResponseTestCases")
    @DisplayName("Testes para getRaiseResponse")
    void testGetRaiseResponse(String description, List<TrucoCard> hand, int playerScore, int opponentScore, TrucoCard vira, int expected) {
        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(vira), vira, 3)
                .botInfo(hand, playerScore)
                .opponentScore(opponentScore)
                .build();

        int result = botBlessed.getRaiseResponse(intel);
        assertThat(result).as(description).isEqualTo(expected);
    }

    private static Stream<Object[]> getRaiseResponseTestCases() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        return Stream.of(
                new Object[]{"Oponente pede aumento, tem manilha e 3's → aumenta (1)",
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.FIVE), card(CardSuit.HEARTS, CardRank.THREE), card(CardSuit.CLUBS, CardRank.THREE)), 5, 6, vira, 1},

                new Object[]{"Oponente pede aumento, só 1 carta forte (manilha) → recusa (-1)",
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.SIX), card(CardSuit.CLUBS, CardRank.FIVE)), 4, 7, vira, -1},

                new Object[]{"Oponente pede aumento, só cartas medianas e está em desvantagem → recusa (-1)",
                        List.of(card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.SIX), card(CardSuit.SPADES, CardRank.SEVEN)), 3, 6, vira, -1},

                new Object[]{"Oponente pede aumento, 2 ou mais 3's → aceita (0)",
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FOUR)), 2, 5, vira, 0},

                new Object[]{"Oponente pede aumento, só uma carta fraca e está em desvantagem → recusa (-1)",
                        List.of(card(CardSuit.CLUBS, CardRank.TWO), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FOUR)), 1, 7, vira, -1},

                new Object[]{"Oponente pede aumento, tem Zap e 3 → aumenta (1)",
                        List.of(zap(vira), card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SIX)), 3, 4, vira, 1},

                new Object[]{"Oponente pede aumento, só cartas medianas e placar empatado (10x10) → recusa (-1)",
                        List.of(card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.SIX), card(CardSuit.SPADES, CardRank.JACK)), 10, 10, vira, -1},

                new Object[]{"Oponente pede aumento, casal (Zap + Copas) → aumenta (1)",
                        List.of(zap(vira), card(CardSuit.HEARTS, CardRank.QUEEN), card(CardSuit.HEARTS, CardRank.FIVE)), 6, 6, vira, 1},

                new Object[]{"Oponente pede aumento, só uma carta boa e placar favorável → recusa (-1)",
                        List.of(manilha(vira), card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.HEARTS, CardRank.SIX)), 8, 5, vira, -1},

                new Object[]{"Oponente pede aumento, 0 pontos e sem cartas fortes → recusa (-1)",
                        List.of(card(CardSuit.CLUBS, CardRank.SIX), card(CardSuit.HEARTS, CardRank.FOUR), card(CardSuit.SPADES, CardRank.SEVEN)), 0, 4, vira, -1},

                new Object[]{"Oponente pede aumento, 10 pontos, adversário 9, com 1 três e manilha → aumenta (1)",
                        List.of(manilha(vira), card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SEVEN)), 10, 9, vira, 1},

                new Object[]{"Oponente pede aumento, 8 pontos, adversário 10, sem manilhas nem 3's → recusa (-1)",
                        List.of(card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.HEARTS, CardRank.SIX), card(CardSuit.SPADES, CardRank.KING)), 8, 10, vira, -1},

                new Object[]{"Oponente pede aumento, tem manilha, placar muito desfavorável → recusa (-1)",
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.FOUR)), 2, 9, vira, -1},

                new Object[]{"Oponente pede aumento, só cartas fracas, placar muito favorável → recusa (-1)",
                        List.of(card(CardSuit.CLUBS, CardRank.TWO), card(CardSuit.HEARTS, CardRank.FOUR), card(CardSuit.SPADES, CardRank.FIVE)), 9, 3, vira, -1},

                new Object[]{"Oponente pede aumento, tem Zap e 3, placar muito desfavorável → aumenta (1)",
                        List.of(zap(vira), card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FOUR)), 3, 9, vira, 1},

                new Object[]{"Oponente pede aumento, só cartas medianas, placar favorável → aceita (0)",
                        List.of(card(CardSuit.CLUBS, CardRank.KING), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.TWO)), 8, 5, vira, 0},

                new Object[]{"Oponente pede aumento, casal (Zap + Copas), placar empatado → aumenta (1)",
                        List.of(zap(vira), card(CardSuit.HEARTS, CardRank.FIVE), card(CardSuit.CLUBS, CardRank.FIVE)), 6, 6, vira, 1},

                new Object[]{"Oponente pede aumento, só uma manilha (não Zap), placar empatado → recusa (-1)",
                        List.of(manilha(vira), card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.HEARTS, CardRank.SEVEN)), 10, 10, vira, -1},

                new Object[]{"Oponente pede aumento, cartas ruins, placar empatado → recusa (-1)",
                        List.of(card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.HEARTS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX)), 10, 10, vira, -1},

                new Object[]{"Oponente pede aumento, 3's na mão, placar muito alto para jogador → aceita (0)",
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SEVEN)), 12, 5, vira, 0},

                new Object[]{"Oponente pede aumento, cartas fortes, placar muito baixo para jogador → aumenta (1)",
                        List.of(manilha(vira), zap(vira), card(CardSuit.HEARTS, CardRank.FIVE)), 1, 8, vira, 1}
        );
    }


    private static TrucoCard card(CardSuit suit, CardRank rank) {
        return TrucoCard.of(rank, suit);
    }

    private static TrucoCard manilha(TrucoCard vira) {
        CardRank manilhaRank = vira.getRank().next();
        return TrucoCard.of(manilhaRank, CardSuit.SPADES);
    }

    private static TrucoCard zap(TrucoCard vira) {
        CardRank manilhaRank = vira.getRank().next();
        return TrucoCard.of(manilhaRank, CardSuit.CLUBS);
    }
}
