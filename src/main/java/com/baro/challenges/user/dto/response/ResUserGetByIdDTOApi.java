package com.baro.challenges.user.dto.response;

import com.baro.challenges.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResUserGetByIdDTOApi {

    private User user;

    public static ResUserGetByIdDTOApi of(UserEntity userEntity) {
        return ResUserGetByIdDTOApi.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User {
        private Long userId;
        private String username;
        private UserEntity.Role role;

        public static User from(UserEntity userEntity) {
            return User.builder()
                    .userId(userEntity.getUserId())
                    .username(userEntity.getUsername())
                    .role(userEntity.getRole())
                    .build();
        }
    }
}
