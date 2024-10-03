package com.bianca.joaopedro.lgtbot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LgtbotTest {
    private Lgtbot lgtbot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        lgtbot = new Lgtbot();

    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem menos de 7 pontos e jogador possui boas cartas")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreLessThan7_HasThreeGoodCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> goodCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(goodCards, 11)
                    .opponentScore(6);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem menos de 11 pontos e jogador possui cartas fortes")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreLessThan11_HasThreeStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(strongCards, 11)
                    .opponentScore(10);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem exatamente 11 pontos")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreEqualTo11() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 11)
                    .opponentScore(11);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }


        @Test
        @DisplayName("Não aceitar mão de onze quando não tem boas cartas e oponente tem menos de 7 pontos")
        public void testShouldNotAcceptMaoDeOnze_WhenNoGoodCardsAndOpponentScoreLessThan7() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> badCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(badCards, 11)
                    .opponentScore(6);
            assertFalse(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }
}
