package toy.todoapp.repository;

import toy.todoapp.domain.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);

    Optional<Todo> findById(Long todoId);

    List<Todo> findAll(Long memberId);

    int update(Long todoId, TodoUpdateDto updateDto);

    int deleteById(Long todoId);
}
