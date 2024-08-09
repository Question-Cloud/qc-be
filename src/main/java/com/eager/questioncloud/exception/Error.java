package com.eager.questioncloud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum Error {
    FAIL_LOGIN(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 인증 실패"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생하였습니다."),
    FAIL_SOCIAL_LOGIN(HttpStatus.UNAUTHORIZED, "소셜 로그인 실패"),
    PENDING_EMAIL_VERIFICATION(HttpStatus.FORBIDDEN, "이메일 인증이 완료되지 않은 계정입니다."),
    NOT_ACTIVE_USER(HttpStatus.FORBIDDEN, "탈퇴 혹은 정지 된 계정입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "이미 사용중인 핸드폰입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "결과가 존재하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
