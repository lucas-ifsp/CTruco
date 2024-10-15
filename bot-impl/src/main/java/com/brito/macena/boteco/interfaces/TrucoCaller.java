package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public interface TrucoCaller {
    boolean shouldCallTruco(GameIntel intel, Status status);
}
