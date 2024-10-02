package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CamaleaoTruqueiroTest {

    //maior carta
    @Test
    @DisplayName("Should return the greater rank card")
    void shouldReturnTheGreaterRankCard() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        TrucoCard greaterCard = getGreaterCard(cards);
        assertEquals(greaterCard, TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
    }



    //menor carta

    //estamos ganhando

    //estamos perdendo

    //temos manilha
        // temos 0 manilha
        // temos 1 manilha
        // temos 2 manilha
        // temos 3 manilha

    //temos carta alta
        // temos 0 carta alta
        // temos 1 carta alta
        // temos 2 carta alta
        // temos 3 carta alta

    //temos carta baixa
}
