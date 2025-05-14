package com.baro.challenges.user.jwt;

import com.baro.challenges.common.exception.dto.ResDTO;
import com.baro.challenges.common.exception.dto.ResponseCode;
import com.baro.challenges.user.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 회원가입, 로그인, API 문서 요청은 필터링 대상에서 제외
        String path = request.getRequestURI();
        if (path.equals("/api/auth/sign-up") || path.equals("/api/auth/sign-in")
                || path.contains("/v3/api-docs") || path.contains("/springdoc")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더 누락 또는 잘못된 형식");
            // JSON 형식으로 에러 메시지 반환
            sendErrorResponse(response, ResponseCode.TOKEN_NOT_FOUND);
            return;
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거

        try {
            if (jwtUtil.isExpired(token)) {
                log.warn("만료된 토큰");
                // JSON 형식으로 에러 메시지 반환
                sendErrorResponse(response, ResponseCode.INVALID_TOKEN);
                return;
            }

            Claims claims = Jwts.parser()
                    .verifyWith(jwtUtil.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long userId = claims.get("userId", Long.class);
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            if (userId == null || username == null || role == null) {
                log.warn("JWT 내 필수 클레임 누락");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }

            UserEntity userEntity = UserEntity.builder()
                    .userId(userId)
                    .username(username)
                    .role(UserEntity.Role.valueOf(role))
                    .build();

            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = CustomUserDetails.of(userEntity);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("JWT 인증 처리 중 예외 발생", e);
            // JSON 형식으로 에러 메시지 반환
            sendErrorResponse(response, ResponseCode.INVALID_TOKEN);
            return;
        }

        //다음 필터로 넘김
        filterChain.doFilter(request, response);
    }

    // JSON 형식으로 에러 메시지를 반환하는 공통 메서드
    private void sendErrorResponse(HttpServletResponse response, ResponseCode code) throws IOException {
        ResDTO<Object> body = ResDTO.fail(code);
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }
}
