package kr.vintly.util.scheduled;

import kr.vintly.member.MemberRepository;
import kr.vintly.refresh.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@EnableScheduling
@Component
public class MemberScheduled {

    private MemberRepository memberRepository;

    private RefreshRepository refreshRepository;

    @Autowired
    public MemberScheduled(MemberRepository memberRepository, RefreshRepository refreshRepository){
        this.memberRepository = memberRepository;
        this.refreshRepository = refreshRepository;
    }

    // 인증기간 지난 회원 삭제
    @Transactional
    @Scheduled(cron = "0 0 17 * * *") // 매일 15시 실행
    public void deleteExpiredId(){
        int i = memberRepository.deleteByEmailExDateBeforeAndUseYn(new Timestamp(System.currentTimeMillis()),"K");
    }

    @Transactional
    @Scheduled(cron = "0 0 18 * * *") // 매일 15시 실행
    public void deleteExpiredRefreshToken(){
        boolean status = refreshRepository.deleteByExpirationBefore(new Timestamp(System.currentTimeMillis()));
    }
}
