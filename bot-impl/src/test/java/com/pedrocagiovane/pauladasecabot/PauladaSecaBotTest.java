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
    @DisplayName("Aumenta aposta se tiver copas e ganhou a primeira")
    void aumentaApostaSeTiverCopasEGanharPrimeira() {
        maoPlayer = List.of(TrucoCard.of(JACK,CLUBS), TrucoCard.of(THREE, HEARTS), TrucoCard.of(TWO, CLUBS));
        vira = TrucoCard.of(TWO, HEARTS);
        roundResult = List.of(GameIntel.RoundResult.WON);
        cartas = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 3).botInfo(maoPlayer, 1).opponentScore(0);
        int resultado = pauladaSecaBot.getRaiseResponse(stepBuilder.build());
        assertThat(resultado).isOne();
    }
    @Test
    @DisplayName("Aumenta aposta se tiver zap e ganhou a primeira")
    void aumentarApostaSeTiverZapEGanharPrimeira() {
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
        maoPlayer = List.of( TrucoCard.of(FIVE, SPADES),TrucoCard.of(ACE, HEARTS), TrucoCard.of(SEVEN, HEARTS));
        vira = TrucoCard.of(KING, SPADES);
        roundResult = List.of();
        cartas = List.of();
        TrucoCard maoOponente = TrucoCard.of(FOUR, SPADES);
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cartas, vira, 1).botInfo(maoPlayer, 3).opponentScore(0).opponentCard(maoOponente);
        CardToPlay cardToPlay = pauladaSecaBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FIVE, SPADES));
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

}
