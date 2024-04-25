package kr.vintly.member;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import kr.vintly.common.model.Message;
import kr.vintly.common.model.StatusEnum;
import kr.vintly.member.model.req.ReqJoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
        // return new ResponseEntity<>(message, headers, HttpStatus.OK); // 헤더까지 수정 가능
        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message("")
                        .data(memberService.getChkId(id))
                        .build()
                , HttpStatus.OK);
    }

    // email 중복 체크
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getChkEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(
                Message.builder()
                .status(StatusEnum.OK)
                .data(memberService.getChkEmail(email))
                .build()
        );
    }


    // nickname 중복 체크
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> getChkNickname(@PathVariable("nickname") String nickname){
        return ResponseEntity.ok(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message("")
                        .data(memberService.getChkNickname(nickname))
                        .build()
        );
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> createMember(@Valid @RequestBody ReqJoinDTO reqJoinDTO) throws MessagingException, IOException {
        return memberService.createMember(reqJoinDTO);
    }

    // 회원가입 인증
    @GetMapping("/enable")
    public String enableMember(@RequestParam String id, @RequestParam String emailCode)  {
        Message message = (Message) memberService.enableMember(id, emailCode).getBody();
        return message.getMessage();
        //return memberService.enableMember(id, emailCode);
    }
}
