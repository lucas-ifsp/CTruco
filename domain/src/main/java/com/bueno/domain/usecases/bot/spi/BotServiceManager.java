/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

package com.bueno.domain.usecases.bot.spi;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BotServiceManager {

    public static BotService load(String botServiceName){
        final Predicate<BotService> hasName = botService -> botService.getName().equals(botServiceName);
        final Optional<BotService> possibleBot = providers().filter(hasName).findAny();
        if(possibleBot.isPresent()) return possibleBot.get();
        throw new NoSuchElementException("BotService not available: " + botServiceName);
    }

    public static Stream<BotService> providers() {
        return ServiceLoader.load(BotService.class).stream().map(ServiceLoader.Provider::get);
    }
}
