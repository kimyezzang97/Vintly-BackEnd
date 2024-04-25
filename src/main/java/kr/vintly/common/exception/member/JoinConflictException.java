package kr.vintly.common.exception.member;

import kr.vintly.common.exception.StatusEnum;
import lombok.Getter;

/**
 * 회원가입 - 닉네임, ID, email 중복 확인
 */
@Getter
public class JoinConflictException extends RuntimeException{
    private final StatusEnum status;

    public JoinConflictException(){
        this.status = StatusEnum.JOIN_CONFLICT;
    }
}
