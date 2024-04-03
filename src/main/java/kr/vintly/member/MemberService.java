package kr.vintly.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
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
}
