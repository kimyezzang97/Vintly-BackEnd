package kr.vintly.common.model;

import kr.vintly.common.exception.StatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Message { // Response Message
    private StatusEnum status;
    private String message;
    private Object data;

    @Builder
    public Message(StatusEnum status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
