package com.baro.challenges.user.dto.response;

import com.baro.challenges.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostSignUpDTOApi {

    private User user;

    public static ResAuthPostSignUpDTOApi of(UserEntity userEntity){
        return ResAuthPostSignUpDTOApi.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User{
        private String username;

        public static User from(UserEntity userEntity){
            return User.builder()
                    .username(userEntity.getUsername())
                    .build();
        }
    }
}
