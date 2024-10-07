package com.luigi.ana.batatafritadobarbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class BatataFritaDoBarBotTest {
    private BatataFritaDoBarBot batataFritaDoBarBot;
    private GameIntel.StepBuilder stepBuilder;
    private GameIntel intel;


    //1
    @Test
    @DisplayName("Make sure the bot is the first to play")
    void makeSureTheBotIsTheFirstToPlay() {
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(batataFritaDoBarBot.checkIfIsTheFirstToPlay(intel));
    }

}