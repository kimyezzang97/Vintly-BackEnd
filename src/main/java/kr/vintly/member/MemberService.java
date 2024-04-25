package kr.vintly.member;

import jakarta.mail.MessagingException;
import kr.vintly.Entity.Member;
import kr.vintly.common.exception.member.JoinConflictException;
import kr.vintly.common.model.Message;
import kr.vintly.common.exception.StatusEnum;
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
    public void createMember(ReqJoinDTO reqJoinDTO) throws MessagingException, IOException{
        // 중복체크 확인
        if(getChkId(reqJoinDTO.getMemberId()) > 0 || getChkEmail(reqJoinDTO.getEmail()) > 0 || getChkNickname(reqJoinDTO.getNickname()) > 0 ){
            throw new JoinConflictException();
        }

        // 비밀번호 암호화
        reqJoinDTO.encPw(bCryptPasswordEncoder.encode(reqJoinDTO.getPw()));

        // email 코드 및 DB 회원정보 저장
        String code = memberRepository.save(reqJoinDTO.toEntity()).getEmailCode();

        // 인증메일 발송
        mailSend(reqJoinDTO, code);
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

    // 메일 인증 처리
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> enableMember(String memberId, String emailCode){
        int chk = memberRepository.countByMemberIdAndEmailCode(memberId, emailCode);
        if(chk < 1){
            notExistMember();

        } else {
            Member member = memberRepository.findByMemberIdAndEmailCode(memberId, emailCode);
            member.enableMember();
            memberRepository.save(member);
        }

        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.OK)
                        .message("이메일 인증에 성공하였습니다. 로그인 후 사용 해주세요.")
                        .data("")
                        .build()
                , HttpStatus.OK);
    }

    private ResponseEntity<Message> notExistMember(){
        return new ResponseEntity<>(
                Message.builder()
                        .status(StatusEnum.BAD_REQUEST)
                        .message("ID가 존재하지 않습니다. 회원가입을 해주세요.")
                        .data("")
                        .build()
                , HttpStatus.OK);
    }
}
