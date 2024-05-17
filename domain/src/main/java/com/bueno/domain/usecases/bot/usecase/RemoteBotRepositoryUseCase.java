package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class RemoteBotRepositoryUseCase { // TODO - separar um usecase por verbo RemoteBotRegisterUseCase/RemoteBot
    private final RemoteBotRepository remoteBotRepository;

    public RemoteBotRepositoryUseCase(RemoteBotRepository remoteBotRepository) {
        this.remoteBotRepository = remoteBotRepository;
    }

    public List<RemoteBotDto> getAll(){
        return remoteBotRepository.findAll();
    }

    public RemoteBotDto getByName(String name) throws RemoteBotNotFoundException {
        final Optional<RemoteBotDto> bot = remoteBotRepository.findAll().stream()
                .filter(remoteBotDto -> remoteBotDto.name().equals(name)).limit(1)
                .findAny();
        if (bot.isEmpty()) throw new RemoteBotNotFoundException("Bot with name: " + name + " wasn't found");
        return bot.get();
    }

    public void addBot(RemoteBotDto dtoToAdd) throws RemoteBotAlreadyExistsException {
//        if(remoteBotRepository.findAll().stream().anyMatch(checkIfBotAlreadyExists(dtoToAdd)))
        Objects.requireNonNull(dtoToAdd, "Bot cannot be null");// TODO - Faz tudo desse jeito aqui ó
        if(remoteBotRepository.existByName(dtoToAdd.name())){
            throw new RemoteBotAlreadyExistsException("Trying to add a bot with same name or port of one already registered");
        }
        remoteBotRepository.save(dtoToAdd);
    }

    public void delete(String name) throws RemoteBotNotFoundException{
        Optional<RemoteBotDto> botToDelete = remoteBotRepository.findAll().stream()
                .filter(remoteBotDto -> remoteBotDto.name().equals(name))
                .findFirst();
        if (botToDelete.isEmpty()) throw new RemoteBotNotFoundException("Bot with name: " + name + " wasn't found");
        remoteBotRepository.delete(botToDelete.get());
    }

    public void update(String botToUpdateName,RemoteBotDto newDto) throws   RemoteBotNotFoundException,
                                                                            RemoteBotAlreadyExistsException {
        delete(botToUpdateName);
        addBot(newDto);
    }

    private Predicate<RemoteBotDto> checkIfBotAlreadyExists(RemoteBotDto dtoToAdd) {
        return remoteBotDto -> remoteBotDto.name().equals(dtoToAdd.name()) ||
                dtoToAdd.port().equals(remoteBotDto.port());
    }
}
