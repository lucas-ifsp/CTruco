package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatriciaAparecidaTest {
    @Mock
    GameIntel intel;
    private PatriciaAparecida patricia;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    void setUp() {
        patricia = new PatriciaAparecida();
    }

    @Nested
    @DisplayName("Check the variables to calculate the probability")
    class checkVariables {

        @Nested
        @DisplayName("Number of remainder cards")
        class RemainderCards{

            @Test
            @DisplayName("Start Of the Game")
            public void startOfTheGame() {

                List<TrucoCard> randomBotCards = generateTrucoCardToPlayList(3);
                List<TrucoCard> openCards = generateTrucoCardToPlayList(1);

                when(intel.getCards()).thenReturn(randomBotCards);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfRemainderCards(intel),36);
            }

            @Test
            @DisplayName("Last Round")
            public void lastRound () {

                List<TrucoCard> botCards = generateTrucoCardToPlayList(1);
                List<TrucoCard> openCards = generateTrucoCardToPlayList(6);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfRemainderCards(intel),33);
            }

        }

        @Nested
        @DisplayName("Number of best cards known")
        class BestCardsKnown{
            private TrucoCard openCard;
            private List<TrucoCard> botCards;

            @BeforeEach
            void setCards(){
                botCards =  List.of(TrucoCard.of(ACE,CLUBS),
                        TrucoCard.of(ACE,HEARTS),
                        TrucoCard.of(ACE,SPADES));
                openCard = TrucoCard.of(ACE,DIAMONDS);
            }

            @Test
            @DisplayName("Have no better cards known")
            public void ShouldReturnZeroIfHaveNoBetterCardsKnown(){
                TrucoCard vira = TrucoCard.of(FOUR,SPADES);
                List<TrucoCard> openCards = List.of(vira, openCard);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1),intel),0);
            }

            @Test
            @DisplayName("Have better cards known")
            public void ShouldReturnANumberOfBetterCardsKnown(){
                TrucoCard vira = TrucoCard.of(THREE,SPADES);
                List<TrucoCard> openCards = List.of(vira, openCard);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1), intel),1);
            }
        }

        @Nested
        @DisplayName("Number of best cards unknown")
        class BestCardsUnkown {

            private TrucoCard vira;
            private List<TrucoCard> botCards;
            private List<TrucoCard> openCards;

            @BeforeEach
            void setCards(){
                botCards = List.of(
                        TrucoCard.of(FOUR, SPADES),
                        TrucoCard.of(FOUR, DIAMONDS),
                        TrucoCard.of(KING, DIAMONDS));
                vira = TrucoCard.of(THREE,SPADES);
                openCards = List.of(
                        vira,
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FOUR, CLUBS));
            }

            @Test
            @DisplayName("Case is Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsManilha() {

                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);

                assertEquals(patricia.getNumberOfBestCardsUnknown(botCards.get(1),intel),0);
            }

            @Test
            @DisplayName("Case isnt Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsntManilha() {

                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);

                assertEquals(patricia.getNumberOfBestCardsUnknown(botCards.get(2),intel), 7);
            }
        }

        @Nested
        @DisplayName("Should return the number of opponent's cards ")
        class CardsOfOpponent {

            @Test
            @DisplayName("Case win the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseWinLastRound() {
                when(intel.getRoundResults()).thenReturn(List.of(LOST,WON));
                assertEquals(patricia.getNumberOfOpponentsCards((intel)),1);
            }

            @Test
            @DisplayName("Case lost the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseLostLastRound(){
                when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                assertEquals(patricia.getNumberOfOpponentsCards(intel),0);
            }
        }
    }

    @Nested
    @DisplayName("Check the probability")
    class Probability{

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        @DisplayName("Should calculate the probability of each bot's cards")
        public void ShouldCalculateTheProbOfEachBotCardsWhen3cards(int qnt) {
            List<TrucoCard> botCards = generateTrucoCardToPlayList(qnt);
            TrucoCard vira = generateRandomCardToPlay();

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(vira);

            assertEquals(patricia.listProbAllCards(intel).size(),botCards.size());
        }

        @Nested
        @DisplayName("Check values")
        class ProbValues{

            private TrucoCard vira;
            private List<TrucoCard> openCards;
            private List<GameIntel.RoundResult> roundResults;

            @BeforeEach
            void setCards(){
                vira = TrucoCard.of(FIVE, CLUBS);
                openCards = List.of(vira, TrucoCard.of(SIX, SPADES));
                roundResults = Collections.EMPTY_LIST;
            }

            @Test
            @DisplayName("Should return the same prob when Know all better cards")
            public void ShouldReturnTheSameProbWhenKnowAllBetterCards () {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, CLUBS),
                        TrucoCard.of(SIX, HEARTS),
                        TrucoCard.of(SIX, DIAMONDS));

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getRoundResults()).thenReturn(roundResults);

                List<Double> probList = patricia.listProbAllCards(intel);

                assertEquals(probList.get(0), probList.get(1), probList.get(2));
            }

            @Test
            @DisplayName("Should return proportional prob to card rank")
            public void ShouldReturnProportionalProbToCardRank () {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, CLUBS),
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(KING, DIAMONDS));

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getRoundResults()).thenReturn(roundResults);

                List<Double> probList = patricia.listProbAllCards(intel);

                assertTrue(intel.getCards().get(0).compareValueTo(intel.getCards().get(2),vira)>0);
                assertTrue(intel.getCards().get(2).compareValueTo(intel.getCards().get(1),vira)>0);
                assertTrue(probList.get(0) < probList.get(2));
                assertTrue(probList.get(2) < probList.get(1));
            }
        }
    }


    @Nested
    @DisplayName("Exception Thrown by Methods Tests")
    class ExceptionThrownByMethodsTests{
        private List<TrucoCard> botCards;
        private TrucoCard vira;
        private List<TrucoCard> openCards ;


        @BeforeEach
        void setCards(){
            botCards = List.of(TrucoCard.of(SIX, SPADES),
                    TrucoCard.of(FOUR,SPADES),
                    TrucoCard.of(TWO,HEARTS));
            vira = TrucoCard.of(FIVE,CLUBS);
            openCards = List.of(vira);
        }
        @Nested
        @DisplayName("Mão de Onze Exception Tests")
        class MaoDeOnzeExceptions{

            @Test
            @DisplayName("Should Throw exception if bot accepts mão de onze without 11 points")
            public void shouldThrowExceptionInMaoDeOnzeWhenBotDoesntHave11Points(){
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON,LOST), openCards, vira, 1).
                        botInfo(botCards, 10).
                        opponentScore(10);

                assertThrows(IllegalArgumentException.class , () -> patricia.getMaoDeOnzeResponse(stepBuilder.build())) ;   }
        }
        @Nested
        @DisplayName("Decide if Raises Tests")
        class DecideIfRaisesExceptions{
            @Test
            @DisplayName("Should Throw exception if bot raises beyond 12 points")
            public void shouldThrowExceptionInDecideIfRaisesIfBotRaiseAfter12Points() {
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON, LOST), openCards, vira, 2).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertThrows(IllegalArgumentException.class, () -> patricia.decideIfRaises(stepBuilder.build()));
            }
        }
        @Nested
        @DisplayName("ChoiceCardExceptions")
        class chooseCardExceptions{
            @Test
            @DisplayName("Should Throw exception if bot chooses null as card")
            public void shouldThrowExceptionIfDecideToChooseNullCard() {
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON, LOST), openCards, vira, 13).
                        botInfo(Collections.EMPTY_LIST, 0).
                        opponentScore(0);

                assertThrows(IllegalStateException.class, () -> patricia.chooseCard(stepBuilder.build()));
            }
        }
    }

    @Nested
    @DisplayName("choose card tests ")
    class ChooseCard{
        private List<TrucoCard> botCards;
        private TrucoCard vira;

        @BeforeEach
        void setCards(){
            botCards = List.of(TrucoCard.of(KING, HEARTS), //2
                    TrucoCard.of(FOUR,SPADES), // 3
                    TrucoCard.of(TWO,HEARTS)); // 1

            vira = TrucoCard.of(FIVE,CLUBS);

        }

        @Test
        @DisplayName("Choose the weakest card that wins the hand")
        public void ChooseWeakestCardThatWinsHand(){
            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(Collections.EMPTY_LIST,openCards,vira,1).
                    botInfo(botCards,0).
                    opponentScore(0)
                    .opponentCard(TrucoCard.of(QUEEN,DIAMONDS));

            assertEquals(CardToPlay.of(TrucoCard.of(KING, HEARTS)).content() ,patricia.chooseCard(stepBuilder.build()).value());
        }
        @Test
        @DisplayName("Discard the weakest card that loses the hand")
        public void DiscardWeakestCardThatLosesHand(){
            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(Collections.EMPTY_LIST,openCards,vira,1).
                    botInfo(botCards,0).
                    opponentScore(0)
                    .opponentCard(TrucoCard.of(SIX,CLUBS));

            assertEquals(CardToPlay.of(TrucoCard.of(FOUR,SPADES)).content() ,patricia.chooseCard(stepBuilder.build()).content());
            assertEquals(TrucoCard.closed(),patricia.chooseCard(stepBuilder.build()).value());
        }
        @Test
        @DisplayName("Draws if can't win and can draw")
        public void DrawIfCantWin(){
            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(Collections.EMPTY_LIST,openCards,vira,1).
                    botInfo(botCards,0).
                    opponentScore(0)
                    .opponentCard(TrucoCard.of(TWO,CLUBS));

            assertEquals(CardToPlay.of(TrucoCard.of(TWO,HEARTS)).value() ,patricia.chooseCard(stepBuilder.build()).value());
        }

    }


    public GameIntel.RoundResult generateRandomRoundResult() {
        GameIntel.RoundResult[] values = GameIntel.RoundResult.values();

        Random random = new Random();
        int index = random.nextInt(values.length);

        return values[index];
    }

    public CardRank generateRandomCardRank() {
        CardRank[] values = CardRank.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }

    public CardSuit generateRandomCardSuit() {
        CardSuit[] values = CardSuit.values();
        Random random = new Random();
        int index = random.nextInt(values.length);

        while (values[index] == CardSuit.HIDDEN) {
            index = random.nextInt(values.length);
        }

        return values[index];
    }

    public TrucoCard generateRandomCard(){ //assegura que CardRank.HIDDEN só receba CardSuit.HIDDEN
        CardRank cardRank = generateRandomCardRank();
        if(cardRank == CardRank.HIDDEN){
            return TrucoCard.of(cardRank,CardSuit.HIDDEN);
        }
        return TrucoCard.of(cardRank,generateRandomCardSuit());
    }

    public TrucoCard generateRandomCardToPlay(){ //sem cartas viradas (hidden)
        TrucoCard cardToPlay = generateRandomCard();
        while (cardToPlay.getSuit() == CardSuit.HIDDEN) {
            cardToPlay = generateRandomCard();
        }
        return cardToPlay;
    }

    public List<TrucoCard> generateTrucoCardToPlayList(int qnt){
        List<TrucoCard> cardToPlayList = new ArrayList<>();
        for(int i = 0; i < qnt ; i++){
            cardToPlayList.add(generateRandomCard());
        }
        return cardToPlayList;
    }

}