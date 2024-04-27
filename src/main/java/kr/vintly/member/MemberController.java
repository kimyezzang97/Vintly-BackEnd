package kr.vintly.member;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import kr.vintly.common.exception.member.JoinConflictException;
import kr.vintly.common.model.Message;
import kr.vintly.common.exception.StatusEnum;
import kr.vintly.member.model.req.ReqJoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;

@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    // ID 중복 확인
    @GetMapping("/id/{id}")
    //@ResponseStatus(HttpStatus.OK) HTTP STATUS 예시
    public ResponseEntity<?> getChkId(@PathVariable("id") String id){
        //HttpHeaders headers=new HttpHeaders(); headers.add("Custom-Header","bar");
        //return new ResponseEntity<>(memberService.getChkId(id), headers, HttpStatus.OK); // 헤더까지 수정 가능
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkId(id)).build());
    }

    // email 중복 체크
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getChkEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkEmail(email))
                .build());
    }


    // nickname 중복 체크
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> getChkNickname(@PathVariable("nickname") String nickname){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkNickname(nickname))
                .build());
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> createMember(@Valid @RequestBody ReqJoinDTO reqJoinDTO) throws MessagingException, IOException {
        memberService.createMember(reqJoinDTO);

        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message(reqJoinDTO.getMemberId() + " ID로 회원가입을 성공하였습니다.")
                .build());
    }

    // 회원가입 인증
    @GetMapping("/enable")
    public String enableMember(@RequestParam String id, @RequestParam String emailCode)  {
        memberService.enableMember(id, emailCode);
        return "이메일 인증에 성공하였습니다. 로그인 후 사용 해주세요.";
    }

    // ID 찾기
    @GetMapping("/find/id")
    public ResponseEntity<?> findId(@RequestParam String name, @RequestParam Date birth){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .data(memberService.findId(name, birth))
                .message("")
                .build());
    }

}
