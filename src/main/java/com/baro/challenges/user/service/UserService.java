package com.baro.challenges.user.service;

import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import jakarta.validation.Valid;

public interface UserService {
    UserEntity signUp(@Valid ReqAuthPostSignUpDTOApi reqDto);

    ResAuthPostSignInDTOApi signIn(@Valid ReqAuthPostSignInDTOApi reqDto);

    UserEntity getUserByUserId(Long userId);
}
