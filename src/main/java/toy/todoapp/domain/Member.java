package toy.todoapp.domain;

import lombok.Data;

@Data
public class Member {
    private Long memberId;
    private String username;
    private String email;
    private String password;

    public static Member createMember(String username, String email, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(password);
        return member;
    }
}
