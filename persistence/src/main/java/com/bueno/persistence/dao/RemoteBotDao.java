package com.bueno.persistence.dao;

import com.bueno.persistence.dto.RemoteBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RemoteBotDao extends JpaRepository<RemoteBotEntity,Long> {
    RemoteBotEntity getById(UUID uuid);
    RemoteBotEntity getByName(String username);
    boolean existsByName(String botName);
}
