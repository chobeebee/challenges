package com.baro.challenges.user.repository;

import com.baro.challenges.user.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository {
    boolean existRefreshTokenByUsername(String username);

    Optional<RefreshTokenEntity> findByUsername(String username);

    void save(RefreshTokenEntity refreshEntity);
}
