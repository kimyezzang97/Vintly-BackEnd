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
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    // ID 중복 확인
    @GetMapping("/id/{id}")
    public ResponseEntity<Message> getChkId(@PathVariable("id") String id){
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
    public ResponseEntity<Message> getChkEmail(@PathVariable("email") String email){
        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message("")
                        .data(memberService.getChkEmail(email))
                        .build()
                , HttpStatus.OK);
    }


    // nickname 중복 체크
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Message> getChkNickname(@PathVariable("nickname") String nickname){
        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message("")
                        .data(memberService.getChkNickname(nickname))
                        .build()
                , HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> createUser(@Valid @RequestBody ReqJoinDTO reqJoinDTO) throws MessagingException, IOException {
        return memberService.createMember(reqJoinDTO);
    }
}
