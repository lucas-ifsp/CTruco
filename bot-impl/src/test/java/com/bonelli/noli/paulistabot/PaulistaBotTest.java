package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaulistaBotTest {

    @Mock
    GameIntel intel;
    @InjectMocks
    FirstRound firstRound;
    
    @Nested
    @DisplayName("Testing first round")
    class FirstRoundTest {
        @Nested
        @DisplayName("Playing cards")
        class PlayingCards {
            @Test
            @DisplayName("Make sure the bot is the first to play")
            void makeSureTheBotIsTheFirstToPlay () {
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                assertThat(firstRound.getWhichBotShouldPlayFirst(intel)).isEmpty();
            }

            @Test
            @DisplayName("Make sure the bot is not the first to play")
            void makeSureTheBotIsNotTheFirstToPlay () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)));
                assertThat(firstRound.getWhichBotShouldPlayFirst(intel)).isPresent();
            }

            @Test
            @DisplayName("Make sure the bot have One or more Cards Higher Of Opponent")
            void makeSureTheBotHaveOneOrMoreCardsHigherOfOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
                assertThat(firstRound.getCountCardsAreHigherOfOpponent(intel.getCards(), intel.getOpponentCard().get(),
                        intel.getVira())).isPositive();
            }
            
            @Test
            @DisplayName("Sure to play the lowest card in the hand higher than the opponent")
            void sureToPlayTheLowestCardInTheHandHigherThanTheOpponentS () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));
                assertThat(firstRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }

            @Test
            @DisplayName("Be sure to play the only card that beats the opponent")
            void beSureToPlayTheOnlyCardThatBeatsTheOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
                assertThat(firstRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(2));
            }

            @Test
            @DisplayName("Be sure to play the lowest card in the hand smaller than the opponent")
            void beSureToPlayTheLowestCardInTheHandSmallerThanTheOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
                assertThat(firstRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(2));
            }
            
            @Test
            @DisplayName("Make sure not to play first round clubs or hearts")
            void makeSureNotToPlayFirstClubsOrHearts () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));
                assertThat(firstRound.chooseCard(intel).value().getSuit()).isNotIn(CardSuit.CLUBS, CardSuit.HEARTS);
            }
            
            @Test
            @DisplayName("Make sure you have cards to play")
            void makeSureYouHaveCardsToPlay () {
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                assertThat(intel.getCards().size()).isGreaterThan(0);
            }
            
            @Test
            @DisplayName("Ensure the total hand value is greater than or equal to 23")
            void ensureTheTotalHandValueIsGreaterThanOrEqualTo23 () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                assertThat(firstRound.calculateCurrentHandValue(intel)).isGreaterThanOrEqualTo(23);
            }
            
            @Test
            @DisplayName("Sure to play the card with medium strength")
            void sureToPlayTheCardWithMediumStrength () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                assertThat(firstRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(2));
            }
            
            @Test
            @DisplayName("Check if you have a manilha in your hand")
            void checkIfYouHaveAShackleInYourHand () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                assertTrue(firstRound.hasManilha(intel));
            }
            
            @Test
            @DisplayName("Check if you have Diamonds or Spades in your hand")
            void checkIfYouHaveDiamondsOrSpadesInYourHand () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                assertTrue(firstRound.hasOurosOrEspadilha(intel));
            }
            
            @Test
            @DisplayName("Sure to play manilha if you have it if the hand value is less than 23")
            void sureToPlayShackleIfYouHaveItIfTheHandValueIsLessThan23 () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)));
                assertThat(firstRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("Certainty to play any card with a value greater than or equal to 1")
            void certaintyToPlayAnyCardWithAValueGreaterThanOrEqualTo1 () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)));
                assertThat(firstRound.chooseCard(intel).value().getRank().value()).isGreaterThanOrEqualTo(1);
            }
        }

        @Nested
        @DisplayName("Decide if raises")
        class DecideRaises {
        }

        @Nested
        @DisplayName("Get raises response")
        class GetRaisesResponse {
            @Test
            @DisplayName("Must accept truco if the initial total value of the hand is greater than or equal to 28")
            void mustAcceptTrucoIfTheInitialTotalValueOfTheHandIsGreaterThanOrEqualTo28 () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));

                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(0);
            }
            
            @Test
            @DisplayName("Must raise truco if you have 2 or 3 and Hearts or Clubs")
            void mustRaiseTrucoIfYouHave2Or3AndHeartsOrClubs () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(1);
            }
            
            @Test
            @DisplayName("You must accept truco if you have 2 or 3 and any Manilha")
            void mustAcceptTrucoIfYouHave2Or3AndAnyManilha () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(0);
            }
            
            @Test
            @DisplayName("Must refuse truco if you have only 2 or 3 and any other card in your hand")
            void mustRefuseTrucoIfYouHaveOnly2Or3AndAnyOtherCardInYourHand () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(-1);
            }
            
            @Test
            @DisplayName("Must accept truco if it has two manilhas")
            void mustAcceptTrucoIfItHasTwoShackles () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(0);
            }
            
            @Test
            @DisplayName("Should increase the truco if you have a bigger couple")
            void shouldIncreaseTheTrucoIfYouHaveABiggerCouple () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(1);
            }

            @Test
            @DisplayName("Should increase the truco if you have a black couple")
            void shouldIncreaseTheTrucoIfYouHaveABlackCouple () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(1);
            }
            
            @Test
            @DisplayName("Must refuse truco if you don't have a bigger couple or a black couple")
            void mustRefuseTrucoIfYouDonTHaveABiggerCoupleOrABlackCouple () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                assertThat(firstRound.getRaiseResponse(intel)).isEqualTo(-1);
            }
        }
    }

    @Nested
    @DisplayName("Testing second round")
    class SecondRoundTest {

    }

    @Nested
    @DisplayName("Testing third round")
    class ThirdRoundTest {

    }
}