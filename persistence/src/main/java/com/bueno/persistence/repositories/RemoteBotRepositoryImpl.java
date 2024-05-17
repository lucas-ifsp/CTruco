package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RemoteBotRepositoryImpl implements RemoteBotRepository {

    private final RemoteBotDao dao;
    private final UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public RemoteBotRepositoryImpl(RemoteBotDao dao, UserRepositoryImpl userRepositoryImpl) {
        this.dao = dao;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public List<RemoteBotDto> findAll() {
        return dao.findAll().stream().map(RemoteBotEntity::toRemoteBotDto).toList();
    }

    public Optional<RemoteBotDto> findByName(String name){
        final RemoteBotEntity dto = dao.getByName(name);
        return Optional.ofNullable(RemoteBotEntity.toRemoteBotDto(dto));
    }

    public Optional<RemoteBotDto> findById(UUID uuid){
        final RemoteBotEntity dto = dao.getById(uuid);
        return Optional.ofNullable(RemoteBotEntity.toRemoteBotDto(dto));
    }

    @Override
    public void save(RemoteBotDto dto){
        dao.save(Objects.requireNonNull(RemoteBotEntity.from(dto, userRepositoryImpl)));
    }

    @Override
    public void delete(RemoteBotDto dto){
        dao.delete(Objects.requireNonNull(RemoteBotEntity.from(dto, userRepositoryImpl)));
    }

    @Override
    public boolean existByName(String botName) {
        return dao.existsByName(botName);
    }
}
