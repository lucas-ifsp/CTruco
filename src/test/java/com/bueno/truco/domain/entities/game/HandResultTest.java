package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HandResultTest {

    @Mock
    private Player player;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 6, 9, 12})
    void shouldCorrectlyConstructHandResultWithAllArguments(int handValue){
        HandResult handResult = new HandResult(player, handValue);
        Assertions.assertEquals(player, handResult.getWinner().get());
        Assertions.assertEquals(handValue, handResult.getPoints());
    }

    @Test
    void shouldCorrectlyConstructHandResultWithNullPlayer(){
        HandResult handResult = new HandResult(null, 0);
        Assertions.assertTrue(handResult.getWinner().isEmpty());
        Assertions.assertEquals(0, handResult.getPoints());
    }

    @Test
    void shouldThrowConstructingWithInvalidHandValue(){
        Assertions.assertThrows(GameRuleViolationException.class, () -> new HandResult(null, 2));
    }
}