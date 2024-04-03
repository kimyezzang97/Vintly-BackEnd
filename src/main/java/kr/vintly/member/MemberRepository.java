package kr.vintly.member;

import kr.vintly.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // ID 중복 확인
    Integer countByMemberId(String memberId);

}
