package com.baro.challenges.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostSignInDTOApi {

    private String accessJwt;
    private String refreshJwt;

    public static ResAuthPostSignInDTOApi of(String accessJwt, String refreshJwt) {
        return ResAuthPostSignInDTOApi.builder()
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .build();
    }
}
