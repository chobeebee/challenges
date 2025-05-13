package com.baro.challenges.common.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {
    // 공통
    SUCCESS("000", "성공적으로 처리됐습니다.", HttpStatus.OK),
    NEED_LOGIN("001", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("004", "존재하지 않는 데이터입니다.", HttpStatus.NOT_FOUND),

    // User
    USER_ALREADY_EXISTS("US100", "이미 등록된 회원입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("US200", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH("US201", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_FEIGN_CLIENT_ERROR("US202", "회원 정보 조회 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("US301", "만료되었거나 유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("US302", "토큰이 DB에 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_MISMATCH("US303", "요청된 토큰이 서버에 저장된 토큰과 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 예상치 못한 예외 발생
    INTERNAL_SERVER_ERROR("999", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ResponseCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
