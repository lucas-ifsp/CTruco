package com.lucasmurilo.akkosocorrompido;


import static com.bueno.spi.model.CardRank.THREE;
import static com.bueno.spi.model.CardSuit.HEARTS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.lucasmurilo.m.lazarinipodenciano.Akkosocorrompido;

public class AkkosocorrompidoTest {
    private Akkosocorrompido bot;
    @Mock
    GameIntel intel;

    @BeforeEach
    public void setup() {
        bot = new Akkosocorrompido();
    }

    //truca ao invez disso
    @Test
    @DisplayName("Should start with second best card if has high card and manilha")
    public void shouldReturnsSecondBestCardIfHaveManilhaAndHigh() {        
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS); 

        List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
            );

        List<TrucoCard> openCards = List.of(vira);

        GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), openCards, vira, 1)
            .botInfo(botCards, 5)
            .opponentScore(3)
            .build();

        assertEquals(TrucoCard.of(THREE, HEARTS), bot.chooseCard(intel).value().toString());
    }

    //truca depois dessa, ou agora tbm
    @Test
    @DisplayName("should start with best card if no manilha in hand")
    public void shouldStartWithBestCardifNotManilha (){
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES); 

        List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
            TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );
        List<TrucoCard> openCards = List.of(vira);

        GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .build();

        assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), bot.chooseCard(intel).value().toString());
    }

    @Test
    @DisplayName("should return worst card that CAN beat opponent")
    public void shouldReturnWorstCardThatCanBeatOpponent (){
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS); 

        List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        List<TrucoCard> openCards = List.of(vira);

        GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), openCards, vira, 1)
            .botInfo(botCards, 2)
            .opponentScore(3)
            .opponentCard(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS))
            .build();

        assertEquals(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), bot.chooseCard(intel).value().toString());
    }

    @Test
    @DisplayName("should return worst card on hand if can NOT beat opponent")
    public void shouldReturnWorstCardOnHandIfCantBeatOpponent (){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS); 

        List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );
        List<TrucoCard> openCards = List.of(vira);

        GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), openCards, vira, 1)
            .botInfo(botCards, 3)
            .opponentScore(3)
            .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
            .build();

        assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), bot.chooseCard(intel).value().toString());
    }
    
    
}