package com.manhani.stefane.reimubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.manhani.stefane.reimubot.ReimuBot.REFUSE;
import static com.manhani.stefane.reimubot.ReimuBot.ACCEPT;
import static com.manhani.stefane.reimubot.ReimuBot.RERAISE;

class ReimuBotTest {

    private ReimuBot reimuBot;
    
    @BeforeEach
    void setUp(){ reimuBot = new ReimuBot(); }
    
    @Nested
    @DisplayName("chooseCard tests")
    class ChooseCardTests {
        @Test
        @DisplayName("Should select weakest card if cannot defeat opponent")
        void selectWeakestIfLose() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
                    .build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        }

        @Test
        @DisplayName("Should select the weakest card on first round if has casal maior")
        void selectWeakestIfCasalMaior(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
        }

        @Test
        @DisplayName("Should select the weakest card on first round if has casal menor")
        void selectWeakestIfCasalMenor(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
        }

        @Test
        @DisplayName("Should select the weakest card on first round if has casal preto")
        void selectWeakestIfCasalPreto(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
        }

        @Test
        @DisplayName("Should select the weakest card on first round if has casal vermelho")
        void selectWeakestIfCasalVermelho(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
        }
    }

    @Nested
    @DisplayName("decideIfRaises tests")
    class DecideIfRaisesTests {
        @Test
        @DisplayName("Should raise if on round 2 and has two manilhas")
        void raiseIfTwoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(GameIntel.RoundResult.WON),
                            List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)),
                            vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(1).opponentCard(TrucoCard.of(CardRank.KING, CardSuit.CLUBS))
                    .build();
            assertThat(reimuBot.decideIfRaises(step)).isTrue();
        }

        @Test
        @DisplayName("Should raise on first round if has casal maior")
        void raiseIfCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(),
                            List.of(),
                            vira, 0)
                    .botInfo(reimuCards, 0)
                    .opponentScore(0).opponentCard(null)
                    .build();
            assertThat(reimuBot.decideIfRaises(step)).isTrue();
        }

        @Test
        @DisplayName("Should raise if on round two and wins first round and has two manilhas")
        void raiseIfWinsFirstAndTwoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(GameIntel.RoundResult.WON),
                            List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)),
                            vira, 1)
                    .botInfo(reimuCards, 1)
                    .opponentScore(0).opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS))
                    .build();
            assertThat(reimuBot.decideIfRaises(step)).isTrue();
        }

        @Test
        @DisplayName("If the opponent has mao de onze, don't raise")
        void opponentMaoDeOnzeDontRaise() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(),
                            List.of(),
                            vira, 1)
                    .botInfo(reimuCards, 0)
                    .opponentScore(11)
                    .build();
            assertThat(reimuBot.decideIfRaises(step)).isFalse();
        }

    }
    
    @Nested
    @DisplayName("getMaoDeOnzeResponse tests")
    class GetMaoDeOnzeResponseTests {
        @Test
        @DisplayName("Should refuse mão de onze if enemy has 9 points and cards are weak")
        void refuseIfCardsSuck() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(),
                            List.of(),
                            vira, 1).botInfo(reimuCards, 11)
                    .opponentScore(9)
                    .build();
            assertThat(reimuBot.getMaoDeOnzeResponse(step)).isFalse();
        }



    }

    @Nested
    @DisplayName("getRaiseResponseTests")
    class GetRaiseResponseTests {
        @Test
        @DisplayName("Should re-raise if has two manilhas")
        void acceptsIfTwoManilhas(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(GameIntel.RoundResult.WON),
                            List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)),
                            vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(1).opponentCard(TrucoCard.of(CardRank.KING, CardSuit.CLUBS))
                    .build();
            assertThat(reimuBot.getRaiseResponse(step)).isEqualTo(RERAISE);
        }
    }

}