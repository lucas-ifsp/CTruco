package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetRemoteBotRepositoryUseCase {
    private final RemoteBotRepository remoteBotRepository;
    private final UserRepository userRepository;


    public GetRemoteBotRepositoryUseCase(RemoteBotRepository remoteBotRepository, UserRepository userRepository) {
        this.remoteBotRepository = remoteBotRepository;
        this.userRepository = userRepository;
    }

    public List<RemoteBotResponseModel> getAll() {
        List<RemoteBotDto> botDtoList = remoteBotRepository.findAll();
        return botDtoList.stream()
                .map(bot -> new RemoteBotResponseModel(bot.name(), getApplicationUserDto(bot).username(), bot.url(), bot.port())).toList();
    }

    public RemoteBotResponseModel getByName(String botName) throws EntityNotFoundException {
        RemoteBotDto dto = remoteBotRepository.findByName(botName).orElseThrow(() -> new EntityNotFoundException(botName + " not found"));
        ApplicationUserDto userDto = getApplicationUserDto(dto);
        return new RemoteBotResponseModel(dto.name(), userDto.username(), dto.url(), dto.port());
    }

    private ApplicationUserDto getApplicationUserDto(RemoteBotDto dto) {
        return userRepository.findByUuid(dto.user())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<RemoteBotResponseModel> getByUserId(UUID userId) {
        ApplicationUserDto userDto = userRepository.findByUuid(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<RemoteBotDto> bots = remoteBotRepository.findByUserId(userId);
        return bots.stream().map(bot -> new RemoteBotResponseModel(bot.name(), userDto.username(), bot.url(), bot.port())).toList();
    }
}
