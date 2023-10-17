package toy.todoapp.repository;

import toy.todoapp.domain.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);

    Optional<Todo> findById(Long id);

    List<Todo> findAll();

    void update(Long todoId, TodoUpdateDto updateDto);

    void deleteById(Long todoId);
}