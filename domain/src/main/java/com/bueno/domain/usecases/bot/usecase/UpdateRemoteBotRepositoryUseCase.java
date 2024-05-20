package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateRemoteBotRepositoryUseCase {
    private final RemoteBotRepository botRepository;


    public UpdateRemoteBotRepositoryUseCase(RemoteBotRepository botRepository) {
        this.botRepository = botRepository;
    }


    public RemoteBotResponseModel update(String name, RemoteBotDto dto) {// TODO - Implementar método update e fazer as verificações necessárias
        return null;
    }
}
