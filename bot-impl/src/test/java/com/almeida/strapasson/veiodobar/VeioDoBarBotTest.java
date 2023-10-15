package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class VeioDoBarBotTest {
    @Mock
    GameIntel gameIntel;

    @InjectMocks
    FirstRound firstRound;

    @Nested
    class FirstRoundTest{
        @Nested
        @DisplayName("Strategy to one manilha")
        class OneManilha{
            @Test
            @DisplayName("Play Manilha First")
            void playManilhaFirst() {
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
                when(gameIntel.getCards()).thenReturn(
                        List.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)),
                        List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)),
                        List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)));
            }

        }

    }

    @Nested
    class SecondRoundTest{

    }

    @Nested
    class  ThirdRoundTest{

    }


}