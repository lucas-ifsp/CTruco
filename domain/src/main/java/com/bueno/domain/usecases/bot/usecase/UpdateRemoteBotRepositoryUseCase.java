package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotRequestModel;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UserNotAllowedException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UpdateRemoteBotRepositoryUseCase {
    private final RemoteBotRepository botRepository;
    private final UserRepository userRepository;

    public UpdateRemoteBotRepositoryUseCase(RemoteBotRepository botRepository, UserRepository userRepository) {
        this.botRepository = botRepository;
        this.userRepository = userRepository;
    }

    public RemoteBotResponseModel update(String botName, RemoteBotRequestModel requestDto) {// TODO mudar o jeito q o update é feito, passando por uma query de update no Banco

        Objects.requireNonNull(requestDto, "request is null");
        Objects.requireNonNull(botName, "botName is null");

        RemoteBotDto bot = botRepository.findByName(botName).orElseThrow(() -> new EntityNotFoundException("bot not found"));

        if (!isSameUser(bot.user(), requestDto.userId())) {
            throw new UserNotAllowedException("user not allowed to update bot");
        }

        ApplicationUserDto userOfNewBot = userRepository
                .findByUuid(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found, userId might be wrong"));

        RemoteBotDto newDto = new RemoteBotDto(bot.uuid(),
                requestDto.userId(),
                requestDto.name(),
                requestDto.url(),
                requestDto.port());
        botRepository.update(newDto);
        return new RemoteBotResponseModel(newDto.name(), userOfNewBot.username(), newDto.url(), newDto.port());
    }

    private boolean isSameUser(UUID botOwner, UUID requester) {
        return botOwner.equals(requester);
    }
}
