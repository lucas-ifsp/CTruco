package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HandResultTest {

    @Mock
    private Player player;

    @ParameterizedTest(name = "[{index}]: with valid hand value = {0}")
    @DisplayName("Should allow creating hand result with all arguments")
    @ValueSource(ints = {0, 1, 3, 6, 9, 12})
    void shouldAllowCreatingHandResultWithValidArguments(int handValue){
        HandResult handResult = new HandResult(player, handValue);
        assertEquals(player, handResult.getWinner().get());
        assertEquals(handValue, handResult.getPoints());
    }

    @ParameterizedTest(name = "[{index}]: with invalid hand value = {0}")
    @DisplayName("Should allow creating hand result with all arguments")
    @ValueSource(ints = {-1, 2, 15})
    void shouldNotAllowCreatingHandResultWithInvalidHandValues(int handValue){
        assertThrows(GameRuleViolationException.class, () -> new HandResult(null, handValue));
    }

    @Test
    @DisplayName("Should allow creating hand result with null player")
    void shouldAllowCreatingHandResultWithNullPlayer(){
        HandResult handResult = new HandResult(null, 0);
        assertTrue(handResult.getWinner().isEmpty());
        assertEquals(0, handResult.getPoints());
    }

    @Test
    @DisplayName("Should not allow creating hand result with null player")
    void shouldNotAllowCreatingHandResultWithInvalidHandValue(){
        assertThrows(GameRuleViolationException.class, () -> new HandResult(null, 2));
    }
}