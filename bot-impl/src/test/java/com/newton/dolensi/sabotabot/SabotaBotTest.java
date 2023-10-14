package com.newton.dolensi.sabotabot;


import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SabotaBotTest {
    @Mock
    GameIntel intel;
    @InjectMocks
    SabotaBot sut;

    @Nested
    @DisplayName("Choose Card")
    class ChooseCardTests{

        @Test
        @DisplayName("Should play any card from hand")
        void shouldPlayAnyCardFromHand(){
            var cards = IntelMock.cardList3Cards();
            when(intel.getCards()).thenReturn(cards);
            when(intel.getVira()).thenReturn(IntelMock.vira5C());

            assertNotNull(sut.chooseCard(intel).content());
        }

        @Nested
        @DisplayName("First Round Plays")
        class FirstRoundTests{

            @Test
            @DisplayName("Should play a strong card if is first to play")
            void shouldPlayAStrongCardIfIsFirstToPlay(){
                var cards = IntelMock.cardList3Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                assertEquals(cards.get(2), sut.chooseCard(intel).content());
            }

            @Test
            @DisplayName("Should play the weakest card if other player plays a strong card")
            void shouldPlayTheWeakestCardIfOtherPlayerPlaysAStrongCard(){
                var cards = IntelMock.cardList3Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                assertEquals(cards.get(1), sut.chooseCard(intel).content());
            }

            @Test
            @DisplayName("Should play diamonds if has it and good values")
            void shouldPlayDiamondsIfHasItAndGoodValues(){
                var cards = IntelMock.diamondsAndGoodValues();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.viraKC());

                assertTrue(sut.chooseCard(intel).content().isOuros(intel.getVira()));
            }
        }

        @Nested
        @DisplayName("Second Round Plays")
        class SecondRoundTests{
            @Test
            @DisplayName("Should play a card in second round")
            void shouldPlayACardInSecondRound(){
                var cards = IntelMock.cardList2Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                assertNotNull(sut.chooseCard(intel).content());
            }
        }

        @Nested
        @DisplayName("Third Round Plays")
        class ThirdRoundTests{

        }

    }

    @Nested
    @DisplayName("Decide If Raises decideIfRaises")
    class DecideIfRaisesTests{


        @Test
        @DisplayName("should return false if is the first round")
        void shouldReturnFalseIfIsTheFirstRound(){
            when(intel.getRoundResults()).thenReturn(List.of());
            assertThat(sut.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("should rise if won first round and have one strong manilha card")
        void shouldRiseIfWonFirstRoundAndHaveAStrongManilhaCard(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));

            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

            assertThat(sut.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("should not rise if won first round and have no one strong manilha card")
        void shouldNotRiseIfWonFirstRoundAndHaveNoAStrongManilhaCard(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));

            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

            assertThat(sut.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("should rise if drew the first and have card to win the second round")
        void shouldRiseIfDrewTheFirstAndHaveCardToWinTheSecondRound(){

            var cards = IntelMock.cardList2Cards();
            when(intel.getCards()).thenReturn(cards);

            when(intel.getOpponentCard()).thenReturn(
                    Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS))
            );

            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));

            assertThat(sut.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("should rise if is drew on third round and the opponent card is weak")
        void shouldRiseIfIsDrewOnThirdRoundAndTheOpponentCardIsWeak(){

            when(intel.getOpponentCard()).thenReturn(
                    Optional.of(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS))
            );

            when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));

            assertThat(sut.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("should rise if is drew on thir round and has strong manilha")
        void shouldRiseIfIsDrewOnThirRoundAndHasStrongManilha(){

            when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON));

            assertThat(sut.decideIfRaises(intel)).isTrue();

        }
    }
}
