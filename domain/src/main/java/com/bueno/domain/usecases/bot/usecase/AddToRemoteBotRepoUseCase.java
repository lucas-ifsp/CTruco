package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;

public class AddToRemoteBotRepoUseCase {

    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotDto dtoToAdd;

    public AddToRemoteBotRepoUseCase(RemoteBotRepository remoteBotRepository, RemoteBotDto dtoToAdd) {
        this.remoteBotRepository = remoteBotRepository;
        this.dtoToAdd = dtoToAdd;
    }

    public void addBot(){
        remoteBotRepository.save(dtoToAdd);
    }
}
