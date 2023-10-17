package toy.todoapp.repository;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import toy.todoapp.domain.TodoStatus;

@Getter
@AllArgsConstructor
public class TodoUpdateDto {
    @NotEmpty
    private String content;

    private TodoStatus status;
}
