package com.baro.challenges.user.repository;

import com.baro.challenges.common.exception.CustomException;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.user.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public boolean existRefreshTokenByUsername(String username) {
        return jpaRepository.existsRefreshTokenEntityByUsername(username);
    }

    @Override
    public Optional<RefreshTokenEntity> findByUsername(String username) {
        RefreshTokenEntity refreshToken = jpaRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.TOKEN_NOT_FOUND));
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void save(RefreshTokenEntity refreshEntity) {
        jpaRepository.save(refreshEntity);
    }
}
