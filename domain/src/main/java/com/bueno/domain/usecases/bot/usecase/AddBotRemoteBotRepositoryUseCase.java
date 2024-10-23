package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotRequestModel;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.utils.exceptions.EntityAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.InvalidRequestException;
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
        Objects.requireNonNull(dtoRequest, "request is null");
        Objects.requireNonNull(dtoRequest.name(), "name is null");
        Objects.requireNonNull(dtoRequest.url(), "url is null");
        Objects.requireNonNull(dtoRequest.port(), "port is null");

        if (dtoRequest.name().trim().length() < 4)
            throw new InvalidRequestException("nome inválido");

        if (dtoRequest.url().trim().length() < 4)
            throw new InvalidRequestException("url inválida");

        if (dtoRequest.port().trim().length() != 4)
            throw new InvalidRequestException("porta inválida");


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
