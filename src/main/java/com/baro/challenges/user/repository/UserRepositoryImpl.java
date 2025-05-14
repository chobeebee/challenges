package com.baro.challenges.user.repository;

import com.baro.challenges.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return jpaRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username) {
        return jpaRepository.findByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return jpaRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }
}
