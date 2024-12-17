package com.eager.questioncloud.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {
    FAIL_LOGIN(401, "아이디 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_TOKEN(401, "토큰 인증 실패"),
    INTERNAL_SERVER_ERROR(500, "오류가 발생하였습니다."),
    FAIL_SOCIAL_LOGIN(401, "소셜 로그인 실패"),
    PENDING_EMAIL_VERIFICATION(403, "이메일 인증이 완료되지 않은 계정입니다."),
    NOT_ACTIVE_USER(403, "탈퇴 혹은 정지 된 계정입니다."),
    DUPLICATE_EMAIL(409, "이미 사용중인 이메일입니다."),
    DUPLICATE_PHONE(409, "이미 사용중인 핸드폰입니다."),
    DUPLICATE_SOCIAL_UID(409, "이미 가입 된 소셜 계정입니다."),
    NOT_FOUND(404, "결과가 존재하지 않습니다."),
    BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    NOT_ENOUGH_POINT(403, "포인트가 부족합니다."),
    ALREADY_OWN_QUESTION(409, "이미 보유하고 있는 문제입니다."),
    ALREADY_REGISTER_COUPON(409, "이미 등록한 쿠폰입니다."),
    ALREADY_IN_CART(409, "이미 장바구니에 담겨 있는 문제입니다."),
    EXPIRED_COUPON(400, "등록 기간이 지난 쿠폰입니다."),
    LIMITED_COUPON(400, "쿠폰 물량이 전부 소진되었습니다."),
    WRONG_COUPON(400, "잘못된 쿠폰 사용입니다."),
    PAYMENT_ERROR(500, "결제 요청 실패"),
    ALREADY_PROCESSED_PAYMENT(409, "이미 처리된 결제입니다."),
    ALREADY_ORDERED(409, "이미 요청된 주문입니다."),
    NOT_PROCESS_PAYMENT(400, "완료된 결제가 아닙니다."),
    ALREADY_REGISTER_CREATOR(409, "이미 크리에이터입니다."),
    ALREADY_SUBSCRIBE_CREATOR(409, "이미 구독중인 크리에이터입니다."),
    NOT_PASSWORD_SUPPORT_ACCOUNT(400, "비밀번호를 사용하지 않는 계정 유형입니다."),
    ALREADY_REGISTER_REVIEW(409, "이미 후기를 남긴 문제입니다."),
    NOT_OWNED_QUESTION(400, "보유하고 있는 문제가 아닙니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    UNAVAILABLE_QUESTION(400, "현재 이용할 수 없는 문제 입니다."),
    ALREADY_VERIFIED_EMAIL(409, "이미 인증 완료 된 메일입니다."),
    FAIL_USE_COUPON(400, "쿠폰 사용에 실패하였습니다.");

    private final int httpStatus;
    private final String message;
}
