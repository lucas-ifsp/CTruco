package com.bueno.domain.usecases.bot.repository;

import java.util.List;

public interface RemoteBotRepository {
    List<RemoteBotDto> findAll();
    void save(RemoteBotDto dto);
    void delete(RemoteBotDto dto);
    boolean existByName(String botName);
}
