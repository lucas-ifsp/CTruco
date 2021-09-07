package com.bueno.truco.domain.entities.hand;

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
    void shouldAllowCreatingHandResultWithValidArguments(int value){
        HandResult handResult = new HandResult(player, HandScore.of(value));
        assertEquals(player, handResult.getWinner().get());
        assertEquals(HandScore.of(value), handResult.getScore());
    }

    @Test
    @DisplayName("Should not allow creating hand result with partial parameters")
    void shouldNotAllowCreatingHandResultWithPartialParameters() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new HandResult(null, HandScore.of(1))),
                () -> assertThrows(NullPointerException.class, () -> new HandResult(player, null)));
    }
}