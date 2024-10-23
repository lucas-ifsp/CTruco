package com.local.newton.dolensi.sabotabot;


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
            var cards = IntelMock.cardListAC4C3H();
            when(intel.getCards()).thenReturn(cards);
            when(intel.getVira()).thenReturn(IntelMock.vira5C());

            assertNotNull(sut.chooseCard(intel).content());
        }

        @Nested
        @DisplayName("First Round Plays")
        class FirstRoundTests{

            @Nested
            @DisplayName("Is First To Play")
            class IsFirstToPlay{
                @Test
                @DisplayName("Should play a strong card if is first to play")
                void shouldPlayAStrongCardIfIsFirstToPlay(){
                    var cards = IntelMock.cardListAC4C3H();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.vira5C());

                    when(intel.getOpponentCard()).thenReturn(Optional.empty());
                    assertEquals(cards.get(2), sut.chooseCard(intel).content());
                }

                @Test
                @DisplayName("Should play a strong card non manilha")
                void shouldPlayAStrongCardNonManilha(){
                    var cards = IntelMock.cardListAC4C3H();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.vira2H());

                    when(intel.getOpponentCard()).thenReturn(Optional.empty());
                    assertEquals(cards.get(0), sut.chooseCard(intel).content());
                }

                @Test
                @DisplayName("Should play diamonds if has it and good values")
                void shouldPlayDiamondsIfHasItAndGoodValues(){
                    var cards = IntelMock.cardListAD2CAH();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.viraKC());

                    assertTrue(sut.chooseCard(intel).content().isOuros(intel.getVira()));
                }

                @Test
                @DisplayName("Should keep diamonds if it is the strongest card")
                void shouldKeepDiamondsIfItIsTheStrongestCard(){
                    var cards = IntelMock.cardListAD7CKH();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.viraKC());

                    assertFalse(sut.chooseCard(intel).content().isOuros(intel.getVira()));
                }

            }

            @Nested
            @DisplayName("Is Second To Play")
            class IsSecondToPlay{
                @Test
                @DisplayName("Should play the weakest card if other player plays a strong card")
                void shouldPlayTheWeakestCardIfOtherPlayerPlaysAStrongCard(){
                    var cards = IntelMock.cardListAC4C3H();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.vira5C());

                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertEquals(cards.get(1), sut.chooseCard(intel).content());
                }

                @Test
                @DisplayName("Should play a card greater the opponent's if it has")
                void shouldPlayACardGreaterTheOpponentsIfItHas(){
                    var cards = IntelMock.cardListAC4C3H();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.vira5C());

                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                    assertEquals(cards.get(2), sut.chooseCard(intel).content());
                }

                @Test
                @DisplayName("Should draw and keep a manilha next if it has it")
                void shouldDrawAndKeepAManilhaIfItHasIt(){
                    var cards = IntelMock.cardListAD7CKH();
                    when(intel.getCards()).thenReturn(cards);
                    when(intel.getVira()).thenReturn(IntelMock.vira6C());

                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
                    assertEquals(cards.get(2), sut.chooseCard(intel).content());
                }
            }

        }

        @Nested
        @DisplayName("Second Round Plays")
        class SecondRoundTests{
            @Test
            @DisplayName("Should play a card in second round")
            void shouldPlayACardInSecondRound(){
                var cards = IntelMock.cardList4C3D();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                assertNotNull(sut.chooseCard(intel).content());
            }

            @Test
            @DisplayName("Should play the greatest card if draw first round")
            void shouldPlayTheGreatestCardIfDrawFirstRound(){
                var cards = IntelMock.cardList4C3D();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira3C());

                when(intel.getRoundResults()).thenReturn(IntelMock.roundResultDrewIn1st());
                assertEquals(cards.get(0), sut.chooseCard(intel).content());
            }

            @Test
            @DisplayName("Should discard the lowest if won first round and has strong card")
            void shouldDiscardTheLowestIfWOnFirstRoundAndHasStrongCard(){
                var cards = IntelMock.cardList4C3D();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira3C());

                when(intel.getRoundResults()).thenReturn(IntelMock.roundResultWonIn1st());
                assertTrue(sut.chooseCard(intel).isDiscard());
            }
        }

        @Nested
        @DisplayName("Third Round Plays")
        class ThirdRoundTests{
            @Test
            @DisplayName("Should play a card in third round")
            void shouldPlayACardInThirdRound(){
                var cards = IntelMock.cardList2H();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                assertNotNull(sut.chooseCard(intel).content());
            }
        }

    }

    @Nested
    @DisplayName("Decide If Raises")
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

            var cards = IntelMock.cardList4C3D();
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

        @Test
        @DisplayName("should rise if won the first lost the second and will empate the third")
        void shouldRiseIfWonTheFirstLostTheSecondAndWillEmpateTheThird(){

            when(intel.getOpponentCard()).thenReturn(
                    Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS))
            );

            when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));

            assertThat(sut.decideIfRaises(intel)).isTrue();

        }

        @Test
        @DisplayName("should rise if it has ten points and four or more different to opponent")
        void shouldRiseIfItHasTenPointsAndFourOrMoreDifferentToOpponent(){

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
            when(intel.getScore()).thenReturn(10);
            when(intel.getOpponentScore()).thenReturn(6);

            assertThat(sut.decideIfRaises(intel)).isTrue();
        }
    }

    @Nested
    @DisplayName("Get Raise Response")
    class GetRaiseResponseTests{


        @Test
        @DisplayName("should accept rise if has two manilhas card")
        void shouldAcceptRiseIfHasTwoManilhasCard(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));

            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));

            assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);

        }

        @Test
        @DisplayName("should up rise if won one round and has zap")
        void shouldUpRiseIfWonOneRoundAndHasZap(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON,
                                                             GameIntel.RoundResult.LOST));

            assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);

        }


        @Test
        @DisplayName("should accept rise if opponent has eleven points")
        void shouldAcceptRiseIfOpponentHasElevenPoints(){

            when(intel.getOpponentScore()).thenReturn(11);

            assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);

        }

        @Test
        @DisplayName("should up rise if has manilhas major casal")
        void shouldUpRiseIfHasManilhasMajorCasal(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));

            assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);
        }

        @Test
        @DisplayName("should not rise if dont have manilha")
        void shouldNotRiseIfDontHaveManilha(){

            when(intel.getCards()).thenReturn(
                    List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

            assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
        }



    }

    @Nested
    @DisplayName("Get Mao De Onze Response Tests")
    class GetMaoDeOnzeResponseTests{
        @Test
        @DisplayName("Should return true if has two manilhas")
        void shouldReturnTrueIfHasTwoManilhas(){
            var cards = IntelMock.cardListAD2CAH();
            when(intel.getCards()).thenReturn(cards);

            assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should return true if has 2 and 3")
        void shouldReturnTrueIfHas2and3(){
            var cards = IntelMock.cardList3D7C2H();
            when(intel.getCards()).thenReturn(cards);

            assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should return true if has a 3 and two Aces")
        void shouldReturnTrueIfHasA3AndTwoAces(){
            var cards = IntelMock.cardList3DACAH();
            when(intel.getCards()).thenReturn(cards);

            assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should return false if has an only good card")
        void shouldReturnFalseIfHasAnOnlyGoodCard(){
            var cards = IntelMock.cardList3D5CQH();
            when(intel.getCards()).thenReturn(cards);

            assertThat(sut.getMaoDeOnzeResponse(intel)).isFalse();
        }
    }
}
