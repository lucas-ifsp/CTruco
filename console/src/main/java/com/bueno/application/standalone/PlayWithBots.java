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
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.LogManager;

//TODO Colocar Logs estratégicos para companhar o novo andamento do processo.
public class PlayWithBots {

    private static final UUID uuid1 = UUID.randomUUID();
    private static final UUID uuid2 = UUID.randomUUID();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LogManager.getLogManager().reset();

        PlayWithBots main = new PlayWithBots();
        final List<Player> results = main.play(1000);

        System.out.print("MineiroBot1: " + results.stream().filter(player -> player.getUuid().equals(uuid1)).count() + " | ");
        System.out.print("MineiroBot2: " + results.stream().filter(player -> player.getUuid().equals(uuid2)).count());
    }

    /*public static void main(String[] args) {
        final InMemoryGameRepository repo = new InMemoryGameRepository();
        PlayGameWithBotsUseCase uc = new PlayGameWithBotsUseCase(repo);
        final Player player = uc.playWithBots(new MineiroBot(repo), new MineiroBot(repo));
        System.out.println("Winner: " + player);
    }
*/
    public List<Player> play(int times) throws InterruptedException, ExecutionException {
        final int numberOfThreads = Math.max(1, times / 10);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final List<Callable<Player>> games = new ArrayList<>();

        Callable<Player> game = () -> {
            final InMemoryGameRepository repo = new InMemoryGameRepository();
            PlayGameWithBotsUseCase uc = new PlayGameWithBotsUseCase(repo);
            return uc.playWithBots(new MineiroBot(repo, uuid1), new MineiroBot(repo, uuid2));
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
