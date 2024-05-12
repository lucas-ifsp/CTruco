package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.DtoNotStringableException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;

import java.util.Optional;
import java.util.function.Predicate;

public class RemoteBotRepositoryUseCase {
    private final RemoteBotRepository remoteBotRepository;

    public RemoteBotRepositoryUseCase(RemoteBotRepository remoteBotRepository) {
        this.remoteBotRepository = remoteBotRepository;
    }

    public void addBot(RemoteBotDto dtoToadd) throws DtoNotStringableException, RemoteBotAlreadyExistsException {
        if(remoteBotRepository.findAll().stream().anyMatch(checkIfBotAlreadyExists(dtoToadd))){
            throw new RemoteBotAlreadyExistsException("Trying to add a bot with same name or port of one already registered");
        }
        remoteBotRepository.save(dtoToadd);
    }

    public void delete(String name) throws RemoteBotNotFoundException, DtoNotStringableException {
        Optional<RemoteBotDto> botToDelete = remoteBotRepository.findAll().stream()
                .filter(remoteBotDto -> remoteBotDto.name().equals(name))
                .findFirst();
        if (botToDelete.isEmpty()) throw new RemoteBotNotFoundException("Bot with name: " + name + "wasn't found");
        remoteBotRepository.remove(botToDelete.get());
    }

    public void update(String botToUpdateName,RemoteBotDto newDto) throws   RemoteBotNotFoundException,
                                                                            DtoNotStringableException,
                                                                            RemoteBotAlreadyExistsException {
        delete(botToUpdateName);
        addBot(newDto);
    }

    private Predicate<RemoteBotDto> checkIfBotAlreadyExists(RemoteBotDto dtoToAdd) {
        return remoteBotDto -> remoteBotDto.name().equals(dtoToAdd.name()) ||
                dtoToAdd.port().equals(remoteBotDto.port());
    }
}
