package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void save(RemoteBotDto dto) { throw new UnsupportedOperationException();}

    private static RemoteBotDto toRemoteBotDto(String line) {
        final String[] data = line.split(";");
        return new RemoteBotDto(data[0], data[1], data[2], data[3]);
    }
}
