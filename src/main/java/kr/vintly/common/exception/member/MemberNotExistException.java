package kr.vintly.common.exception.member;

import kr.vintly.common.exception.StatusEnum;
import lombok.Getter;

/**
 * 로그인, 메일인증 - ID 존재 안함
 */
@Getter
public class MemberNotExistException extends RuntimeException{

    private final StatusEnum status;

    public MemberNotExistException(){
        this.status = StatusEnum.MEMBER_NOT_EXIST;
    }
}
