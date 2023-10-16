package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.GameIntel;

public interface Strategy {
    boolean hasBiggerCouple(GameIntel intel);
    boolean hasMinorCouple(GameIntel intel);
    boolean hasGoodHand(GameIntel intel);
    boolean hasMediumHand(GameIntel intel);
    boolean hasBadHand(GameIntel intel);


}
