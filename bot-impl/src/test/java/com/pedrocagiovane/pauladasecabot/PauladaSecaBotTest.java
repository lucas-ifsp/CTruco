package com.pedrocagiovane.pauladasecabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.pedrocagiovane.pauladasecabot.PauladaSecaBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardSuit.HEARTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PauladaSecaBotTest {
    PauladaSecaBot pauladaSecaBot;
    private GameIntel.StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> roundResult;
    private List<TrucoCard> cartas;
    private TrucoCard vira;
    private List<TrucoCard> maoPlayer;
    @BeforeEach
    void setUp(){ pauladaSecaBot = new PauladaSecaBot();}

    @Test
    @DisplayName("Jogar menor carta na primeira rodada caso tenha casal maior")
    void jogarMenorCartaPrimeiraSeTiverCasalMaior() {
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(ACE, CLUBS), TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FIVE,SPADES));
    }
    @Test
    @DisplayName("Jogar menor carta na primeira rodada caso tenha casal menor")
    void jogarMenorCartaPrimeiraSeTiverCasalMenor() {
        maoPlayer = List.of( TrucoCard.of(TWO, DIAMONDS),TrucoCard.of(ACE, SPADES), TrucoCard.of(ACE, DIAMONDS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO,DIAMONDS));
    }
    @Test
    @DisplayName("Jogar menor carta na primeira rodada caso tenha casal preto")
    void jogarMenorCartaPrimeiraSeTiverCasalPreto() {
        maoPlayer = List.of( TrucoCard.of(THREE, SPADES),TrucoCard.of(ACE, CLUBS), TrucoCard.of(ACE, SPADES));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,SPADES));
    }

    @Test
    @DisplayName("Jogar menor carta na primeira rodada caso tenha casal vermelho")
    void jogarMenorCartaPrimeiraSeTiverCasalVermelho() {
        maoPlayer = List.of( TrucoCard.of(THREE, SPADES),TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,SPADES));
    }

    @Test
    @DisplayName("Pede truco na segunda se tiver casal maior")
    void trucoSegundaSeTiverCasalMaior() {
        maoPlayer = List.of(TrucoCard.of(FIVE,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(KING, HEARTS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Aceita aposta se tiver copas e ganhou a primeira")
    void aceitaApostaSeTiverCopasEGanharPrimeira() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(THREE, HEARTS), TrucoCard.of(TWO, CLUBS));
        vira = TrucoCard.of(TWO, HEARTS);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }
    @Test
    @DisplayName("Aceita aposta se tiver zap e ganhou a primeira")
    void aceitaApostaSeTiverZapEGanharPrimeira() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(ACE, HEARTS), TrucoCard.of(THREE, CLUBS));
        vira = TrucoCard.of(TWO, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }
    @Test
    @DisplayName("Truca na primeira se tiver casal menor")
    void trucaNaPrimeiraComCasalMenor() {
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(ACE, SPADES));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        Boolean resultado = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(resultado);
    }

    @Test
    @DisplayName("Se tiver zap ou copas tenta amarrar a primeira")
    void amarrarPrimeiraSeTiverZapOuCopas() {
        maoPlayer = List.of( TrucoCard.of(SIX, SPADES),TrucoCard.of(TWO, SPADES), TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        TrucoCard opponentCard = TrucoCard.of(TWO, CLUBS);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0).opponentCard(opponentCard);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));
    }


    @Test
    @DisplayName("Jogar melhor carta se não tiver manilha")
    void jogarMelhorCartaPrimeiraSeNaoTiverManilha() {
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(THREE, CLUBS), TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(SEVEN, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, CLUBS));
    }

    @Test
    @DisplayName("Jogar ouros ou espadas na primeira se tiver")
    void jogarOurosOuEspadaNaPrimeira() {
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(ACE, SPADES), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(ACE,SPADES));
    }

    @Test
    @DisplayName("Matar a carta do pato com a menor carta nossa")
    void matarComMaisFraca(){
        maoPlayer = List.of( TrucoCard.of(KING, HEARTS),TrucoCard.of(ACE, HEARTS), TrucoCard.of(JACK, HEARTS));
        vira = TrucoCard.of(SEVEN, SPADES);
        roundResult = List.of();
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(FOUR, SPADES);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0).opponentCard(maoOponente);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(JACK, HEARTS));
    }

    @Test
    @DisplayName("Matar a manilha do pato")
    void matarManilha(){
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(ACE, HEARTS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(ACE, SPADES);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0).opponentCard(maoOponente);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(ACE, HEARTS));
    }

    @Test
    @DisplayName("Pede truco na segunda se tiver ganhado a primeira e se tiver manilha")
    void trucoSegundaSeGanharPrimeiraETiverManilha() {
        maoPlayer = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, DIAMONDS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    //REVISAR
    @Test
    @DisplayName("Pede truco na terceira se tiver dois ou três")
    void trucoTerceiraSeTiverTresOuDois() {
        maoPlayer = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, DIAMONDS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Pede truco na terceira se fez a primeira e consegue amarrar a terceira")
    void trucoTerceiraSeTiverManilha() {
        maoPlayer = List.of(TrucoCard.of(ACE,HEARTS), TrucoCard.of(JACK, DIAMONDS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(ACE, SPADES);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0).opponentCard(maoOponente);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("BLEFA NO MARRECO: Pede truco na terceira se fez a carta do oponente é um valete ou menor")
    void trucoTerceiraSeOponentCardForRuim() {
        maoPlayer = List.of(TrucoCard.of(QUEEN,HEARTS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(JACK, DIAMONDS);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0).opponentCard(maoOponente);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("trucar na terceira se a carta for maior e não for manilha e tiver ganho a primeira")
    void trucoTerceiraSeCartaMaiorQueOponente() {
        maoPlayer = List.of(TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(FIVE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(KING, DIAMONDS);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0).opponentCard(maoOponente);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("trucar na terceira se a carta for maior e for manilha")
    void trucoTerceiraSeCartaMaiorQueManilhaOponente() {
        maoPlayer = List.of(TrucoCard.of(QUEEN,HEARTS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(ACE, CLUBS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON);
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(ACE, DIAMONDS);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0).opponentCard(maoOponente);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Aceitamo o truco se tem mais de uma manilha")
    void aceitaSeTemMaisDeUmaManilha() {
        maoPlayer = List.of(TrucoCard.of(FIVE,SPADES),TrucoCard.of(FIVE,HEARTS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(FOUR, DIAMONDS);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        int result = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Aceitamo o truco se tem uma manilha e tres")
    void aceitaSeTemUmaManilhaETres() {
        maoPlayer = List.of(TrucoCard.of(FIVE,SPADES),TrucoCard.of(THREE,HEARTS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(FOUR, DIAMONDS);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        int result = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(result).isZero();
    }


    @Test
    @DisplayName("Aceitamo o truco se tem dois tres")
    void aceitaSeTemDoisTres() {
        maoPlayer = List.of(TrucoCard.of(THREE,SPADES),TrucoCard.of(TWO,HEARTS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(FOUR, DIAMONDS);
        roundResult = List.of();
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0);
        int result = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Aceitamo o truco se tem tres na terceira e ganhamos a primeira")
    void aceitaSeTemTresNaTerceira() {
        maoPlayer = List.of(TrucoCard.of(THREE,CLUBS), TrucoCard.of(ACE, HEARTS), TrucoCard.of(SEVEN, CLUBS));
        vira = TrucoCard.of(FOUR, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isZero();
    }

    @Test
    @DisplayName("trucar se tiver feito a primeira e tem tres pra segunda")
    void trucoSegundaSeFezPriemriaETemTres() {
        maoPlayer = List.of(TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(THREE, CLUBS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Aceitamo o truco se tem tres na segunda e ganhamos a primeira")
    void aceitaTrucoSeTiverTresSegundaGanhouPrimeira() {
        maoPlayer = List.of(TrucoCard.of(THREE,CLUBS), TrucoCard.of(SEVEN, CLUBS));
        vira = TrucoCard.of(FOUR, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isZero();
    }

    @Test
    @DisplayName("Aceita truco se casal vermelho e perdeu a primeira")
    void aceitarApostaSeTiverCasalVermelho() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, DIAMONDS));
        vira = TrucoCard.of(ACE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }
    @Test
    @DisplayName("Aceita truco se casal preto e perdeu a primeira")
    void aceitarApostaSeTiverCasalPreto() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(TWO, CLUBS), TrucoCard.of(TWO, SPADES));
        vira = TrucoCard.of(ACE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }

    @Test
    @DisplayName("Aceita truco se casal maior e perdeu a primeira")
    void aceitarApostaSeTiverCasalMaior() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(TWO, CLUBS), TrucoCard.of(TWO, HEARTS));
        vira = TrucoCard.of(ACE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }

    @Test
    @DisplayName("Aceita truco se casal menor e perdeu a primeira")
    void aceitarApostaSeTiverCasalMenor() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, DIAMONDS));
        vira = TrucoCard.of(ACE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }

    @Test
    @DisplayName("Aceita truco se tem dois 2 e ganhou a primeira")
    void aceitarApostaSeTiverDoisDois() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, DIAMONDS));
        vira = TrucoCard.of(THREE, SPADES);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }

    @Test
    @DisplayName("Pede truco na segunda se tiver casal menor")
    void trucoSegundaSeTiverCasalMenor() {
        maoPlayer = List.of(TrucoCard.of(FIVE,HEARTS), TrucoCard.of(KING, SPADES), TrucoCard.of(KING, DIAMONDS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }
    @Test
    @DisplayName("Pede truco na segunda se tiver casal preto")
    void trucoSegundaSeTiverCasalPreto() {
        maoPlayer = List.of(TrucoCard.of(FIVE,HEARTS), TrucoCard.of(KING, SPADES), TrucoCard.of(KING, CLUBS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Pede truco na segunda se tiver casal vermelho")
    void trucoSegundaSeTiverCasalVermelho() {
        maoPlayer = List.of(TrucoCard.of(FIVE,HEARTS), TrucoCard.of(KING, HEARTS), TrucoCard.of(KING, DIAMONDS));
        vira = TrucoCard.of(JACK, SPADES);
        roundResult = List.of(GameIntel.RoundResult.LOST);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 1).opponentScore(0);
        boolean result = pauladaSecaBot.decideIfRaises(stepBuilder.build());
        assertThat(result).isTrue();
    }
}
