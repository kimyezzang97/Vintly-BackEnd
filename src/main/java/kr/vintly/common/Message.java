package kr.vintly.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Message {
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
