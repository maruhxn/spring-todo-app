package toy.todoapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Member;
import toy.todoapp.domain.Todo;
import toy.todoapp.domain.TodoStatus;
import toy.todoapp.repository.MemberRepository;
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

    @Autowired
    MemberRepository memberRepository;

    Member memberA = Member.createMember("memberA", "testA@test.com", "password");

    @AfterEach
    void afterEach() {
        if (todoRepository instanceof MemoryTodoRepository) {
            ((MemoryTodoRepository) todoRepository).clearStore();
        }
    }

    @Test
    void createTodo() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto = new CreateTodoDto(member.getMemberId(), "test");

        // When
        Todo savedTodo = todoService.createTodo(dto);

        // Then
        Todo findTodo = todoService.findOne(savedTodo.getTodoId());
        assertThat(savedTodo.getTodoId()).isEqualTo(findTodo.getTodoId());
    }

    @Test
    void findAll() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto1 = new CreateTodoDto(member.getMemberId(), "test");
        CreateTodoDto dto2 = new CreateTodoDto(member.getMemberId(), "test2");

        todoService.createTodo(dto1);
        todoService.createTodo(dto2);
        // When
        List<Todo> todos = todoService.findTodos(member.getMemberId());

        // Then
        assertThat(todos.size()).isEqualTo(2);
    }

    @Test
    void update() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto = new CreateTodoDto(member.getMemberId(), "test");

        Todo savedTodo = todoService.createTodo(dto);

        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("update!", TodoStatus.COMPLETED);
        // When
        todoService.updateTodo(savedTodo.getTodoId(), todoUpdateDto);

        // Then
        Todo findTodo = todoService.findOne(savedTodo.getTodoId());
        assertThat(findTodo.getContent()).isEqualTo(todoUpdateDto.getContent());
        assertThat(findTodo.getStatus()).isEqualTo(todoUpdateDto.getStatus());
    }

    @Test
    void updateFailWithNoTodo() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto = new CreateTodoDto(member.getMemberId(), "test");

        Todo savedTodo = todoService.createTodo(dto);

        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("update!", TodoStatus.COMPLETED);
        // When


        // Then
        assertThatThrownBy(() -> todoService.updateTodo(999L, todoUpdateDto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteTodo() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto = new CreateTodoDto(member.getMemberId(), "test");

        Todo savedTodo = todoService.createTodo(dto);
        // When
        todoService.deleteTodo(savedTodo.getTodoId());

        // Then
        assertThatThrownBy(() -> todoService.findOne(savedTodo.getTodoId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteFailWithNoTodo() {
        // Given
        Member member = memberRepository.save(memberA);
        CreateTodoDto dto = new CreateTodoDto(member.getMemberId(), "test");

        todoService.createTodo(dto);
        // When

        // Then
        assertThatThrownBy(() -> todoService.deleteTodo(999L))
                .isInstanceOf(NoSuchElementException.class);
    }
}