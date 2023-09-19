package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaulistaBotTest {

    @Mock
    GameIntel intel;
    @InjectMocks
    FirstRound firstRound;
    @InjectMocks
    SecondRound secondRound;
    
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
            
            @Disabled
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
            @Test
            @DisplayName("Sure not to ask for truco if opponent is in hand of eleven")
            void sureNotToAskForTrucoIfOpponentIsInHandOfEleven () {
                when(intel.getOpponentScore()).thenReturn(11);
                assertFalse(firstRound.decideIfRaises(intel));
            }

            @Test
            @DisplayName("Sure not to ask fro truco if score is in hand of eleven")
            void sureNotToAskFroTrucoIfScoreIsInHandOfEleven () {
                when(intel.getScore()).thenReturn(11);
                assertFalse(firstRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Make sure not to ask for truco if the hand is worth 12")
            void makeSureNotToAskForTrucoIfTheHandIsWorth12 () {
                when(intel.getHandPoints()).thenReturn(12);
                assertFalse(firstRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Make sure the enemy's score doesn't go above 12 if they ask for truco")
            void makeSureTheEnemySScoreDoesnTGoAbove12IfTheyAskForTruco () {
                when(intel.getOpponentScore()).thenReturn(10);
                assertFalse(firstRound.decideIfRaises(intel));
            }

            @Test
            @DisplayName("Make sure the score doesn't go above 12 if they ask for truco")
            void makeSureTheScoreDoesnTGoAbove12IfTheyAskForTruco () {
                when(intel.getOpponentScore()).thenReturn(10);
                assertFalse(firstRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if the average strength card compared to the enemy's is greater than or equal to 8")
            void askForTrucoIfTheAverageStrengthCardComparedToTheEnemySIsGreaterThanOrEqualTo7 () {
                when(intel.getScore()).thenReturn(0);
                when(intel.getOpponentScore()).thenReturn(0);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
                assertTrue(firstRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if hand value is greater than or equal to 24")
            void askForTrucoIfHandValueIsGreaterThanOrEqualTo24 () {
                when(intel.getScore()).thenReturn(0);
                when(intel.getOpponentScore()).thenReturn(0);
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
                assertTrue(firstRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if you have 2 manilhas")
            void askForTrucoIfYouHave2Manilhas () {
                when(intel.getScore()).thenReturn(0);
                when(intel.getOpponentScore()).thenReturn(0);
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                assertTrue(firstRound.decideIfRaises(intel));
            }
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
        
        @Nested
        @DisplayName("Get mao de onze")
        class GetMaoDeOnze {
            @Test
            @DisplayName("Accept truco hand if hand value is greater than or equal to 25 and has manilha")
            void acceptTrucoHandIfHandValueIsGreaterThanOrEqualTo25AndHasManilha () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)));
                assertTrue(firstRound.getMaoDeOnzeResponse(intel));
            }
            
            @Test
            @DisplayName("Accept hand of eleven if opponent's score is less than 9 and has a 2 or 3 in hand")
            void acceptHandOfElevenIfOpponentSScoreIsLessThan9AndHasA2Or3InHand () {
                when(intel.getOpponentScore()).thenReturn(8);
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertTrue(firstRound.getMaoDeOnzeResponse(intel));
            }
            
            @Test
            @DisplayName("Accept hand where opponent's score is greater than or equal to 9 and card with average strength greater than or equal to 9")
            void acceptHandWhereOpponentSScoreIsGreaterThanOrEqualTo9AndCardWithAverageStrengthGreaterThanOrEqualTo9 () {
                when(intel.getOpponentScore()).thenReturn(9);
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertTrue(firstRound.getMaoDeOnzeResponse(intel));
            }
            
            @Test
            @DisplayName("Accept hand where opponent's score is less than or equal to 9 and card with average strength less than or equal to 9")
            void acceptHandWhereOpponentSScoreIsLessThanOrEqualTo9AndCardWithAverageStrengthLessThanOrEqualTo9 () {
                when(intel.getOpponentScore()).thenReturn(8);
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertTrue(firstRound.getMaoDeOnzeResponse(intel));
            }
            
            @Test
            @Disabled
            @DisplayName("Must decline hand of eleven if hand value is less than 25")
            void mustDeclineHandOfElevenIfHandValueIsLessThan25 () {
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES), TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)));
                assertFalse(firstRound.getMaoDeOnzeResponse(intel));
            }
        }
    }

    @Nested
    @DisplayName("Testing second round")
    class SecondRoundTest {
        @Nested
        @DisplayName("Playing cards")
        class PlayingCards {
            @Test
            @DisplayName("If you won the first round and have zap cover the weakest card")
            void ifYouWonTheFirstRoundAndHaveZapCoverTheWeakestCard () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(TrucoCard.closed());
            }
            
            @Test
            @DisplayName("Play the weakest card in your hand if you won the first round and have power 17 or greater")
            void playTheWeakestCardInYourHandIfYouWonTheFirstRoundAndHavePower17OrGreater () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("Play the strongest card if hand value is between 5 and 13")
            void playTheStrongestCardIfHandValueIsBetween8And16 () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("Sure to play weaker card if hand value is not good")
            void sureToPlayWeakerCardIfHandValueIsNotGood () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("Discard any card if you lost the first round and don't have any cards that beat your opponent and not have same value of opponent")
            void discardAnyCardIfYouLostTheFirstRoundAndDonTHaveAnyCardsThatBeatYourOpponentAndNotHaveSameValueOfOpponent () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(TrucoCard.closed());
            }
            
            @Test
            @DisplayName("Play the only card you win from your opponent if you lost the first round")
            void playTheOnlyCardYouWinFromYourOpponentIfYouLostTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(1));
            }
            
            @Test
            @DisplayName("Playing the weakest card from the opponent's hand if they lost the first round")
            void playingTheWeakestCardFromTheOpponentSHandIfTheyLostTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("If the opponent has covered the card, play a card with a lower value")
            void ifTheOpponentHasCoveredTheCardPlayACardWithALowerValue () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.closed()));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(1));
            }
            
            @Test
            @DisplayName("Play card with the same value to drew if you don't have any that beat the opponent")
            void playCardWithTheSameValueToDrewIfYouDonTHaveAnyThatBeatTheOpponent () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
            
            @Test
            @DisplayName("If you drew the previous round, play the strongest card at the start")
            void ifYouDrewThePreviousRoundPlayTheStrongestCardAtTheStart () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
                assertThat(secondRound.chooseCard(intel).value()).isEqualTo(intel.getCards().get(0));
            }
        }

        @Nested
        @DisplayName("Decide if raises")
        class DecideRaises {
            @Test
            @DisplayName("Should call truco if your hand is weak and you won the first round with a strong card")
            void shouldCallTrucoIfYourHandIsWeakAndYouWonTheFirstRoundWithAStrongCard () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), 
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)));
                assertTrue(secondRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if you have a total hand value greater than or equal to 15")
            void askForTrucoIfYouHaveATotalHandValueGreaterThanOrEqualTo15 () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
                assertTrue(secondRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if you have a shackle and have won the first round")
            void askForTrucoIfYouHaveAShackleAndHaveWonTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
                assertTrue(secondRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("Ask for truco if you lost the first round and have a hand with a value greater than or equal to 18")
            void askForTrucoIfYouLostTheFirstRoundAndHaveAHandWithAValueGreaterThanOrEqualTo18 () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
                when(intel.getHandPoints()).thenReturn(1);
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.TWO.next(), CardSuit.SPADES)));
                assertTrue(secondRound.decideIfRaises(intel));
            }
            
            @Test
            @DisplayName("If you drew the first round, trucar if you have a manilha")
            void ifYouDrewTheFirstRoundTrucarIfYouHaveAManilha () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
                assertTrue(secondRound.decideIfRaises(intel));
            }
        }

        @Nested
        @DisplayName("Get raises response")
        class GetRaisesResponse {
            @Test
            @DisplayName("Must raise truco if you have a shackle and have won the first round")
            void mustRaiseTrucoIfYouHaveAShackleAndHaveWonTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(1);
            }

            @Test
            @DisplayName("Must accept truco if hand value is greater than or equal to 17 and have won the first round")
            void mustAcceptTrucoIfHandValueIsGreaterThanOrEqualTo17AndHaveWonTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(0);
            }

            @Test
            @DisplayName("Must decline truco if you have a weak hand")
            void mustDeclineTrucoIfYouHaveAWeakHand () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(-1);
            }
            
            @Test
            @DisplayName("You must accept truco if you have zap and a hand greater than or equal to 21")
            void youMustAcceptTrucoIfYouHaveZapAndAHandGreaterThanOrEqualTo21 () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(0);
            }
            
            @Test
            @DisplayName("Must accept truco if you drew the first one and have a manilha")
            void mustAcceptTrucoIfYouDrewTheFirstOneAndHaveAManilha () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(0);
            }
            
            @Test
            @DisplayName("Must accept truco if you have a 3 in your hand if you drew the first round")
            void mustAcceptTrucoIfYouHaveA3InYourHandIfYouDrewTheFirstRound () {
                when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertThat(secondRound.getRaiseResponse(intel)).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("Testing third round")
    class ThirdRoundTest {

    }
}