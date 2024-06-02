package com.bueno.persistence.daoimpl;

import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RemoteBotDaoImpl implements RemoteBotDao {
    @Override
    public RemoteBotEntity getById(UUID uuid) {
        return null;
    }

    @Override
    public Optional<RemoteBotEntity> getByName(String username) {
        return Optional.empty();
    }

    @Override
    public List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity) {
        return List.of();
    }

    @Override
    public boolean existsByName(String botName) {
        return false;
    }

    @Override
    public void save(RemoteBotEntity bot) {

    }

    @Override
    public void delete(RemoteBotEntity bot) {

    }

    @Override
    public List<RemoteBotEntity> findAll() {
        return List.of();
    }
}
