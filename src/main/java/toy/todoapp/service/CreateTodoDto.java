package toy.todoapp.service;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTodoDto {
    private Long memberId;

    @NotEmpty
    private String content;

    public void setMemberId(@Nonnull Long memberId) {
        this.memberId = memberId;
    }
}
