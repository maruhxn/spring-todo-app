package toy.todoapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Member;
import toy.todoapp.repository.MemberRepository;
import toy.todoapp.repository.MemoryMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clearStore();
        }
    }

    @Test
    void register() {
        // Given
        RegisterDto registerDto = new RegisterDto("member", "test@test.com", "password");

        // When
        Member savedMember = authService.register(registerDto);

        // Then
        assertThat(savedMember.getEmail()).isEqualTo(registerDto.getEmail());
    }

    @Test
    void login() {
        // Given
        RegisterDto registerDto = new RegisterDto("member", "test@test.com", "password");
        Member registerMember = authService.register(registerDto);

        LoginDto loginDto = new LoginDto("test@test.com", "password");
        // When
        Member loginMember = authService.login(loginDto);

        // Then
        assertThat(loginMember).isEqualTo(registerMember);
    }
}