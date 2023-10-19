package toy.todoapp.repository;

import toy.todoapp.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    int deleteById(Long memberId);
}
