package com.brito.macena.boteco.intel.trucoCaller;

import com.brito.macena.boteco.interfaces.TrucoCaller;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public class PassiveTrucoCaller implements TrucoCaller {

    @Override
    public boolean shouldCallTruco(GameIntel intel, Status status) {
        return false;
    }
}
