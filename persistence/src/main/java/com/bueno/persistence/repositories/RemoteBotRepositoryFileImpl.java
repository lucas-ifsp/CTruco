package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.DtoNotStringableException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class RemoteBotRepositoryFileImpl implements RemoteBotRepository {

    private final List<String> remoteBotsMock = List.of(
            "bot1;email1@gmail.com;http://localhost;8080",
            "bot2;email2@gmail.com;http://localhost;8000");

    @Override
    public List<RemoteBotDto> findAll() {
        return remoteBotsMock.stream()
                .map(RemoteBotRepositoryFileImpl::toRemoteBotDto)
                .toList();
    }

    @Override
    public void save(RemoteBotDto dto) {
        try {
            String str = toRemoteBotsString(dto);
            remoteBotsMock.add(str);
            System.out.println("Bot: " + dto + " added.");
        } catch (Exception e) {
            System.out.println("Wasn't possible to add bot: " + dto);
        }
    }

    private static RemoteBotDto toRemoteBotDto(String line) {
        final String[] data = line.split(";");
        return new RemoteBotDto(data[0], data[1], data[2], data[3]);
    }

    private String toRemoteBotsString(RemoteBotDto dto) throws DtoNotStringableException {
        if(dto.name() == null || dto.email() == null || dto.url() == null || dto.port() == null) throw new DtoNotStringableException("this dto isn't complete");
        return dto.name() + ";" + dto.email() + ";" + dto.url() + ";" + dto.port();
    }
}
