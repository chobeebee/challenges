package com.baro.challenges.user.repository;

import com.baro.challenges.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);

    Optional<UserEntity> findByUsername(String username);
}
