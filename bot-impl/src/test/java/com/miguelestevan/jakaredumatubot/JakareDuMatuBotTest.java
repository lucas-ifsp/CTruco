package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
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
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
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
        //Se tiver feito a primeira e tiver uma manilha
        @Test
        @DisplayName("Should raise request truco when have manilha on OnSecondHandAndWinsFirstOne") // Pedir truco quando tiver duas manilhas na mão na primeira rodada com uma diferença de score maior que 3
        public void ShouldRaiseRequestTrucoWhenHaveManilhaOnSecondHandAndWinsFirstOne(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);
            List<TrucoCard> openCards = List.of(
                    vira,
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        //Se tiver feito a primeira e tiver uma ou duas carta forte
        @Test
        @DisplayName("Should raise request truco when have OneOrMoreGoodCard on OnSecondHandAndWinsFirstOne") // Pedir truco quando tiver duas manilhas na mão na primeira rodada com uma diferença de score maior que 3
        public void ShouldRaiseRequestTrucoWhenHaveOneOrMoreGoodCardOnSecondHandAndWinsFirstOne(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);
            List<TrucoCard> openCards = List.of(
                    vira,
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        //Se tiver empatado tiver duas cartas fortes ou uma manilha

        // Third Hand
        @Test
        @DisplayName("Should raise request truco when have manilha on third hand") // Pedir truco quando tiver duas manilhas na mão na primeira rodada com uma diferença de score maior que 3
        public void ShouldRaiseRequestTrucoWhenHaveManilhaOnThirdHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
            List<TrucoCard> openCards = List.of(
                    vira,
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should raise request truco when have one good card and score difference higher than 3 points on third hand") // Pedir truco quando tiver duas manilhas na mão na primeira rodada com uma diferença de score maior que 3
        public void ShouldRaiseRequestTrucoWhenHaveOneGoodCardAndScoreDifferenceHigherThanThreePointsOnThirdHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
            List<TrucoCard> openCards = List.of(
                    vira,
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 1)
                    .opponentScore(7)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }
    }

    @Nested
    class chooseCard {
        //                                          First Hand

        //                                      Adversario primeiro
        //Se consigo matar a primeira carta do adversario, matar com a mais fraca possivel
        @Test
        @DisplayName("Should play the more weakest card possible if can win opponent first card in first hand")
        public void ShouldPlayTheMoreWeakestCardPossibleIfCanWinOpponentFirstCardInFirstHand(){
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
        @DisplayName("Should drew if possible if can't win opponent first card in the first hand")
        public void ShouldDrewIfPossibleIfCantWinOpponentFirstCardInTheFirstHand(){
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

        //Se nao conseguir ganhar nem empatar
        @Test
        @DisplayName("Should choose the weakest card if impossible drew or win the first opponent card")
        public void ShouldChooseTheWeakestCardIfImpossibleDrewOrWinTheFirstOpponentCard(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES) //oponent card
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
        }

        //                                       Nos primeiro
        //Jogar a maior carta na mão
        @Test
        @DisplayName("Should play the strongest card if play first in first hand")
        public void ShouldPlayTheStrongestCardIfPlayFirstInFirstHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(
                    vira
            );

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
        }

        //Se tiver zap e um tres ou dois, guardar o zap pra dar na testa do caquinho
        @Test
        @DisplayName("Should play the second strongest card if play first and have zap in first hand")
        public void ShouldPlayTheSecondStrongestCardIfPlayFirstAndHaveZapInFirstHand() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
        }

       //---------------------------------------------------------------------------------------------------------------


        //                                        Second Hand
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

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        }

        // Se fez a primeira e tem uma manilha para jogar e está trucado jogar a manilha para fechar
        @Test
        @DisplayName("Should Play The Manilha If Was Raises")
        public void ShouldPlayTheManilhaIfWasRaises(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON); // Perdeu a primeira rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES), //bot card
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES) //oponent card
                    );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 3)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
        }

        // Se fez a primeira e tem uma manilha para jogar mas não está trucado jogar a outra carta
        @Test
        @DisplayName("Should Play The Weakness If Was Not Raises")
        public void ShouldPlayTheWeaknessIfWasNotRaises(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON); // Ganhou a primeira rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES), //bot card
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES) //oponent card
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        }

        // Se fez a primeira e não te uma manilha jogar a carta mais forte

        // Se fez a primeira e tem o zap segurar para a última (mais chance de pedir um truco)
        @Test
        @DisplayName("Decide ")//Se tiver feito a primeira
        public void DecideThatYouWillAskForTricksInTheFirstRoundWithAnOlderCouple(){

        }

        //--------------------------------------------------------------------------------------------------------------

        //                                        Third Hand
        @Test
        @DisplayName("Should play the last card in third hand")
        public void ShouldPlayTheLastCardInThirdHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON); // Ganhou a segunda rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES), //first bot card
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES), //second bot card
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), //first opponent card
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS) //second opponent card
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        }
    }

    @Nested
    class getRaiseResponse {
        //                                   First Hand

        // Pediu truco para sair Se tiver 1 manilha e uma outra carta boa aceita (a,2,3)
        @Test
        @DisplayName("ShouldAcceptRaisesIfWasOneManilhaAndAnotherGoodHand")
        public void ShouldAcceptRaisesIfWasOneManilhaAndAnotherGoodHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(); // Ganhou a primeira rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertEquals(jakareDuMatuBot.getRaiseResponse(intel), 1);
        }
        // Pediu truco para sair Aceita se tiver 2 cartas boas (a,2,3)
        @Test
        @DisplayName("ShouldAcceptRaisesIfWasTwoGoodHand")
        public void ShouldAcceptRaisesIfWasTwoGoodHand(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(); // Ganhou a primeira rodada
            List<TrucoCard> openCards = Arrays.asList(
                    vira
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertEquals(jakareDuMatuBot.getRaiseResponse(intel), 0);
        }

        // Pediu truco na carta do bote, só aumenta se tiver duas manilhas ou a diferença de pontos for maior que 5 e tiver apenas uma carta boa
        @Test
        @DisplayName("Should Re Raises After Bot Plays If Was TwoManilhasOrDifferenceWasMoreThanFivePoints")
        public void ShouldReRaisesAfterBotPlaysIfWasTwoManilhasOrDifferenceWasMoreThanFivePoints(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS) //bot card
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 7)
                    .opponentScore(1)
                    .build();

            assertEquals(1, jakareDuMatuBot.getRaiseResponse(intel));
        }

        //                                  Second Hand
        @Test
        @DisplayName("Should accept raises if have one manilha")
        public void ShouldAcceptRaisesIfHaveOneManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );


            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 7)
                    .opponentScore(1)
                    .build();

            assertEquals(0, jakareDuMatuBot.getRaiseResponse(intel));
        }

        //                                   Third Hand
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

    @Nested
    @DisplayName("hasCardHigherThan")
    class hasCardHigherThan{

        @Test
        @DisplayName("Should returns if some card of list can win")
        public void ShouldReturnsIfHasCardHighterThanSomeCard(){
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

            assertEquals(jakareDuMatuBot.hasCardHigherThan(intel, intel.getOpponentCard().get()), true);
        }
    }

    @Nested
    @DisplayName("hasGoodCards") // Good cards are a,2,3 not been a manilha
    class hasGoodCards{
        @Test
        @DisplayName("Should returns true if has a good card and not been a manilha")
        public void ShouldReturnsTrueIfHasAGoodCardNotBeenAManilha(){
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
            );
            assertEquals(jakareDuMatuBot.hasGoodCards(botCards, TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)).size(), 1);

            List<TrucoCard> botCards2 = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );
            assertEquals(jakareDuMatuBot.hasGoodCards(botCards2, TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)).size(), 2);

        }
    }

}