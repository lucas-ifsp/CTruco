package com.almeida.strapasson.veiodobar;


import com.bueno.spi.model.GameIntel;

public class FirstRound implements Strategy {
    @Override
    public boolean hasMajorityCouple(GameIntel intel) {return false;}

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
