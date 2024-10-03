package com.manhani.stefane.reimubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReimuBotTest {

    private ReimuBot reimuBot;
    
    @BeforeEach
    void setUp(){ reimuBot = new ReimuBot(); }
 
    @Test
    @DisplayName("Should select weakest card if cannot defeat opponent")
    void selectWeakestIfLose(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        List<TrucoCard> reimuCards = List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
        );
        var step = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                .opponentScore(0).opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
                .build();
        var selectedCard = reimuBot.chooseCard(step).content();
        assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
    }

    @Test
    @DisplayName("Should raise if on round 2 and has two manilhas")
    void raiseIfTwoManilhas(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        List<TrucoCard> reimuCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
        );
        var step = GameIntel.StepBuilder.with()
                .gameInfo(
                        List.of(GameIntel.RoundResult.WON),
                        List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)),
                        vira, 1).botInfo(reimuCards, 0)
                .opponentScore(1).opponentCard(TrucoCard.of(CardRank.KING, CardSuit.CLUBS))
                .build();
        assertThat(reimuBot.decideIfRaises(step)).isTrue();
    }

}