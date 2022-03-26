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

package com.bueno.spi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotServiceManager {

    public static Stream<BotServiceProvider> providers() {
        return ServiceLoader.load(BotServiceProvider.class).stream().map(ServiceLoader.Provider::get);
    }

    public static BotServiceProvider load(String botServiceName){
        final Predicate<BotServiceProvider> hasName = botImpl -> botImpl.getName().equals(botServiceName);
        final Optional<BotServiceProvider> possibleBot = providers().filter(hasName).findAny();
        return possibleBot.orElseThrow(() -> new NoSuchElementException("Service implementation not available: " + botServiceName));
    }

    public static List<String> providersNames(){
        return providers().map(BotServiceProvider::getName).collect(Collectors.toList());
    }
}
