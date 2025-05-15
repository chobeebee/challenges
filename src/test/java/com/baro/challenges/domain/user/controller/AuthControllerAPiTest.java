package com.baro.challenges.domain.user.controller;

import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.user.dto.request.ReqAuthPostSignInDTOApi;
import com.baro.challenges.user.dto.request.ReqAuthPostSignUpDTOApi;
import com.baro.challenges.user.dto.response.ResAuthPostSignInDTOApi;
import com.baro.challenges.user.entity.UserEntity;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerAPiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 회원가입 성공 테스트
    @Test
    public void testAuthPostSignUpSuccess() throws Exception {
        ReqAuthPostSignUpDTOApi reqDto = ReqAuthPostSignUpDTOApi.builder()
                .user(ReqAuthPostSignUpDTOApi.User.builder()
                        .username("testuser0")
                        .password("Test1234!@")
                        .role(UserEntity.Role.USER)
                        .build())
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-up")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("AUTH 회원가입 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH")
                                        .summary("AUTH 회원가입")
                                        .description("""
                                                ## AUTH 회원가입 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password, role을 입력받아 회원가입을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                                fieldWithPath("user.role").type(JsonFieldType.STRING).description("사용자 권한")
                                        )
                                        .build()
                                )
                        )
                )
                .andReturn();
    }

    // 회원가입 실패 테스트
    @Test
    public void testAuthPostSignUpFailed() throws Exception {
        objectMapper.readValue(
                signUp("duplicated", "Test1234!@", "USER").getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        ReqAuthPostSignUpDTOApi reqDto = ReqAuthPostSignUpDTOApi.builder()
                .user(ReqAuthPostSignUpDTOApi.User.builder()
                        .username("duplicated")
                        .password("Test1234!@")
                        .role(UserEntity.Role.USER)
                        .build())
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-up")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isConflict(),
                        MockMvcResultMatchers.jsonPath("code").value("US100")
                )
                .andDo(
                        document("AUTH 회원가입 실패",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH")
                                        .summary("AUTH 회원가입 실패")
                                        .description("""
                                                ## AUTH 회원가입 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password, role을 입력받아 회원가입을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                                fieldWithPath("user.role").type(JsonFieldType.STRING).description("사용자 권한")
                                        )
                                        .build()
                                )
                        )
                )
                .andReturn();
    }

    // 로그인 성공 테스트
    @Test
    @Transactional
    public void testAuthPostSignInSuccess() throws Exception {

        ReqAuthPostSignInDTOApi reqDto = ReqAuthPostSignInDTOApi.builder()
                .user(
                        ReqAuthPostSignInDTOApi.User.builder()
                                .username("testuser")
                                .password("Test1234!@")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("AUTH 로그인 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH")
                                        .summary("AUTH 로그인")
                                        .description("""
                                                ## AUTH 로그인 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password 입력받아 로그인을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 로그인 실패 테스트 - 아이디 불일치
    @Test
    @Transactional
    public void testAuthPostSignInFailWhenNotFoundUser() throws Exception {

        ReqAuthPostSignInDTOApi reqDto = ReqAuthPostSignInDTOApi.builder()
                .user(
                        ReqAuthPostSignInDTOApi.User.builder()
                                .username("noexituser")
                                .password("Test1234!@")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.jsonPath("code").value("US200")
                )
                .andDo(
                        document("AUTH 로그인 실패 - 아이디 불일치",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH")
                                        .summary("AUTH 로그인 실패")
                                        .description("""
                                                ## AUTH 로그인 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password 입력받아 로그인을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 로그인 실패 테스트 - 비밀번호 불일치
    @Test
    @Transactional
    public void testAuthPostSignInFailWhenPasswordIncorrect() throws Exception {

        ReqAuthPostSignInDTOApi reqDto = ReqAuthPostSignInDTOApi.builder()
                .user(
                        ReqAuthPostSignInDTOApi.User.builder()
                                .username("testuser")
                                .password("incorrect0!@")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("code").value("US201")
                )
                .andDo(
                        document("AUTH 로그인 실패 - 비밀번호 불일치",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH")
                                        .summary("AUTH 로그인 실패")
                                        .description("""
                                                ## AUTH 로그인 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password 입력받아 로그인을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 관리자용 회원 조회 성공 테스트 - 관리자
    @Test
    @Transactional
    public void testAuthGetUserSuccess() throws Exception {

        ResDTO<ResAuthPostSignInDTOApi> resDto = objectMapper.readValue(
                login("testmaster", "Test1234!@").getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/admin/userId/{userId}", 1)
                                .header("X-User-Role", "MASTER")
                                .header("X-User-Name", "testmaster")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ resDto.getData().getAccessJwt())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "MASTER용 회원 상세조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("ADMIN")
                                                .summary("MASTER용 회원 상세조회")
                                                .description("""
                                                ## MASTER가 회원을 상세조회하는 엔드포인트 입니다.
                                                """)
                                                .build()
                                )
                        )
                );
    }

    // 관리자용 회원 조회 실패 테스트 - 일반 회원
    @Test
    @Transactional
    public void testAuthGetUserFail() throws Exception {
        objectMapper.readValue(
                signUp("roleuser", "Test1234!@", "USER").getResponse().getContentAsString(),
                new TypeReference<>() {}

        );

        ResDTO<ResAuthPostSignInDTOApi> resDto = objectMapper.readValue(
                login("roleuser", "Test1234!@").getResponse().getContentAsString(),
                new TypeReference<>() {}

        );

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/admin/userId/{userId}", 1)
                                .header("X-User-Role", "USER")
                                .header("X-User-Name", "roleuser")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ resDto.getData().getAccessJwt())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isForbidden(),
                        MockMvcResultMatchers.jsonPath("code").value("002")
                )
                .andDo(
                        document(
                                "MASTER용 회원 상세조회 실패 - 일반 회원",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("ADMIN")
                                                .summary("MASTER용 회원 상세조회")
                                                .description("""
                                                ## MASTER가 회원을 상세조회하는 엔드포인트 입니다.
                                                """)
                                                .build()
                                )
                        )
                );
    }


    private MvcResult login(String username, String password) throws Exception {
        ReqAuthPostSignInDTOApi reqDto = ReqAuthPostSignInDTOApi.builder()
                .user(
                        ReqAuthPostSignInDTOApi.User.builder()
                                .username(username)
                                .password(password)
                                .build()
                )
                .build();
        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        return mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andReturn();
    }

    private MvcResult signUp(String username, String password, String role) throws Exception {
        ReqAuthPostSignUpDTOApi reqDto = ReqAuthPostSignUpDTOApi.builder()
                .user(
                        ReqAuthPostSignUpDTOApi.User.builder()
                                .username(username)
                                .password(password)
                                .role(UserEntity.Role.valueOf(role))
                                .build()
                )
                .build();
        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        return mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/sign-up")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andReturn();
    }
}
