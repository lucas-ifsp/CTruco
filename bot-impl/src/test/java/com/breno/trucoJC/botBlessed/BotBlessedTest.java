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

    // --- Testes para getMaoDeOnzeResponse ---
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
                new Object[]{"❌ 5x11 com uma manilha → Recusa", 5, 11, List.of(manilha(vira)), vira, false},
                new Object[]{"✅ 9x10 com Zap e 3 → Aceita", 9, 10, List.of(zap(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, true},
                new Object[]{"❌ 0x5 com uma manilha → Recusa", 0, 5, List.of(manilha(vira)), vira, false},
                new Object[]{"✅ 11x11 com manilha e 3 → Aceita", 11, 11, List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE)), vira, true},
                new Object[]{"❌ 0x5 sem manilha ou 3 → Recusa", 0, 5, List.of(card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX)), vira, false},
                new Object[]{"✅ 6x11 com Zap e 3 → Aceita", 6, 11, List.of(zap(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, true},
                new Object[]{"✅ 9x5 com manilha e dois 3 → Aceita", 9, 5, List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.THREE), card(CardSuit.CLUBS, CardRank.THREE)), vira, true},
                new Object[]{"✅ 10x9 com duas manilhas → Aceita", 10, 9, List.of(manilha(vira), manilha(vira)), vira, true},
                new Object[]{"✅ 0x10 com duas manilhas → Aceita", 0, 10, List.of(manilha(vira), manilha(vira)), vira, true},
                new Object[]{"❌ 7x9 com manilha e 3 → Recusa", 7, 9, List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.THREE)), vira, false},
                new Object[]{"✅ 11x6 com manilha e 3 → Aceita", 11, 6, List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE)), vira, true}
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("decideIfRaisesTestCases")
    @DisplayName("Testes para decideIfRaises")
    void testDecideIfRaises(String description, int playerScore, int opponentScore, List<TrucoCard> hand, TrucoCard vira, boolean expected) {
        GameIntel intel = GameIntel.StepBuilder
                .with()
                .gameInfo(List.of(), List.of(vira), vira, 3)
                .botInfo(hand, playerScore)
                .opponentScore(opponentScore)
                .build();

        boolean result = botBlessed.decideIfRaises(intel);
        assertThat(result).as(description).isEqualTo(expected);
    }

    private static Stream<Object[]> decideIfRaisesTestCases() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        return Stream.of(
                new Object[]{"Você com manilha e oponente com 0 pontos → Deve aceitar o aumento",
                        0, 0,
                        List.of(manilha(vira), card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX)),
                        vira, true},

                new Object[]{"Você com 2 ou mais 3's e oponente com 10 pontos → Deve aceitar o aumento",
                        10, 10,
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, true},

                new Object[]{"Você com um Zap e oponente com 9 pontos → Deve aceitar o aumento",
                        9, 9,
                        List.of(zap(vira), card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"Você com cartas fracas (não manilhas ou 3's) e oponente com 11 pontos → Não deve aceitar o aumento",
                        5, 11,
                        List.of(card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, false},

                new Object[]{"Você com 9 pontos e oponente com 11 pontos, sem manilhas ou 3's → Não deve aceitar o aumento",
                        9, 11,
                        List.of(card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, false},

                new Object[]{"Você com 0 pontos e oponente com 11 pontos, sem cartas fortes → Não deve aceitar o aumento",
                        0, 11,
                        List.of(card(CardSuit.CLUBS, CardRank.FOUR), card(CardSuit.SPADES, CardRank.FIVE), card(CardSuit.HEARTS, CardRank.SIX)),
                        vira, false},

                new Object[]{"Você com manilha e oponente com 10 pontos → Deve aceitar o aumento",
                        10, 10,
                        List.of(manilha(vira), card(CardSuit.HEARTS, CardRank.SEVEN), card(CardSuit.CLUBS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"Você com 3's e oponente com 9 pontos → Deve aceitar o aumento",
                        9, 9,
                        List.of(card(CardSuit.CLUBS, CardRank.THREE), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.FOUR)),
                        vira, true},

                new Object[]{"Você com 0 pontos e oponente com 6 pontos, sem manilhas ou 3's → Não deve aceitar o aumento",
                        0, 6,
                        List.of(card(CardSuit.CLUBS, CardRank.FIVE), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.SEVEN)),
                        vira, false},

                new Object[]{"Você com 5 pontos e oponente com 5 pontos, sem cartas fortes → Não deve aceitar o aumento",
                        5, 5,
                        List.of(card(CardSuit.CLUBS, CardRank.SEVEN), card(CardSuit.SPADES, CardRank.SIX), card(CardSuit.HEARTS, CardRank.FIVE)),
                        vira, false},

                new Object[]{"Você com 7 pontos e oponente com 10 pontos, com 1 manilha e 3 → Deve aceitar o aumento",
                        7, 10,
                        List.of(manilha(vira), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.HEARTS, CardRank.SIX)),
                        vira, true},

                new Object[]{"Você com 0 pontos e oponente com 10 pontos, com Zap e 1 3 → Deve aceitar o aumento",
                        0, 10,
                        List.of(zap(vira), card(CardSuit.SPADES, CardRank.THREE), card(CardSuit.CLUBS, CardRank.SIX)),
                        vira, true}
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
