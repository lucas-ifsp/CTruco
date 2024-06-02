package com.bueno.persistence.daoimpl;

import com.bueno.persistence.dao.UserDao;
import com.bueno.persistence.dto.UserEntity;

import java.util.UUID;

public class UserDaoImpl implements UserDao {

    @Override
    public UserEntity getByUuid(UUID uuid) {
        return null;
    }

    @Override
    public UserEntity getByEmail(String email) {
        return null;
    }

    @Override
    public UserEntity getByUsername(String username) {
        return null;
    }

    @Override
    public void save(UserEntity user) {

    }
}
