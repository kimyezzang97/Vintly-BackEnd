package kr.vintly.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Entity(name = "member")
@Getter
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "member_id")
    private String memberId;

    private String pw;

    private String name;

    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    private String nickname;

    private Date birth;

    private String email;

    private String gender;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "reg_date")
    @CreatedDate
    private Timestamp regDate;

    @Column(name = "pw_date")
    @CreatedDate
    private Timestamp pwDate;

    @Column(name = "del_date")
    private Timestamp delDate;

    @Column(name = "email_code")
    private String emailCode;

    @Column(name = "email_ex_date")
    @CreatedDate()
    private Timestamp emailExDate;

    @Builder
    public Member(String memberId, String pw, String name, String address,
                     String detailAddress, String nickname, Date birth, String email,
                     String gender, String emailCode){
        this.memberId = memberId;
        this.pw = pw;
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.nickname = nickname;
        this.birth = birth;
        this.email = email;
        this.gender = gender;
        this.useYn = "K"; // 이메일 인증 대기
        this.emailCode = String.valueOf(ThreadLocalRandom.current().nextInt(10000,1000000)); // 이메일코드 6자리 생성;
        this.emailExDate = Timestamp.valueOf(LocalDateTime.now().plusDays(3)); //만든 날짜 보다 3일 후 입력
    }
}
