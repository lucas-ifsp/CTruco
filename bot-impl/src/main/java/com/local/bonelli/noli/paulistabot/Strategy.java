/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

<<<<<<<< HEAD:bot-impl/src/main/java/com/brito/macena/boteco/interfaces/TrucoCaller.java
package com.brito.macena.boteco.interfaces;
========
package com.local.bonelli.noli.paulistabot;
>>>>>>>> 21432f8a (feat: creating remote bot caller):bot-impl/src/main/java/com/local/bonelli/noli/paulistabot/Strategy.java

import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public interface TrucoCaller {
    boolean shouldCallTruco(GameIntel intel, Status status);
}
