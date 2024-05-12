package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.DtoNotStringableException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RemoteBotRepositoryFileImpl implements RemoteBotRepository {

    private final List<String> remoteBotsMock = new ArrayList<>(List.of("bot1;email1@gmail.com;http://localhost;8080",
                                                                        "bot2;email2@gmail.com;http://localhost;8000"));

    @Override
    public List<RemoteBotDto> findAll() {
        return remoteBotsMock.stream()
                .map(RemoteBotRepositoryFileImpl::toRemoteBotDto)
                .toList();
    }

    @Override
    public void save(RemoteBotDto dto) throws DtoNotStringableException {
        remoteBotsMock.add(toRemoteBotsString(dto));
    }

    @Override
    public void remove(RemoteBotDto dto) throws DtoNotStringableException,RemoteBotNotFoundException {
        Optional<RemoteBotDto> botToRemove = findAll().stream()
                .filter(bot -> bot.equals(dto))
                .findFirst();
        if (botToRemove.isEmpty()) {
            throw new RemoteBotNotFoundException("Wasn't possible to find bot: " + dto.name());
        }
        remoteBotsMock.remove(toRemoteBotsString(botToRemove.get()));
    }

    private static RemoteBotDto toRemoteBotDto(String line) {
        final String[] data = line.split(";");
        return new RemoteBotDto(data[0], data[1], data[2], data[3]);
    }

    private String toRemoteBotsString(RemoteBotDto dto) throws DtoNotStringableException {
        if(dto.name() == null || dto.email() == null || dto.url() == null || dto.port() == null)
            throw new DtoNotStringableException("Wasn't possible to convert bot: " + dto.name() + " dto to string");

        return dto.name() + ";" + dto.email() + ";" + dto.url() + ";" + dto.port();
    }

}
