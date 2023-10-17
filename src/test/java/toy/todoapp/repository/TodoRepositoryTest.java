package toy.todoapp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Todo;
import toy.todoapp.domain.TodoStatus;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @AfterEach
    void afterEach() {
        if (todoRepository instanceof MemoryTodoRepository) {
            ((MemoryTodoRepository) todoRepository).clearStore();
        }
    }

    @Test
    void save() {
        // Given
        Todo todo = Todo.createTodo(1L, "test");

        Todo savedTodo = todoRepository.save(todo);
        // When
        Todo findTodo = todoRepository.findById(savedTodo.getTodoId()).get();

        // Then
        assertThat(findTodo).isEqualTo(savedTodo);
    }

    @Test
    void findAll() {
        // Given
        Todo todo1 = Todo.createTodo(1L, "test");
        Todo todo2 = Todo.createTodo(2L, "test2");

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        // When
        List<Todo> todos = todoRepository.findAll(1L);

        // Then
        assertThat(todos).containsExactly(new Todo[]{todo1});
        assertThat(todos).doesNotContain(todo2);
    }

    @Test
    void update() {
        // Given
        Todo todo = Todo.createTodo(1L, "test");

        Todo savedTodo = todoRepository.save(todo);

        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("update!", TodoStatus.COMPLETED);
        // When
        todoRepository.update(todo.getTodoId(), todoUpdateDto);

        // Then
        Todo findTodo = todoRepository.findById(todo.getTodoId()).get();
        assertThat(findTodo.getContent()).isEqualTo(todoUpdateDto.getContent());
        assertThat(findTodo.getStatus()).isEqualTo(todoUpdateDto.getStatus());
    }

    @Test
    void deleteById() {
        // Given
        Todo todo = Todo.createTodo(1L, "test");

        Todo savedTodo = todoRepository.save(todo);
        // When
        todoRepository.deleteById(todo.getTodoId());

        // Then
        assertThatThrownBy(() -> todoRepository.findById(savedTodo.getTodoId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}