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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void registerFailWithDuplicateUsername() {
        // Given
        memberRepository.save(Member.createMember("member", "test1@test.com", "password"));
        RegisterDto registerDto = new RegisterDto("member", "test2@test.com", "password");

        // When

        // Then
        assertThatThrownBy(() -> authService.register(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 이름 혹은 이메일입니다");
    }

    @Test
    void registerFailWithDuplicateEmail() {
        // Given
        memberRepository.save(Member.createMember("member1", "test@test.com", "password"));
        RegisterDto registerDto = new RegisterDto("member2", "test@test.com", "password");

        // When

        // Then
        assertThatThrownBy(() -> authService.register(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 이름 혹은 이메일입니다");
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

    @Test
    void loginFailWithNoMember() {
        // Given
        RegisterDto registerDto = new RegisterDto("member", "test@test.com", "password");
        Member registerMember = authService.register(registerDto);

        LoginDto loginDto = new LoginDto("hack@test.com", "password");
        // When

        // Then
        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 혹은 비밀번호가 일치하지 않습니다.");
    }

    @Test
    void loginFailWithIncorrectPassword() {
        // Given
        RegisterDto registerDto = new RegisterDto("member", "test@test.com", "password");
        Member registerMember = authService.register(registerDto);

        LoginDto loginDto = new LoginDto("test@test.com", "hack");
        // When

        // Then
        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 혹은 비밀번호가 일치하지 않습니다.");
    }
}