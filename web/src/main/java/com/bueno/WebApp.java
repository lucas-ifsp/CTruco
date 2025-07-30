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

package com.bueno;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.TransientRemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.GameResultDto;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.user.RegisterUserUseCase;
import com.bueno.domain.usecases.user.dtos.RegisterUserRequestDto;
import com.bueno.persistence.DataBaseBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class WebApp {
    public static void main(String[] args) throws SQLException {
        DataBaseBuilder dataBaseBuilder = new DataBaseBuilder();
        dataBaseBuilder.buildDataBaseIfMissing();
        SpringApplication.run(WebApp.class, args);
    }

    @Bean
    CommandLineRunner run(RegisterUserUseCase registerUserUseCase,
                          GameResultRepository gameResultRepository,
                          PasswordEncoder encoder,
                          RemoteBotRepository botRepository,
                          TournamentRepository tournamentRepository, MatchRepository matchRepository) {
        return args -> {
            tournamentRepository.deleteAll();
            matchRepository.deleteAll();
            final String encodedPassword = encoder.encode("123123");
            final RegisterUserRequestDto defaultUser = new RegisterUserRequestDto("Lucas", encodedPassword, "lucas.ruas@gmail.com");
            final RegisterUserRequestDto user1 = new RegisterUserRequestDto("User 1", encodedPassword, "user1@gmail.com");
            final RegisterUserRequestDto user2 = new RegisterUserRequestDto("User 2", encodedPassword, "user2@gmail.com");
            final UUID defaultUuid = registerUserUseCase.create(defaultUser).uuid();
            final UUID user1Uuid = registerUserUseCase.create(user1).uuid();
            final UUID user2Uuid = registerUserUseCase.create(user2).uuid();
            final TransientRemoteBotDto remoteBot = new TransientRemoteBotDto(UUID.randomUUID(), defaultUuid, "Remote Bot", "http://localhost", "8030", "https://github.com/gcontiero11/CTruco");
            botRepository.save(remoteBot);
            botRepository.authorizeByUuid(remoteBot.uuid());
            for (int i = 0; i < 30; i++) {
                gameResultRepository.save(new GameResultDto(UUID.randomUUID(), LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now(), defaultUuid, defaultUuid, 12, user1Uuid, 3));
                gameResultRepository.save(new GameResultDto(UUID.randomUUID(), LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now(), user1Uuid, user2Uuid, 7, user1Uuid, 12));
                gameResultRepository.save(new GameResultDto(UUID.randomUUID(), LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now(), defaultUuid, user2Uuid, 5, defaultUuid, 12));
            }
            gameResultRepository.save(new GameResultDto(UUID.randomUUID(), LocalDateTime.now().minusMinutes(5),
                    LocalDateTime.now(), user2Uuid, user2Uuid, 7, user1Uuid, 12));

            gameResultRepository.findTopWinners(3).forEach((entry -> System.out.println(entry.username() + " - " + entry.wins())));

//            gameResultRepository.findAllByUserUuid(defaultUuid).forEach(System.out::println);

        };
    }
}
