package com.baro.challenges.user.contorller;

import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignUpDTOApi;
import com.baro.challenges.user.service.UserService;
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

    private final UserService userService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResDTO<ResAuthPostSignUpDTOApi>> signUp(@RequestBody @Valid ReqAuthPostSignUpDTOApi reqDto){

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignUpDTOApi.of(userService.signUp(reqDto))),
                HttpStatus.OK
        );
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<ResDTO<ResAuthPostSignInDTOApi>> signIn(@RequestBody @Valid ReqAuthPostSignInDTOApi reqDto){

        return new ResponseEntity<>(
                ResDTO.success(userService.signIn(reqDto)),
                HttpStatus.OK
        );
    }
}
