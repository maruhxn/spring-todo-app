package toy.todoapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Todo;
import toy.todoapp.domain.TodoStatus;
import toy.todoapp.repository.MemoryTodoRepository;
import toy.todoapp.repository.TodoRepository;
import toy.todoapp.repository.TodoUpdateDto;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @AfterEach
    void afterEach() {
        if (todoRepository instanceof MemoryTodoRepository) {
            ((MemoryTodoRepository) todoRepository).clearStore();
        }
    }

    @Test
    void createTodo() {
        // Given
        CreateTodoDto dto = new CreateTodoDto(1L, "test");

        // When
        Todo savedTodo = todoService.createTodo(dto);

        // Then
        Todo findTodo = todoService.findOne(savedTodo.getTodoId()).get();
        assertThat(savedTodo).isEqualTo(findTodo);
    }

    @Test
    void findAll() {
        // Given
        CreateTodoDto dto1 = new CreateTodoDto(1L, "test");
        CreateTodoDto dto2 = new CreateTodoDto(1L, "test2");

        Todo todo1 = todoService.createTodo(dto1);
        Todo todo2 = todoService.createTodo(dto2);
        // When
        List<Todo> todos = todoService.findTodos();

        // Then
        assertThat(todos).containsExactly(new Todo[]{todo1, todo2});
    }

    @Test
    void update() {
        // Given
        CreateTodoDto dto = new CreateTodoDto(1L, "test");

        Todo savedTodo = todoService.createTodo(dto);

        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("update!", TodoStatus.COMPLETED);
        // When
        todoService.updateTodo(savedTodo.getTodoId(), todoUpdateDto);

        // Then
        Todo findTodo = todoService.findOne(savedTodo.getTodoId()).get();
        assertThat(findTodo.getContent()).isEqualTo(todoUpdateDto.getContent());
        assertThat(findTodo.getStatus()).isEqualTo(todoUpdateDto.getStatus());
    }

    @Test
    void deleteTodo() {
        // Given
        List<Todo> todos = todoService.findTodos();
        CreateTodoDto dto = new CreateTodoDto(1L, "test");

        Todo savedTodo = todoService.createTodo(dto);
        // When
        todoService.deleteTodo(savedTodo.getTodoId());

        // Then
        assertThatThrownBy(() -> todoService.findOne(savedTodo.getTodoId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}