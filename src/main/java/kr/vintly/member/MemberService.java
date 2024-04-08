package kr.vintly.member;

import jakarta.mail.MessagingException;
import kr.vintly.common.model.Message;
import kr.vintly.common.model.StatusEnum;
import kr.vintly.member.model.req.ReqJoinDTO;
import kr.vintly.util.mail.MailService;
import kr.vintly.util.mail.model.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private MailService mailService;
    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
    }

    // ID 중복 체크
    public Integer getChkId(String id){
        return memberRepository.countByMemberId(id);
    }

    // email 중복 체크
    public Integer getChkEmail(String email){
        return memberRepository.countByEmail(email);
    }

    // 닉네임 중복 체크
    public Integer getChkNickname(String nickname){
        return memberRepository.countByNickname(nickname);
    }

    // 회원가입 처리
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Message> createMember(ReqJoinDTO reqJoinDTO) throws MessagingException, IOException{
        // 중복체크 확인
        if(0!=getChkId(reqJoinDTO.getMemberId()) || 0!=getChkEmail(reqJoinDTO.getEmail()) || 0!=getChkNickname(reqJoinDTO.getNickname())){
            return chkConflictMember();
        }

        // 비밀번호 암호화
        reqJoinDTO.encPw(bCryptPasswordEncoder.encode(reqJoinDTO.getPw()));

        // email 코드
        String code = memberRepository.save(reqJoinDTO.toEntity()).getEmailCode();

        // 인증메일 발송
        mailSend(reqJoinDTO,code);

        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message(reqJoinDTO.getMemberId() + " ID로 회원가입을 성공하였습니다.")
                        .data("")
                        .build()
                ,HttpStatus.OK);
    }

    // 중복체크
    private ResponseEntity<Message> chkConflictMember() {
            return new ResponseEntity<>(
                    Message.builder()
                            .status(StatusEnum.CONFLICT)
                            .message("중복확인을 다시 해주세요.")
                            .data("")
                            .build()
                    , HttpStatus.OK);
    }

    // 회원가입 인증 메일 발송
    public void mailSend(ReqJoinDTO reqJoinDTO, String code) throws MessagingException, IOException {
        MailDTO mailDTO = MailDTO.builder().address(reqJoinDTO.getEmail())
                .title("회원가입").message("회원가입 메시지").build();

        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("id", reqJoinDTO.getMemberId());
        emailValues.put("code", code);

        mailService.mailSend(mailDTO, emailValues,"join");
    }
}
