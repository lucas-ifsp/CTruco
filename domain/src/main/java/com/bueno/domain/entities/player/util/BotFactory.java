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

package com.bueno.domain.entities.player.util;

import com.bueno.domain.entities.player.dummybot.DummyBot;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.usecases.game.GameRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BotFactory {

    private static final Map<UUID, Bot> bots = new HashMap<>();

    public static Bot create(UUID uuid, String botName, GameRepository repo){
        final Bot bot = switch (botName) {
            case "MineiroBot" -> new MineiroBot(repo, uuid);
            case "DummyBot" -> new DummyBot(repo, uuid);
            default -> throw new IllegalArgumentException("Bot not found!");
        };
        return register(bot);
    }

    public static Bot create(String botName, GameRepository repo){
        return create(UUID.randomUUID(), botName, repo);
    }

    private static Bot register(Bot bot) {
        if(!bots.containsKey(bot.getUuid()))
            bots.put(bot.getUuid(), bot);
        return bot;
    }

    public static Bot getBot(UUID uuid){
        return bots.get(uuid);
    }
}
