package toy.todoapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toy.todoapp.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

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