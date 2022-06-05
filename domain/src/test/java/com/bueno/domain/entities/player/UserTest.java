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

package com.bueno.domain.entities.player;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class UserTest {

    @Test
    @DisplayName("Should be able to create user without providing uuid")
    void shouldBeAbleToCreateUserWithoutProvidingUuid() {
        final User sut = new User("test", "test@test.com", "password");
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getUuid()).as("UUID").isNotNull();
        softly.assertThat(sut.getUsername()).isEqualTo("test");
        softly.assertThat(sut.getEmail()).isEqualTo("test@test.com");
        softly.assertThat(sut.getPassword()).isEqualTo("password");
        softly.assertAll();
    }

    @Test
    @DisplayName("Should throw if calls constructor with null username")
    void shouldThrowIfCallsConstructorWithNullUsername() {
        assertThatNullPointerException()
                .isThrownBy(() -> new User(UUID.randomUUID(), null,"test@test.com", "password"));
    }

    @Test
    @DisplayName("Should throw if calls constructor with null email")
    void shouldThrowIfCallsConstructorWithNullEmail() {
        assertThatNullPointerException()
                .isThrownBy(() -> new User(UUID.randomUUID(), "test", null, "password"));
    }

    @Test
    @DisplayName("Should users with same uuid and username be the same")
    void shouldUsersWithSameUuidAndUsernameBeTheSame() {
        final UUID uuid = UUID.randomUUID();
        final User user1 = new User(uuid, "test", "test@test.com", "password");
        final User user2 = new User(uuid, "test", "email@email.uk", "password");
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("Should users with same uuid and username have the same hashcode")
    void shouldUsersWithSameUuidAndUsernameHaveTheSameHashcode() {
        final UUID uuid = UUID.randomUUID();
        final User user1 = new User(uuid, "test", "test@test.com", "password");
        final User user2 = new User(uuid, "test", "email@email.uk", "password");
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        final User sut = new User("test", "test@test.com", "password");
        String expected = String.format("User = %s (%s), %s", sut.getUsername(), sut.getUuid(), sut.getEmail());
        assertThat(sut.toString()).isEqualTo(expected);
    }
}