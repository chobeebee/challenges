package com.baro.challenges.common.exception.handler;

import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        try {
            ResponseCode code = ResponseCode.FORBIDDEN;
            ResDTO<Object> body = ResDTO.fail(code);

            String json = objectMapper.writeValueAsString(body);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("AccessDeniedHandler 응답 작성 중 IOException 발생: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("AccessDeniedHandler 예상치 못한 에러 발생: {}", e.getMessage(), e);
        }
    }
}
