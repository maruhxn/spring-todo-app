package toy.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.todoapp.domain.Todo;
import toy.todoapp.repository.TodoRepository;
import toy.todoapp.repository.TodoUpdateDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public Todo createTodo(CreateTodoDto dto) {
        Todo todo = Todo.createTodo(dto.getMemberId(), dto.getContent());
        return todoRepository.save(todo);
    }

    public Optional<Todo> findOne(Long todoId) {
        return todoRepository.findById(todoId);
    }

    public List<Todo> findTodos() {
        return todoRepository.findAll();
    }

    public void updateTodo(Long todoId, TodoUpdateDto updateDto) {
        todoRepository.update(todoId, updateDto);
    }

    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}
