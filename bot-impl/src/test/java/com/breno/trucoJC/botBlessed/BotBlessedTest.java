package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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

    private static TrucoCard card(CardSuit suit, CardRank rank) {
        return TrucoCard.of(rank, suit);
    }

    private static TrucoCard manilha(TrucoCard vira) {
        CardRank manilhaRank = vira.getRank().next();
        return TrucoCard.of(manilhaRank, CardSuit.SPADES);
    }

    private static TrucoCard zap(TrucoCard vira) {
        return TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
    }
}
