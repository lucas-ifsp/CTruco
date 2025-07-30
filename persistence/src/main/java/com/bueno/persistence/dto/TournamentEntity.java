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
    private List<UUID> matchUUIDs;
    private int size;
    private int times;
    private int finalAndThirdPlaceMatchTimes;
    private String winnerName;

    public static TournamentEntity from(TournamentDTO dto) {
        return TournamentEntity.builder()
                .uuid(dto.uuid())
                .participantsNames(dto.participantsNames())
                .matchUUIDs(dto.matchUUIDs())
                .size(dto.size())
                .times(dto.times())
                .finalAndThirdPlaceMatchTimes(dto.finalAndThirdPlaceMatchTimes())
                .winnerName(dto.winnerName())
                .build();
    }

    public TournamentDTO toDto() {
        return new TournamentDTO(uuid,
                participantsNames,
                matchUUIDs,
                size,
                times,
                finalAndThirdPlaceMatchTimes,
                winnerName);
    }
}
