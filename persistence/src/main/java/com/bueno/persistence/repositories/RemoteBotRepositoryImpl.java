package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class RemoteBotRepositoryImpl implements RemoteBotRepository {

    private final RemoteBotDao dao;
    private final UserRepositoryImpl userRepositoryImpl;

    public RemoteBotRepositoryImpl(RemoteBotDao dao, UserRepositoryImpl userRepositoryImpl) {
        this.dao = dao;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public List<RemoteBotDto> findAll() {
        return dao.findAll().stream().map(RemoteBotEntity::toRemoteBotDto).toList();
    }

    public Optional<RemoteBotDto> findByName(String name) {
        final RemoteBotEntity dto = dao.getByName(name).orElseThrow(() -> new EntityNotFoundException("bot not found"));
        return Optional.of(RemoteBotEntity.toRemoteBotDto(dto));
    }

    @Override
    public List<RemoteBotDto> findByUserId(UUID userId) {
        List<RemoteBotEntity> botEntities = dao.getRemoteBotEntitiesByUser(UserEntity
                .from(userRepositoryImpl.findByUuid(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found"))));
        return botEntities.stream().map(RemoteBotEntity::toRemoteBotDto).toList();
    }


    public Optional<RemoteBotDto> findById(UUID uuid) {
        final RemoteBotEntity dto = dao.getById(uuid);
        return Optional.of(RemoteBotEntity.toRemoteBotDto(dto));
    }

    @Override
    public void save(RemoteBotDto dto) {
        dao.save(Objects.requireNonNull(RemoteBotEntity.from(dto, userRepositoryImpl)));
    }

    @Override
    public void delete(RemoteBotDto dto) {
        dao.delete(Objects.requireNonNull(RemoteBotEntity.from(dto, userRepositoryImpl)));
    }

    @Override
    public boolean existByName(String botName) {
        return dao.existsByName(botName);
    }
}
