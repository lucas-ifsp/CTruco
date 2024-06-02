package com.bueno.persistence.dao;

import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemoteBotDao{
    RemoteBotEntity getById(UUID uuid);

    Optional<RemoteBotEntity> getByName(String username);

    List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity);

    boolean existsByName(String botName);

    void save(RemoteBotEntity bot);

    void delete(RemoteBotEntity bot);

    List<RemoteBotEntity> findAll();
}
