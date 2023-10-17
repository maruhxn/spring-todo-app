package toy.todoapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {
    ISSUE("할 일"), INPROGRESS("진행 중"), COMPLETED("완료");

    private final String description;
}
