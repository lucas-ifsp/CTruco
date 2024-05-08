package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JakareDuMatuBotTest {

    private JakareDuMatuBot jakareDuMatuBot;

    @BeforeEach
    public void config() {
        jakareDuMatuBot = new JakareDuMatuBot();
    }

    @Nested
    class getMaoDeOnzeResponse {
        @Test
        @DisplayName("Should accept mao de onze if have an older couple")
        public void ShouldAcceptMaoDeOnzeIfHaveAnOlderCouple() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept mao de onze if have two any manilhas")
        public void ShouldAcceptMaoDeOnzeIfHaveTwoAnyManilhas(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept mao de onze if have high card and any manilha")
        public void ShouldAcceptMaoDeOnzeIfHaveHighCardAndAnyManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept mao de onze if have at least two high cards")
        public void ShouldAcceptMaoDeOnzeIfHaveAtLeastTwoHighCards(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should not accept mao de onze if don't have at least two high cards")
        public void ShouldNotAcceptMaoDeOnzeIfDontHaveAtLeastTwoHighCards(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(false);
        }

    }

    @Nested
    @DisplayName("Decide If Raises Request Point tests")
    class decideIfRaises {
        // First Hand
        @Test
        @DisplayName("Decide that you will ask for tricks in the first round with an older couple")
        public void DecideThatYouWillAskForTricksInTheFirstRoundWithAnOlderCouple(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should raise request truco when you have two shackles") // Pedir truco quando tiver duas manilhas na mão na primeira rodada com uma diferença de score maior que 3
        public void ShouldRaiseRequestTrucoWhenYouHaveTwoShackles(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        // Second Hand
    }

    @Nested
    class chooseCard {
        // First Hand (Estevan)

        //Adversario primeiro
        //Se consigo matar a primeira carta do adversario, matar com a mais fraca possivel
        @Test
        @DisplayName("Should play the weakest card if can win opponent")
        public void ShouldPlayTheWeakestCardIfCanWinOpponentInFirstHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES) //oponent card
                    );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
        }

        //Se nao conseguir matar mas conseguir empatar, entao empata
        @Test
        @DisplayName("Should drew if possible if can't win in the first hand")
        public void ShouldDrewIfPossibleIfCantWinInTheFirstHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES) //oponent card
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
        }



        //Nos primeiro
        //Se tiver

        // Second Hand (Miguel)
        // Se a carta mais fraca das duas mata a segunda carta do adversário jogar a mais fraca
        @Test
        @DisplayName("Should play the weakest card if it kills the opponent's second card")
        public void ShouldPlayTheWeakestCardIfItKillsTheOpponentsCard(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST); // Perdeu a primeira rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES), //bot card
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES), //oponent card
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)); //oponent card


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel)).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        }
        // Se não tiver feito a primeira jogar a carta mais forte caso seja o primeiro a jogar
        // Se fez a primeira e tem o zap segurar para a última (mais chance de pedir um truco)
        @Test
        @DisplayName("Decide that you will ask for tricks in the first round with an older couple")//Se tiver feito a primeira
        public void DecideThatYouWillAskForTricksInTheFirstRoundWithAnOlderCouple(){

        }

        // Third Hand (Estevan)
    }

    @Nested
    class getRaiseResponse {
    }

    @Nested
    @DisplayName("getName Tests")
    class getName {
        @Test
        @DisplayName("Should name of Bot jakareDuMatuBot")
        public void showNameOfJakareDuMatuBot(){
            assertEquals(jakareDuMatuBot.getName(), "JakaréDuMatuBóty");
        }

    }

    @Nested
    @DisplayName("sortedListCards Tests")
    class sortedListCards{
        @Test
        @DisplayName("Should ordering list of cards")
        public void shouldOrderingListOfCards(){
            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
            );
            List<GameIntel.RoundResult> roundResults = List.of();
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, List.of(), vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                    .build();

            assertEquals(jakareDuMatuBot.sortedListCards(intel, vira).get(0).getRank().value(), TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS).getRank().value());
            assertEquals(jakareDuMatuBot.sortedListCards(intel, vira).get(2).getRank().value(), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS).getRank().value());

        }
    }

}