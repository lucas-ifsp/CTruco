package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.StepBuilder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
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
                assertThat(firstRound.getWhichBotShouldPlayFirst()).isEmpty();
            }

            @Test
            @DisplayName("Make sure the bot is not the first to play")
            void makeSureTheBotIsNotTheFirstToPlay () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)));
                assertThat(firstRound.getWhichBotShouldPlayFirst()).isPresent();
            }

            @Test
            @DisplayName("Make sure the bot have One or more Cards Higher Of Opponent")
            void makeSureTheBotHaveOneOrMoreCardsHigherOfOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
                assertThat(intel.getCards().get(0).compareValueTo(intel.getOpponentCard().get(),
                        intel.getVira())).isPositive();
            }
            
            @Test
            @DisplayName("Sure to play the lowest card in the hand higher than the opponent")
            void sureToPlayTheLowestCardInTheHandHigherThanTheOpponentS () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));
                assertThat(intel.getCards().get(0).compareValueTo(intel.getOpponentCard().get(),
                        intel.getVira())).isPositive().isLessThan(intel.getCards().get(2).compareValueTo
                        (intel.getOpponentCard().get(), intel.getVira()));
            }

            @Test
            @DisplayName("Be sure to play the only card that beats the opponent")
            void beSureToPlayTheOnlyCardThatBeatsTheOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
                assertThat(intel.getCards().get(2).compareValueTo(intel.getOpponentCard().get(),
                        intel.getVira())).isPositive();
            }

            @Test
            @DisplayName("Be sure to play the lowest card in the hand smaller than the opponent")
            void beSureToPlayTheLowestCardInTheHandSmallerThanTheOpponent () {
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
                assertThat(intel.getCards().get(2).compareValueTo(intel.getOpponentCard().get(),
                        intel.getVira())).isNegative().isLessThan(intel.getCards().get(0).compareValueTo
                        (intel.getOpponentCard().get(), intel.getVira()));
            }
            
            @Test
            @DisplayName("Make sure not to play first round clubs or hearts")
            void makeSureNotToPlayFirstClubsOrHearts () {
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));
                assertThat(intel.getCards().get(0).getSuit()).isNotIn(CardSuit.CLUBS, CardSuit.HEARTS);
            }
            
            @Test
            @DisplayName("Make sure you have cards to play")
            void makeSureYouHaveCardsToPlay () {
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                assertThat(intel.getCards().size()).isGreaterThan(0);
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

    /*private StepBuilder stepBuilder;

    private List<GameIntel.RoundResult> roundResults;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;

    private TrucoCard vira;

    @Test
    @DisplayName("Creating a GameIntel object")
    void creatingAGameIntelObject () {
        roundResults = List.of(GameIntel.RoundResult.DREW);
        openCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
        vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
        stepBuilder = StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(3);
        GameIntel intel = stepBuilder.opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)).build();
        System.out.println("Cartas do meu bot");
        for (int i = 0; i < intel.getCards().size(); i++) {
            System.out.printf("Carta %d com valor %d \n", (i + 1), intel.getCards().get(i).getRank().value());
            System.out.printf("Carta %d com rank %s \n", (i + 1), intel.getCards().get(i));
        }
        System.out.println("Carta do oponente -> " + intel.getOpponentCard());
        System.out.println("Cartas abertas -> " + intel.getOpenCards());
        System.out.println("Vira -> " + intel.getVira());
    }*/
}