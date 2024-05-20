package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotRequestModel;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.utils.exceptions.EntityAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AddBotRemoteBotRepositoryUseCase {
    private final RemoteBotRepository remoteBotRepository;
    private final UserRepository userRepository;

    public AddBotRemoteBotRepositoryUseCase(RemoteBotRepository remoteBotRepository, UserRepository userRepository) {
        this.remoteBotRepository = remoteBotRepository;
        this.userRepository = userRepository;
    }

    public RemoteBotResponseModel addBot(RemoteBotRequestModel dtoRequest) {
        Objects.requireNonNull(dtoRequest, "request is null");// TODO - Faz tudo desse jeito aqui ó
        RemoteBotDto dto = new RemoteBotDto(UUID.randomUUID(),
                dtoRequest.userId(),
                dtoRequest.name(),
                dtoRequest.url(),
                dtoRequest.port());
        if (remoteBotRepository.existByName(dto.name())) {
            throw new EntityAlreadyExistsException("Trying to add a bot with same name or port of one already registered");
        }
        ApplicationUserDto userDto = userRepository.findByUuid(dto.user())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        remoteBotRepository.save(dto);
        return new RemoteBotResponseModel(dto.name(), userDto.username(), dto.url(), dto.port());
    }
}
