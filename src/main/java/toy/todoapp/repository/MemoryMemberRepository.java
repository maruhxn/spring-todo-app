package toy.todoapp.repository;

import org.springframework.stereotype.Repository;
import toy.todoapp.domain.Member;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<Long, Member> memberStore = new ConcurrentHashMap<>(); //static
    private static long sequence = 0L; //static

    @Override
    public Member save(Member member) {
        member.setMemberId(++sequence);
        memberStore.put(member.getMemberId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(memberStore.get(memberId));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return new ArrayList<>(memberStore.values()).stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst();
    }

    public void clearStore() {
        memberStore.clear();
    }
}
