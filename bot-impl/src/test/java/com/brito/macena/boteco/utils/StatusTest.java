/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.brito.macena.boteco.utils;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Status Tests")
public class StatusTest {
    @BeforeAll
    static void setupAll() { System.out.println("Starting Status tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing Status tests..."); }

    @Nested
    @DisplayName("Status Definition Tests")
    class StatusDefinitionTests {
        @Test
        @DisplayName("Status BAD is defined")
        void statusBadIsDefined() {
            assertThat(Status.valueOf("BAD")).isNotNull();
        }

        @Test
        @DisplayName("Status MEDIUM is defined")
        void statusMediumIsDefined() {
            assertThat(Status.valueOf("MEDIUM")).isNotNull();
        }

        @Test
        @DisplayName("Status GOOD is defined")
        void statusGoodIsDefined() {
            assertThat(Status.valueOf("GOOD")).isNotNull();
        }

        @Test
        @DisplayName("Status EXCELLENT is defined")
        void statusExcellentIsDefined() {
            assertThat(Status.valueOf("EXCELLENT")).isNotNull();
        }
    }

    @Nested
    @DisplayName("Status Values Tests")
    class StatusValuesTests {
        @Test
        @DisplayName("Status values contain all statuses")
        void statusValuesContainsAllStatuses() {
            Status[] statuses = Status.values();
            assertThat(statuses).containsExactly(
                    Status.BAD,
                    Status.MEDIUM,
                    Status.GOOD,
                    Status.EXCELLENT);
        }
    }
}
