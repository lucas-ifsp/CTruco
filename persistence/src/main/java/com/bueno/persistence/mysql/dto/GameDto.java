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

package com.bueno.persistence.mysql.dto;

import com.bueno.domain.entities.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "GAME")
public class GameDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    private UUID uuid;
    private UUID player1Uuid;
    private String player1Username;
    private UUID player2Uuid;
    private String player2Username;

    private GameDto(UUID uuid, UUID player1Uuid, String player1Username, UUID player2Uuid, String player2Username) {
        this.uuid = uuid;
        this.player1Uuid = player1Uuid;
        this.player1Username = player1Username;
        this.player2Uuid = player2Uuid;
        this.player2Username = player2Username;
    }

    public static GameDto of(Game game){
        return new GameDto(
                game.getUuid(),
                game.getPlayer1().getUuid(),
                game.getPlayer1().getUsername(),
                game.getPlayer2().getUuid(),
                game.getPlayer2().getUsername()
        );
    }
}
