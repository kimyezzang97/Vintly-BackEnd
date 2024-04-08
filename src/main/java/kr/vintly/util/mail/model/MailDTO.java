package kr.vintly.util.mail.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MailDTO {
    private String address;
    private String title;
    private String message;

    @Builder
    public MailDTO(String address, String title, String message){
        this.address = address;
        this.title = title;
        this.message = message;
    }
}
