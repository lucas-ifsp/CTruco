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

package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.UserRecordDto;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRecordUseCase {
    private final GameResultRepository gameResultRepository;
    private final UserRepository userRepository;

    public UserRecordUseCase(GameResultRepository gameResultRepository, UserRepository userRepository) {
        this.gameResultRepository = gameResultRepository;
        this.userRepository = userRepository;
    }

    public UserRecordDto listByUuid(UUID userUuid){
        var user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userUuid));
        var userRecord = gameResultRepository.findAllByUserUuid(userUuid);
        return new UserRecordDto(user.uuid(), user.username(), userRecord);
    }
}
