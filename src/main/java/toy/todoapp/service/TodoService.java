package toy.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import toy.todoapp.domain.Todo;
import toy.todoapp.repository.TodoRepository;
import toy.todoapp.repository.TodoUpdateDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public Todo createTodo(CreateTodoDto dto) {
        Todo todo = Todo.createTodo(dto.getMemberId(), dto.getContent());
        try {
            todoRepository.save(todo);
            return todo;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("잘못된 유저 정보입니다.");
        }
    }

    public Todo findOne(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(() -> new NoSuchElementException("해당 TODO는 존재하지 않습니다."));
    }

    public List<Todo> findTodos(Long memberId) {
        return todoRepository.findAll(memberId);
    }

    public void updateTodo(Long todoId, TodoUpdateDto updateDto) {
        int rowCnt = todoRepository.update(todoId, updateDto);
        if (rowCnt == 0) {
            throw new NoSuchElementException("해당 TODO는 존재하지 않습니다.");
        }
    }

    public void deleteTodo(Long todoId) {
        int rowCnt = todoRepository.deleteById(todoId);
        if (rowCnt == 0) {
            throw new NoSuchElementException("해당 TODO는 존재하지 않습니다.");
        }
    }
}
