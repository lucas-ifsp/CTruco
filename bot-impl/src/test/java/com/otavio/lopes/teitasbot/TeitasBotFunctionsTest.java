package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.otavio.lopes.teitasbot.TeitasBotFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.List;

import static com.silvabrufato.impl.silvabrufatobot.SilvaBrufatoBotTest.gameIntel;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TeitasBotFunctionsTest {
    private static GameIntel gameIntel;
    @BeforeEach
    public void setGameIntel() {
        gameIntel = mock(GameIntel.class);
    }

    @Nested
    @DisplayName("HasManilhaTest")
    class HasManilhaTest {
        @Test
        @DisplayName("Should return true if hand at least one manilha")
        void shouldReturnTrueIfHandHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no manilha")
        void shouldReturnFalseIfHandHasNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("TypesOfHand")
    class TypesOfHands{
        @Test
        @DisplayName("Should return true if we had a nuts hand")
        void shouldReturnTrueIfHandIsNuts() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );
            assertThat(TeitasBotFunctions.hasNutsHand(cards,vira)).isTrue();

        }


        @Test
        @DisplayName("Should return true if we had a good hand")
        void shouldReturnTrueIfHandIsGood() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
            assertThat(TeitasBotFunctions.hasGoodHand(cards,vira)).isTrue();

        }
    }



    @Test
    @DisplayName("Should return true if we had a trash hand")
    void shouldReturnTrueIfHandIsTrash() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
        );
        assertThat(TeitasBotFunctions.hasTrashHand(cards,vira)).isTrue();

    }
    @Test
    @DisplayName("Should return True if we ha Strong Hand --  lowest than nuts and better than trash")
    void shouldReturnTrueIfHandIsStrong() {
        TrucoCard vira =  TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        assertThat(TeitasBotFunctions.hasStrongHand(cards,vira)).isTrue();

    }

}
@Nested
class WeStart{
    //game intel error at 'Not found'
    @Test
    @DisplayName("should return true if we last round")
    void shouldReturTrueIfWeStart(){
        assertThat(TeitasBotFunctions.isOpponentThatStartTheRound(gameIntel)).isTrue()    ;
    }
}



@Nested
class PlayModes{
    @Test
    @DisplayName("Should Plaay at agressive mode if")
    void ShouldPlayAgressiveMode(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)

        );
        assertThat(TeitasBotFunctions.PlayAgressiveMode(cards,vira)).isTrue();

    }
}


}