package kr.vintly.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor // 필수(final)변수 파라미터로 가지는 생성자 생성
@Getter
public enum StatusEnum {
    OK("OK"),

    // validation
    BAD_REQUEST(""),

    // Member
    JOIN_CONFLICT("중복 확인을 해주세요.");


    private final String message;
}
