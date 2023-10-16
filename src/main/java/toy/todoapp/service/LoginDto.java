package toy.todoapp.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;
}
