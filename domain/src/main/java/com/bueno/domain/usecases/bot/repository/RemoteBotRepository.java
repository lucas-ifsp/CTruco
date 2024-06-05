package com.bueno.domain.usecases.bot.repository;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemoteBotRepository {
    List<RemoteBotDto> findAll();

    Optional<RemoteBotDto> findById(UUID uuid);

    Optional<RemoteBotDto> findByName(String name);

    List<RemoteBotDto> findByUserId(UUID userId);

    void save(RemoteBotDto dto);

    void delete(RemoteBotDto dto);

    boolean existByName(String botName);
}
