package toy.todoapp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import toy.todoapp.domain.Member;
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

    @Autowired
    MemberRepository memberRepository;

    Member memberA = Member.createMember("memberA", "testA@test.com", "password");
    Member memberB = Member.createMember("memberB", "testB@test.com", "password");

    @AfterEach
    void afterEach() {
        if (todoRepository instanceof MemoryTodoRepository) {
            ((MemoryTodoRepository) todoRepository).clearStore();
        }
    }

    @Test
    void save() {
        // Given
        Member member = memberRepository.save(memberA);
        Todo todo = Todo.createTodo(member.getMemberId(), "test");

        Todo savedTodo = todoRepository.save(todo);
        // When
        Todo findTodo = todoRepository.findById(savedTodo.getTodoId()).get();

        // Then
        assertThat(findTodo.getTodoId()).isEqualTo(savedTodo.getTodoId());
    }

    @Test
    void saveFailWithNoMember() {
        // Given
        Member member = memberRepository.save(memberA);
        Todo todo = Todo.createTodo(999L, "test");

        // When

        // Then
        assertThatThrownBy(() -> todoRepository.save(todo))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findAll() {
        // Given
        Member member1 = memberRepository.save(memberA);
        Member member2 = memberRepository.save(memberB);
        Todo todo1 = Todo.createTodo(member1.getMemberId(), "test1");
        Todo todo2 = Todo.createTodo(member2.getMemberId(), "test2");

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        // When
        List<Todo> todos = todoRepository.findAll(member1.getMemberId());

        // Then
        assertThat(todos.size()).isEqualTo(1);
    }

    @Test
    void update() {
        // Given
        Member member = memberRepository.save(memberA);
        Todo todo = Todo.createTodo(member.getMemberId(), "test");

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
    void updateFailWithNoTodo() {
        // Given
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("update!", TodoStatus.COMPLETED);
        // When
        todoRepository.update(999L, todoUpdateDto);

        // Then
    }

    @Test
    void deleteById() {
        // Given
        Member member = memberRepository.save(memberA);
        Todo todo = Todo.createTodo(member.getMemberId(), "test");

        Todo savedTodo = todoRepository.save(todo);
        // When
        todoRepository.deleteById(todo.getTodoId());

        // Then
        assertThatThrownBy(() -> todoRepository.findById(savedTodo.getTodoId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}