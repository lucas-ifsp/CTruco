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

package com.bueno.domain.usecases.game;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.repos.ActiveGameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindGameUseCaseTest {

    @Mock private ActiveGameRepository repo;
    @Mock private Game game;
    @InjectMocks private FindGameUseCase sut;

    @Test
    @DisplayName("Should load game by UUID")
    void shouldLoadGameByUuid() {
        final UUID uuid = UUID.randomUUID();
        when(repo.findByUuid(uuid)).thenReturn(Optional.of(game));
        assertThat(sut.load(uuid)).isEqualTo(Optional.of(game));
        verify(repo, times(1)).findByUuid(uuid);
    }

    @Test
    @DisplayName("Should load game by user UUID")
    void shouldLoadGameByUserUuid() {
        final UUID uuid = UUID.randomUUID();
        when(repo.findByUserUuid(uuid)).thenReturn(Optional.of(game));
        assertThat(sut.findByUserUuid(uuid)).isEqualTo(Optional.of(game));

        verify(repo, times(1)).findByUserUuid(uuid);
    }

    @Test
    @DisplayName("Should throw if game uuid is null")
    void shouldThrowIfGameUuidIsNull() {
        assertThatNullPointerException().isThrownBy(() -> sut.load(null));
    }

    @Test
    @DisplayName("Should throw if player uuid is null")
    void shouldThrowIfPlayerUuidIsNull() {
        assertThatNullPointerException().isThrownBy(() -> sut.findByUserUuid(null));
    }
}