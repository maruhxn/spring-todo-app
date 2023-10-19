package toy.todoapp.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Todo {
    private Long todoId;
    private Long memberId;
    private String content;
    private TodoStatus status;
    private LocalDateTime createdAt;

    // 생성 메소드
    public static Todo createTodo(Long memberId, String content) {
        Todo todo = new Todo();
        todo.setMemberId(memberId);
        todo.setContent(content);
        todo.setStatus(TodoStatus.ISSUE);
//        todo.setCreatedAt(LocalDateTime.now());

        return todo;
    }
}
