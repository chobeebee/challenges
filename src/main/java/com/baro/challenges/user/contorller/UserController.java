package com.baro.challenges.user.contorller;

import com.baro.challenges.common.exception.CustomException;
import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignUpDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import com.baro.challenges.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResDTO<ResAuthPostSignUpDTOApi>> signUp(@RequestBody @Valid ReqAuthPostSignUpDTOApi reqDto){
        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        // 아이디 중복 체크
        Optional<UserEntity> userCheck = userRepository.findByUsername(username);
        if(userCheck.isPresent()) throw new CustomException(ResponseCode.USER_ALREADY_EXISTS);

        // 회원 생성
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(password)
                .role(reqDto.getUser().getRole())
                .build();

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignUpDTOApi.of(userRepository.save(userEntity))),
                HttpStatus.OK
        );
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<ResDTO<ResAuthPostSignInDTOApi>> signIn(@RequestBody @Valid ReqAuthPostSignInDTOApi reqDto){
        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        // 회원 정보 조회
        UserEntity userEntity = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new CustomException(ResponseCode.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 체크
        if(!Objects.equals(password, userEntity.getPassword())) throw new CustomException(ResponseCode.PASSWORD_MISMATCH);

        String accessJwt = "access";
        String refreshJwt = "refresh";

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignInDTOApi.of(accessJwt, refreshJwt)),
                HttpStatus.OK
        );
    }
}
