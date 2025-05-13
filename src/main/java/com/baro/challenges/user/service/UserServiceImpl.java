package com.baro.challenges.user.service;

import com.baro.challenges.common.exception.CustomException;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import com.baro.challenges.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserEntity signUp(ReqAuthPostSignUpDTOApi reqDto) {

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
        userRepository.save(userEntity);

        return userEntity;
    }

    @Override
    @Transactional
    public ResAuthPostSignInDTOApi signIn(ReqAuthPostSignInDTOApi reqDto) {
        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        // 회원 정보 조회
        UserEntity userEntity = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new CustomException(ResponseCode.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 체크
        if(!Objects.equals(password, userEntity.getPassword())) throw new CustomException(ResponseCode.PASSWORD_MISMATCH);

        // 토큰 생성
        String accessJwt = "access";
        String refreshJwt = "refresh";

        return ResAuthPostSignInDTOApi.of(accessJwt, refreshJwt);
    }
}
