package com.brito.macena.boteco.intel.trucoResponder;

import com.brito.macena.boteco.utils.Status;
import com.brito.macena.boteco.interfaces.TrucoResponder;
import com.bueno.spi.model.GameIntel;

public class AggressiveTrucoResponder implements TrucoResponder {

    @Override
    public int getRaiseResponse(GameIntel intel, Status status) {
        return 0;
    }
}