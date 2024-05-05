package kr.vintly.member.model.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kr.vintly.Entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
public class ReqJoinDTO {

    // 회원 ID
    @NotBlank(message = "ID를 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$", message = "ID는 영어와 숫자만 사용하여 5~20자로 입력해주세요.")
    private String memberId;


    // 회원 비밀번호
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&~<>])[A-Za-z\\d@$!%*#?&~<>]{8,20}$",message = "비밀번호는 영어,숫자,특수문자를" +
            " 사용하여 8~20자로 입력해주세요.")
    private String pw;

    // 회원이름
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "(^[가-힣]{2,10}$)|(^[a-zA-Z]{2,20}$)", message = "이름은 영어 혹은 한글 2~10자로 입력해주세요.")
    private String name;

    // 기본 주소
    @NotBlank(message = "기본주소를 입력해주세요.")
    @Pattern(regexp = "^.{0,200}$", message = "200자 이하로 입력하주세요.")
    private String address;

    // 상세 주소 [필수입력 아님]
    @Pattern(regexp = "^.{0,100}$", message = "100자 이하로 입력하주세요.")
    private String detailAddress;

    // 닉네임
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9_-]{1,15}$", message = "영어,한글 혹은 '-','_' 으로 1~15자로 입력해주세요.")
    private String nickname;

    // 생년월일
    @NotNull(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //@NotNull(message = "생년월일을 입력해주세요.")
    //@Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "생년월일 형식에 맞게 입력해주세요.")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 이거를 사용해도됨
    private Date birth;

    // 이메일
    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    @Pattern(regexp = "^.{0,64}$", message = "64자 이하로 입력하주세요.")
    private String email;

    // 성별 M : 남자, W : 여자
    @NotBlank(message = "성별을 체크해주세요.")
    @Pattern(regexp = "^.{0,2}$", message = "2자 이하로 입력하주세요.")
    private String gender;

    public void encPw(String encPw){
        this.pw = encPw;
    }

    public Member toEntity(){
        return Member.builder()
                .memberId(memberId)
                .pw(pw)
                .name(name)
                .address(address)
                .detailAddress(detailAddress)
                .nickname(nickname)
                .birth(birth)
                .email(email)
                .gender(gender).build();
    }
}
