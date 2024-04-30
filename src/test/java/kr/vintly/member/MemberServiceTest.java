package kr.vintly.member;

import kr.vintly.Entity.Member;
import kr.vintly.member.model.req.ReqJoinDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void beforeJoin(){
        Member member = Member.builder()
                .memberId("hong11")
                .pw(bCryptPasswordEncoder.encode("test"))
                .name("홍길동")
                .address("서울시")
                .detailAddress("광화문")
                .nickname("도둑")
                .birth(java.sql.Date .valueOf("2000-06-22"))
                .email("hong11@naver.com")
                .gender("M")
                .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원가입된 ID, email, nickname 는 조회되고 가입되지 않으 정보는 조회되지 않아야 한다.")
    @Transactional
    void testGetChk(){
        // 회원가입된 ID, email, nickname  카운트 : 1
        Assertions.assertThat(memberRepository.countByMemberId("hong11")).isEqualTo(1);
        Assertions.assertThat(memberRepository.countByEmail("hong11@naver.com")).isEqualTo(1);
        Assertions.assertThat(memberRepository.countByNickname("도둑")).isEqualTo(1);

        // 회원가입 되지 않은 ID, email, nickname 카운트 : 0
        Assertions.assertThat(memberRepository.countByMemberId("hong22")).isEqualTo(0);
        Assertions.assertThat(memberRepository.countByEmail("hong333@naver.com")).isEqualTo(0);
        Assertions.assertThat(memberRepository.countByNickname("강도")).isEqualTo(0);
    }

    @Test
    @DisplayName("메일인증 처리")
    @Transactional
    void testEnableMember(){

        Member member = memberRepository.findByMemberId("hong11").get();
        String emailCode = member.getEmailCode();

        // emailCode 랑 ID가 같으면 인증
        Assertions.assertThat(memberRepository.countByMemberIdAndEmailCode(member.getMemberId(), member.getEmailCode()))
                .isEqualTo(1);

        member.enableMember();
        memberRepository.save(member);

        // emailCode 랑 ID가 같지 않음
        Assertions.assertThat(memberRepository.countByMemberIdAndEmailCode(member.getMemberId(), "11111"))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("ID 찾기")
    @Transactional
    void testFindId(){
        // 존재하는 회원정보
        List<Member> memberList = memberRepository.findByNameAndBirth("홍길동", java.sql.Date .valueOf("2000-06-22"));
        Assertions.assertThat(memberList.size()).isEqualTo(1);

        // ID 뒷자리 3개 ***로 블라인드 처리 확인
        List<String> strList = new ArrayList<>();
        strList.add("hon***");

        Assertions.assertThat(memberList.stream()
                .map(m -> m.getMemberId().substring(0, m.getMemberId().length()-3) + "***")
                .collect(Collectors.toList()))
                .isEqualTo(strList);

        // 존재하지 않는 회원정보
        List<Member> memberList2 = memberRepository.findByNameAndBirth("홍길순", java.sql.Date .valueOf("2000-06-22"));
        Assertions.assertThat(memberList2.size()).isEqualTo(0);
    }
}