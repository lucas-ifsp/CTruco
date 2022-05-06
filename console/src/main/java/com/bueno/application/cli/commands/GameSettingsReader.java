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

package com.bueno.application.cli.commands;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.model.CreateDetachedRequest;
import com.google.common.primitives.Ints;

import java.util.Scanner;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class GameSettingsReader implements Command<CreateDetachedRequest>{

    @Override
    public CreateDetachedRequest execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== CLEAN TRUCO - Let's Play! ======");
        System.out.print("Nome do(a) jogador(a) > ");
        final String username = scanner.nextLine();
        final String bot = readBotName();
        return new CreateDetachedRequest(UUID.randomUUID(), username, bot);
    }

    private String readBotName() {
        final var botNames = BotProviders.availableBots();
        Integer botId;
        while (true){
            System.out.println("Oponente(s): ");
            for (int i = 0; i < botNames.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + botNames.get(i));
            }
            System.out.print("Selecione pelo número > ");
            final Scanner scanner = new Scanner(System.in);
            botId = Ints.tryParse(scanner.nextLine());
            if (botId == null || botId < 1 || botId > botNames.size()){
                System.out.println("Invalid input!");
                continue;
            }
            break;
        }
        return botNames.get(botId - 1);
    }

}
