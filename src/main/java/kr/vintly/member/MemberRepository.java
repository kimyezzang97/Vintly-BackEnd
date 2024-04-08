package kr.vintly.member;

import kr.vintly.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // ID 중복 확인
    Integer countByMemberId(String memberId);

    // email 중복 확인
    Integer countByEmail(String email);

    // 닉네임 중복 체크
    Integer countByNickname(String nickname);

    // 아이디, 인증코드 체크
    Integer countByMemberIdAndEmailCode(String memberId, String emailCode);

    // Member 엔티티 가져오기
    Member findByMemberIdAndEmailCode(String memberId, String emailCode);
}
