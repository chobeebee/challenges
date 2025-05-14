package com.baro.challenges.user.service;

import com.baro.challenges.common.exception.CustomException;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.entity.RefreshTokenEntity;
import com.baro.challenges.user.entity.UserEntity;
import com.baro.challenges.user.jwt.JWTUtil;
import com.baro.challenges.user.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    // 회원 가입
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

    // 로그인 및 JWT 토큰 발급
    @Override
    @Transactional
    public ResAuthPostSignInDTOApi signIn(ReqAuthPostSignInDTOApi reqDto) {
        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        // 회원 정보 조회 (탈퇴하지 않은 회원)
        UserEntity userEntity = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new CustomException(ResponseCode.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 체크
        if(!Objects.equals(password, userEntity.getPassword())) throw new CustomException(ResponseCode.PASSWORD_MISMATCH);

        // accessToken 발급
        String accessToken = jwtUtil.createAccessToken(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getRole().name()
        );

        // refreshToken 발급
        String refreshToken = jwtUtil.createRefreshToken(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getRole().name()
        );

        // refreshToken 엔티티 생성
        RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                .username(userEntity.getUsername())
                .refreshToken(refreshToken)
                .expiration(jwtUtil.getRefreshTokenExpirationTime())
                .build();

        // 기존 토큰이 존재하면 업데이트, 없으면 저장
        if (refreshTokenRepository.existRefreshTokenByUsername(userEntity.getUsername())) {
            RefreshTokenEntity userRefreshToken = refreshTokenRepository
                    .findByUsername(userEntity.getUsername())
                    .orElse(null);

            userRefreshToken.updateRefreshToken(refreshToken, jwtUtil.getRefreshTokenExpirationTime());

        } else {
            refreshTokenRepository.save(refreshEntity);
        }

        return ResAuthPostSignInDTOApi.of(accessToken, refreshToken);
    }
}
