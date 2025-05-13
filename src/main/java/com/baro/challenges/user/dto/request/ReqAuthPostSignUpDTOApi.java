package com.baro.challenges.user.dto.request;

import com.baro.challenges.user.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqAuthPostSignUpDTOApi {
    @Valid
    @NotNull(message = "회원 정보를 입력해주세요.")
    private User user;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        //영문 소문자, 숫자를 포함한 4~10자리 아이디
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 영문 소문자와 숫자만 가능하며 4자 이상 10이하만 가능합니다.")
        private String username;

        //영문 대소문자, 숫자, 특수문자를 포함한 8~15자리 비밀번호
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])[^\\s]{8,15}$",
                message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8자 이상 15자 이하만 가능합니다.")
        private String password;

        @NotNull
        private UserEntity.Role role;
    }
}
