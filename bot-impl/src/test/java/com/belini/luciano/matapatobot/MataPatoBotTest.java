package com.belini.luciano.matapatobot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MataPatoBotTest {

    MataPatoBot mataPatoBot;
    private GameIntel.StepBuilder stepBuilder;
    @Test
    @DisplayName("If opponent plays first return true")
    void shouldReturnTrueForOpponentPlayFirst() {
        mataPatoBot = new MataPatoBot();
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        boolean opponentPlay = true;
        assertThat(mataPatoBot.checkFirstPlay(Optional.ofNullable(opponentCard)).equals(opponentPlay));
    }

    @Test
    @DisplayName("Should return false if we play firt")
    void shouldReturnFalseIfWePlayFirts(){
        mataPatoBot = new MataPatoBot();
        boolean opponentPlay = false;
        assertThat(mataPatoBot.checkFirstPlay(Optional.empty()).equals(opponentPlay));
    }

}