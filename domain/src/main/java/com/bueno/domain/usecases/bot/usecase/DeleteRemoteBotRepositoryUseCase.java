package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteRemoteBotRepositoryUseCase {
    private final RemoteBotRepository remoteBotRepository;

    public DeleteRemoteBotRepositoryUseCase(RemoteBotRepository remoteBotRepository) {
        this.remoteBotRepository = remoteBotRepository;
    }

    public void delete(String name) throws EntityNotFoundException {
        Optional<RemoteBotDto> botToDelete = remoteBotRepository.findAll().stream()
                .filter(remoteBotDto -> remoteBotDto.name().equals(name))
                .findFirst();
        if (botToDelete.isEmpty()) throw new EntityNotFoundException("Bot with name: " + name + " wasn't found");
        remoteBotRepository.delete(botToDelete.get());
    }
}
