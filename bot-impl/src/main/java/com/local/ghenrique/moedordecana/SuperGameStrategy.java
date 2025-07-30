package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface SuperGameStrategy {
    int getRaiseResponse (GameIntel intel);

    boolean decideIfRaises (GameIntel intel);

    CardToPlay chooseCard (GameIntel intel);
}
