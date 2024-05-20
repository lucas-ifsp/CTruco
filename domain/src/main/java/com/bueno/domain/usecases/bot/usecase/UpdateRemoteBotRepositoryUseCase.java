package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotRequestModel;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UserNotAllowedException;
import org.springframework.stereotype.Service;

@Service
public class UpdateRemoteBotRepositoryUseCase {
    private final RemoteBotRepository botRepository;
    private final UserRepository userRepository;


    public UpdateRemoteBotRepositoryUseCase(RemoteBotRepository botRepository, UserRepository userRepository) {
        this.botRepository = botRepository;
        this.userRepository = userRepository;
    }


    public RemoteBotResponseModel update(String botName, RemoteBotRequestModel requestDto) {// TODO - Implementar método update e fazer as verificações necessárias

        RemoteBotDto bot = botRepository.findByName(botName).orElseThrow(() -> new EntityNotFoundException("bot not found"));
        String userName = userRepository
                .findByUuid(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .username();
        if (requestDto.userId() == bot.user()) {
            botRepository.delete(bot);
            RemoteBotDto newDto = new RemoteBotDto(bot.uuid(),
                    requestDto.userId(),
                    requestDto.name(),
                    requestDto.url(),
                    requestDto.port());
            botRepository.save(newDto);
            return new RemoteBotResponseModel(newDto.name(), userName, newDto.url(), newDto.port());
        }
        throw new UserNotAllowedException("User does not own this bot");
    }
}
