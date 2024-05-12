package com.bueno.domain.usecases.bot.repository;

import com.bueno.domain.usecases.utils.exceptions.DtoNotStringableException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;

import java.util.List;

public interface RemoteBotRepository{
    List<RemoteBotDto> findAll();
    void save(RemoteBotDto dto) throws DtoNotStringableException;
    void remove(RemoteBotDto dto) throws DtoNotStringableException, RemoteBotNotFoundException;
}
