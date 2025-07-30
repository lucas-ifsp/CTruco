package com.bueno.domain.usecases.bot.repository;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.TransientRemoteBotDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemoteBotRepository {
    List<RemoteBotDto> findAll();

    Optional<RemoteBotDto> findById(UUID uuid);

    Optional<RemoteBotDto> findByName(String name);

    List<RemoteBotDto> findByUserId(UUID userId);

    void save(TransientRemoteBotDto dto);

    void delete(TransientRemoteBotDto dto);

    void update(TransientRemoteBotDto dto);

    void authorizeByUuid(UUID uuid);

    void disableBot(UUID uuid);

    boolean existByName(String botName);
}
