package toy.todoapp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Member;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clearStore();
        }
    }

    @Test
    void save() {
        // Given
        Member member = new Member();
        member.setUsername("memberA");
        member.setEmail("test@test.com");
        member.setPassword("password");

        Member savedMember = memberRepository.save(member);
        // When
        Member findMember = memberRepository.findById(savedMember.getMemberId()).get();

        // Then
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void saveFailWithDuplicateUsername() {
        // Given
        Member member1 = Member.createMember("memberA", "testA@test.com", "password");
        Member member2 = Member.createMember("memberA", "testB@test.com", "password");

        Member savedMember1 = memberRepository.save(member1);

        // When

        // Then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void saveFailWithDuplicateEmail() {
        // Given
        Member member1 = Member.createMember("memberA", "test@test.com", "password");
        Member member2 = Member.createMember("memberB", "test@test.com", "password");

        Member savedMember1 = memberRepository.save(member1);

        // When

        // Then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void findByEmail() {
        // Given
        Member member = Member.createMember("memberA", "test@test.com", "password");

        Member savedMember = memberRepository.save(member);
        // When
        Member findMember = memberRepository.findByEmail("test@test.com").get();

        // Then
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void findByEmailFail() {
        // Given
        Member member = Member.createMember("memberA", "test@test.com", "password");

        memberRepository.save(member);
        // When
        assertThatThrownBy(() -> memberRepository.findByEmail("testhack@test.com").get())
                .isInstanceOf(NoSuchElementException.class);
    }
}