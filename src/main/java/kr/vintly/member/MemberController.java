package kr.vintly.member;

import kr.vintly.common.Message;
import kr.vintly.common.StatusEnum;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
