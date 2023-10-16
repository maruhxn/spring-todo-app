package toy.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.todoapp.domain.Member;
import toy.todoapp.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    public Member register(RegisterDto dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setEmail(dto.getEmail());
        member.setPassword(dto.getPassword());

        return memberRepository.save(member);
    }

    public Member login(LoginDto dto) {
        return memberRepository.findByEmail(dto.getEmail())
                .filter(m -> m.getPassword().equals(dto.getPassword()))
                .orElse(null);
    }
}
