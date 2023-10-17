package toy.todoapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import toy.todoapp.domain.Member;
import toy.todoapp.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        Member member1 = Member.createMember("tester1", "test1@test.com", "test1");
        Member member2 = Member.createMember("tester2", "test2@test.com", "test2");
        log.info("test member1={}", member1);
        log.info("test member2={}", member2);
        memberRepository.save(member1);
        memberRepository.save(member2);
    }
}
