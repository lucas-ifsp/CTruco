package com.bueno.persistence.dto;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchEntity {
    private UUID uuid;
    private int matchNumber;
    private String p1Name;
    private String p2Name;
    private boolean isAvailable;
    private String winnerName;
    private long p1Score;
    private long p2Score;
    private long timeToExecute;
    private UUID next;

    public static MatchEntity from(MatchDTO dto) {
        return MatchEntity.builder()
                .uuid(dto.uuid())
                .matchNumber(dto.matchNumber())
                .p1Name(dto.p1Name())
                .p2Name(dto.p2Name())
                .isAvailable(dto.available())
                .winnerName(dto.winnerName())
                .p1Score(dto.p1Score())
                .p2Score(dto.p2Score())
                .timeToExecute(dto.timeToExecute())
                .next(dto.next())
                .build();
    }

    public MatchDTO toDto() {
        return new MatchDTO(uuid,
                matchNumber,
                p1Name,
                p2Name,
                isAvailable,
                winnerName,
                p1Score,
                p2Score,
                timeToExecute,
                next);
    }
}
