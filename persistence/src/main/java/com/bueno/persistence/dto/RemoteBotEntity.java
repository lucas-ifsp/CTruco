package com.bueno.persistence.dto;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemoteBotEntity {
    private UUID uuid;
    private UUID userUuid;
    private String name;
    private String url;
    private String port;


    public static RemoteBotEntity from(RemoteBotDto bot) {
        return new RemoteBotEntity(bot.uuid(), bot.user(), bot.name(), bot.url(), bot.port());
    }

    public static RemoteBotDto toRemoteBotDto(RemoteBotEntity dto) {
        Objects.requireNonNull(dto, "null dto");
        return new RemoteBotDto(dto.uuid, dto.userUuid, dto.name, dto.url, dto.port);
    }
}
