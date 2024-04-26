package com.pedrocagiovane;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
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
        CardToPlay cardToPlay = pauladaSecaBot.escolherCarta(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FIVE,SPADES));
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
        CardToPlay cardToPlay = pauladaSecaBot.escolherCarta(stepBuilder.build());
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
        CardToPlay cardToPlay = pauladaSecaBot.escolherCarta(stepBuilder.build());
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
        CardToPlay cardToPlay = pauladaSecaBot.escolherCarta(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(ACE,SPADES));
    }
}
