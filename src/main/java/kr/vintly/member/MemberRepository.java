package kr.vintly.member;

import kr.vintly.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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

    // 인증기간 지난 ID 삭제
    Integer deleteByEmailExDateBeforeAndUseYn(Timestamp today, String useYn);

    // ID 찾기
    List<Member> findByNameAndBirth(String name, Date birth);

    Optional<Member> findByMemberId(String hong11);
}
