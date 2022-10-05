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

import com.bueno.domain.usecases.hand.dtos.HandResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "HAND_RESULT")
public class HandResultEntity {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "HAND_TYPE", length = 9)
    private String handType;
    private UUID gameUuid;
    private UUID handWinner;
    private int points;
    private int pointsProposal;

    @Column(name = "R1_WINNER")
    private UUID round1Winner;
    @Column(name = "R2_WINNER")
    private UUID round2Winner;
    @Column(name = "R3_WINNER")
    private UUID round3Winner;

    @Column(name = "VIRA", length = 2)
    private String vira;
    @Column(name = "R1_C1", length = 2)
    private String card1Round1;
    @Column(name = "R1_C2", length = 2)
    private String card2Round1;
    @Column(name = "R2_C1", length = 2)
    private String card1Round2;
    @Column(name = "R2_C2", length = 2)
    private String card2Round2;
    @Column(name = "R3_C1", length = 2)
    private String card1Round3;
    @Column(name = "R3_C2", length = 2)
    private String card2Round3;

    public static HandResultEntity from(HandResultDto dto){
        final List<String> openCards = dto.openCards();
        final List<UUID> roundWinners = dto.roundWinners();

        return HandResultEntity.builder()
                .gameUuid(dto.gameUuid())
                .handType(dto.handType())
                .handWinner(dto.handWinner())
                .points(dto.points())
                .pointsProposal(dto.pointsProposal())
                .vira(getIfAvailable(openCards, 0))
                .card1Round1(getIfAvailable(openCards, 1))
                .card2Round1(getIfAvailable(openCards, 2))
                .card1Round2(getIfAvailable(openCards, 3))
                .card2Round2(getIfAvailable(openCards, 4))
                .card1Round3(getIfAvailable(openCards, 5))
                .card2Round3(getIfAvailable(openCards, 6))
                .round1Winner(getIfAvailable(roundWinners, 0))
                .round2Winner(getIfAvailable(roundWinners, 1))
                .round3Winner(getIfAvailable(roundWinners, 2))
                .build();
    }

    private static <T> T getIfAvailable(List<T> list, int index){
        if(index < list.size()) return list.get(index);
        return null;
    }
}
