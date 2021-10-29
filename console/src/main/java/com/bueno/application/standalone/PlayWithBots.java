/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.application.standalone;

import com.bueno.domain.entities.player.dummybot.DummyBot;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.PlayGameWithBotsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.LogManager;

public class PlayWithBots {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LogManager.getLogManager().reset();

        PlayWithBots main = new PlayWithBots();
        final List<Player> results = main.play(1000);

        System.out.print("MineiroBot: " + results.stream().filter(player -> player.equals(new MineiroBot())).count() + " | ");
        System.out.print("DummyBot: " + results.stream().filter(player -> player.equals(new DummyBot())).count());
    }

    public List<Player> play(int times) throws InterruptedException, ExecutionException {
        final int numberOfThreads = Math.max(1, times / 10);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final List<Callable<Player>> games = new ArrayList<>();

        Callable<Player> game = () -> {
            PlayGameWithBotsUseCase uc = new PlayGameWithBotsUseCase(new MineiroBot(), new DummyBot());
            return uc.play();
        };

        for (int i = 0; i < times; i++) {
            games.add(game);
        }

        final List<Player> result = new ArrayList<>();
        for (Future<Player> future : executor.invokeAll(games)) {
            result.add(future.get());
        }

        executor.shutdown();
        return result;
    }
}
