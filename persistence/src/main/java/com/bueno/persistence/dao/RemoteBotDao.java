package com.bueno.persistence.dao;

import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RemoteBotDao extends JpaRepository<RemoteBotEntity, Long> {
    RemoteBotEntity getById(UUID uuid);

    Optional<RemoteBotEntity> getByName(String username);

    List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity);

    boolean existsByName(String botName);
}
