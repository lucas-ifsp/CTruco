package com.brito.macena.boteco.intel.trucoCaller;

import com.brito.macena.boteco.interfaces.TrucoCaller;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public class AggressiveTrucoCaller implements TrucoCaller {

    @Override
    public boolean shouldCallTruco(GameIntel intel, Status status) {
        if (status == Status.EXCELLENT || status == Status.GOOD) {
            return true;
        }
        return false;
    }
}
