package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VeioDoBarBotTest {
    private VeioDoBarBot sut;

    @BeforeEach
    void setUp() {
        sut = new VeioDoBarBot();
    }

    @Test
    @DisplayName("Should refuse points raising if all cards are lower than jacks and no manilhas")
    void shouldRefusePointsRaisingIfAllCardsAreLowerThanJacksAndNoManilhas(){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept points raising if all cards are upper than jacks and no manilhas")
    void shouldAcceptPointsRaisingIfAllCardsAreUpperThanJacksAndNoManilhas() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }

}