package com.kayky.waleska.kwtruco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class kwtrucoTest{
    private kwtruco kwtrucoBot;

    private final TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
    private final List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
    );

    @BeforeEach
    public void setUp(){
        kwtrucoBot = new kwtruco();
    }

    @Test
    @DisplayName("Choose the lowest card when the game is starting")
    void chooseLowestCardWhenTheGameIsStarting(){
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST),List.of(vira),vira, 1)
                .botInfo(botCards,7)
                .opponentScore(2);

        CardToPlay cardToPlay = kwtrucoBot.chooseCard(stepBuilder.build());

        assertEquals(CardRank.FOUR, cardToPlay.content().getRank());
    }
}
