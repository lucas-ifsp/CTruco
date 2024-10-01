package com.bueno.persistence.dto;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TournamentEntity {
    private UUID uuid;
    private List<String> participantsNames;
    private List<MatchEntity> matches;
    private int size;
    private int times;
    private String winnerName;

    public static TournamentEntity from(TournamentDTO dto) {
        return TournamentEntity.builder()
                .uuid(dto.uuid())
                .participantsNames(dto.participantsNames())
                .matches(dto.matchesDTO().stream().map(MatchEntity::from).toList())
                .size(dto.size())
                .times(dto.times())
                .winnerName(dto.winnerName())
                .build();
    }

    public TournamentDTO toDto() {
        return new TournamentDTO(uuid,
                participantsNames,
                matches.stream().map(MatchEntity::toDto).toList(),
                size,
                times,
                winnerName);
    }
}
