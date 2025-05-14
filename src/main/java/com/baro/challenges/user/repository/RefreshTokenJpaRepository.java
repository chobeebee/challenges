package com.baro.challenges.user.repository;

import com.baro.challenges.user.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    boolean existsRefreshTokenEntityByUsername(String username);

    Optional<RefreshTokenEntity> findByUsername(String username);
}
