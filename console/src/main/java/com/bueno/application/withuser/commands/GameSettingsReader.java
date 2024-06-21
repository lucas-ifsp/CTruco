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

package com.bueno.application.withuser.commands;

import com.bueno.application.utils.Command;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.CreateDetachedDto;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.bueno.persistence.repositories.UserRepositoryImpl;
import com.google.common.primitives.Ints;
import com.remote.RemoteBotApiAdapter;

import java.util.Scanner;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class GameSettingsReader implements Command<CreateDetachedDto> {

    @Override
    public CreateDetachedDto execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== CLEAN TRUCO - Let's Play! ======");
        System.out.print("Nome do(a) jogador(a) > ");
        final String username = scanner.nextLine();
        final String bot = readBotName();
        return new CreateDetachedDto(UUID.randomUUID(), username, bot);
    }

    private String readBotName() {
        final RemoteBotRepository repository = new RemoteBotRepositoryImpl();
        final RemoteBotApiAdapter botApi = new RemoteBotApiAdapter();
        final BotManagerService botManagerService = new BotManagerService(repository, botApi);
        final var botNames = botManagerService.providersNames();
        Integer botId;
        while (true) {
            System.out.println("Oponente(s): ");
            for (int i = 0; i < botNames.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + botNames.get(i));
            }
            System.out.print("Selecione pelo número > ");
            final Scanner scanner = new Scanner(System.in);
            botId = Ints.tryParse(scanner.nextLine());
            if (botId == null || botId < 1 || botId > botNames.size()) {
                System.out.println("Invalid input!");
                continue;
            }
            break;
        }
        return botNames.get(botId - 1);
    }

}
