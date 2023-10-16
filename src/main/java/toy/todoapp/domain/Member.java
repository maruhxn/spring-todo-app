package toy.todoapp.domain;

import lombok.Data;

@Data
public class Member {
    private Long memberId;
    private String username;
    private String email;
    private String password;
}
