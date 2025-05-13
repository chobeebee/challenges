package com.baro.challenges.user.contorller;

import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignUpDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResDTO<ResAuthPostSignUpDTOApi>> signUp(@RequestBody @Valid ReqAuthPostSignUpDTOApi reqDto){
        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(password)
                .role(reqDto.getUser().getRole())
                .build();

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignUpDTOApi.of(userEntity)),
                HttpStatus.OK
        );
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<ResDTO<ResAuthPostSignInDTOApi>> signIn(@RequestBody @Valid ReqAuthPostSignInDTOApi reqDto){
        String accessJwt = "access";
        String refreshJwt = "refresh";

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignInDTOApi.of(accessJwt, refreshJwt)),
                HttpStatus.OK
        );
    }
}
