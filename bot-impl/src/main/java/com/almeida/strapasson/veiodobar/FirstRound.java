package com.almeida.strapasson.veiodobar;


import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;

public class FirstRound implements Strategy {
    @Override
    public boolean hasBiggerCouple(GameIntel intel) {return false;}

    @Override
    public boolean hasMinorCouple(GameIntel intel) {
        return false;
    }

    @Override
    public boolean hasGoodHand(GameIntel intel) {
        return false;
    }

    @Override
    public boolean hasMediumHand(GameIntel intel) {
        return false;
    }

    @Override
    public boolean hasBadHand(GameIntel intel) {
        return false;
    }
}
