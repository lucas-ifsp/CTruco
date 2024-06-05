package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class RemoteBotRepositoryImpl implements RemoteBotRepository {

    private final RemoteBotDao dao;
    private final UserRepository userRepositoryImpl;

    public RemoteBotRepositoryImpl(RemoteBotDao dao, UserRepository userRepositoryImpl) {
        this.dao = dao;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public List<RemoteBotDto> findAll() {
        try {
            return dao.findAll().stream().map(RemoteBotEntity::toRemoteBotDto).toList();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<RemoteBotDto> findByName(String name) {
        final Optional<RemoteBotEntity> dto;
        try {
            dto = dao.getByName(name);
            if (dto.isPresent()) {
                return Optional.of(RemoteBotEntity.toRemoteBotDto(dto.get()));
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<RemoteBotDto> findById(UUID uuid) {
        final Optional<RemoteBotEntity> dto;
        try {
            dto = dao.getByUuid(uuid);
            if (dto.isPresent()) {
                return Optional.of(RemoteBotEntity.toRemoteBotDto(dto.orElse(null)));
            }
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<RemoteBotDto> findByUserId(UUID userId) {
        List<RemoteBotEntity> botEntities;
        try {
            botEntities = dao.getRemoteBotEntitiesByUser(UserEntity
                    .from(userRepositoryImpl.findByUuid(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"))));
            return botEntities.stream().map(RemoteBotEntity::toRemoteBotDto).toList();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }


    @Override
    public void save(RemoteBotDto dto) {
        try {
            dao.save(Objects.requireNonNull(RemoteBotEntity.from(dto)));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the RemoteBot.");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(RemoteBotDto dto) {
        try {
            dao.delete(Objects.requireNonNull(RemoteBotEntity.from(dto)));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't delete the RemoteBot: " + dto.name());
            e.printStackTrace();
        }
    }

    @Override
    public boolean existByName(String botName) {
        try {
            return dao.existsByName(botName);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the RemoteBot.");
            e.printStackTrace();
            return false;
        }
    }
}
