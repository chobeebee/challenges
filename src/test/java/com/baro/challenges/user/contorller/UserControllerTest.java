package com.baro.challenges.user.contorller;

import com.baro.challenges.common.exception.CustomException;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.common.exception.handler.CustomAccessDeniedHandler;
import com.baro.challenges.user.config.SecurityConfig;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import com.baro.challenges.user.jwt.JWTUtil;
import com.baro.challenges.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        // 테스트에서 사용할 간단한 동작 설정
       doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }).when(customAccessDeniedHandler).handle(any(), any(), any());
    }

    @Test
    @DisplayName("회원가입 성공")
    void testUserSignUpSuccess() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        String password = "Test1234!@";
        UserEntity.Role role = UserEntity.Role.USER;

        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .role(role)
                .build();

        ReqAuthPostSignUpDTOApi.User req = ReqAuthPostSignUpDTOApi.User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        ReqAuthPostSignUpDTOApi request = ReqAuthPostSignUpDTOApi.builder()
                .user(req)
                .build();

        given(userService.signUp(any())).willReturn(userEntity);

        // when & then
        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void testUserSignUpFail() throws Exception {
        // given
        String username = "testuser";
        String password = "Test1234!@";
        UserEntity.Role role = UserEntity.Role.USER;

        ReqAuthPostSignUpDTOApi.User req = ReqAuthPostSignUpDTOApi.User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        ReqAuthPostSignUpDTOApi request = ReqAuthPostSignUpDTOApi.builder()
                .user(req)
                .build();

        given(userService.signUp(any())).willThrow(new CustomException(ResponseCode.USER_ALREADY_EXISTS));

        // when & then
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    void testUserSignInSuccess() throws Exception {
        // given
        String username = "testuser";
        String password = "Test1234!@";

        ReqAuthPostSignInDTOApi.User requestUser = ReqAuthPostSignInDTOApi.User.builder()
                .username(username)
                .password(password)
                .build();

        ReqAuthPostSignInDTOApi request = ReqAuthPostSignInDTOApi.builder()
                .user(requestUser)
                .build();

        ResAuthPostSignInDTOApi response = ResAuthPostSignInDTOApi.builder()
                .accessJwt("mock-access-token")
                .refreshJwt("mock-refresh-token")
                .build();

        given(userService.signIn(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 아이디 불일치")
    void testUserSignInFailWhenNotFoundUser() throws Exception {
        // given
        String username = "testuser";
        String password = "Test1234!@";

        ReqAuthPostSignInDTOApi.User requestUser = ReqAuthPostSignInDTOApi.User.builder()
                .username(username)
                .password(password)
                .build();

        ReqAuthPostSignInDTOApi request = ReqAuthPostSignInDTOApi.builder()
                .user(requestUser)
                .build();

        given(userService.signIn(any())).willThrow(new CustomException(ResponseCode.USER_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void testUserSignInFailWhenPasswordIncorrect() throws Exception {
        // given
        String username = "testuser";
        String password = "Test1234!@";

        ReqAuthPostSignInDTOApi.User requestUser = ReqAuthPostSignInDTOApi.User.builder()
                .username(username)
                .password(password)
                .build();

        ReqAuthPostSignInDTOApi request = ReqAuthPostSignInDTOApi.builder()
                .user(requestUser)
                .build();

        given(userService.signIn(any())).willThrow(new CustomException(ResponseCode.PASSWORD_MISMATCH));

        // when & then
        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}