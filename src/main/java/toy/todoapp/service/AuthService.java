package toy.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import toy.todoapp.domain.Member;
import toy.todoapp.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    public Member register(RegisterDto dto) {
        Member member = Member.createMember(dto.getUsername(), dto.getEmail(), dto.getPassword());

        try {
            memberRepository.save(member);
            return member;
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("중복된 이름 혹은 이메일입니다");
        }
    }

    public Member login(LoginDto dto) {
        return memberRepository.findByEmail(dto.getEmail())
                .filter(m -> m.getPassword().equals(dto.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다."));
    }
}
