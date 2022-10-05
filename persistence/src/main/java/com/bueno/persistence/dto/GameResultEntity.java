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

package com.bueno.persistence.dto;


import com.bueno.domain.usecases.game.dtos.GameResultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GAME_RESULT")
public class GameResultEntity {
    @Id
    @Column(name = "GAME_ID")
    private UUID gameUuid;
    @Column(name = "GAME_START_TIME")
    private LocalDateTime gameStart;
    @Column(name = "GAME_END_TIME")
    private LocalDateTime gameEnd;
    @Column(name = "WINNER")
    private UUID winnerUuid;
    @Column(name = "PLAYER1")
    private UUID player1Uuid;
    @Column(name = "PLAYER1_SCORE")
    private int player1Score;
    @Column(name = "PLAYER2")
    private UUID player2Uuid;
    @Column(name = "PLAYER2_SCORE")
    private int player2Score;


    public static GameResultEntity from(GameResultDto dto){
        return new GameResultEntity(dto.gameUuid(), dto.gameStart(), dto.gameEnd(),
                dto.winnerUuid(), dto.player1Uuid(), dto.player1Score(), dto.player2Uuid(), dto.player2Score()
        );
    }
}
