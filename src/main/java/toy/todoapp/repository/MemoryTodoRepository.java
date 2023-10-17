package toy.todoapp.repository;

import org.springframework.stereotype.Repository;
import toy.todoapp.domain.Todo;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryTodoRepository implements TodoRepository {

    private static final Map<Long, Todo> todoStore = new HashMap<>(); //static
    private static long sequence = 0L; //static

    @Override
    public Todo save(Todo todo) {
        todo.setTodoId(++sequence);
        todoStore.put(todo.getTodoId(), todo);
        return todo;
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todoStore.get(id));
    }

    @Override
    public List<Todo> findAll(Long memberId) {
        return todoStore.values().stream().
                filter(todo -> Objects.equals(todo.getMemberId(), memberId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long todoId, TodoUpdateDto updateDto) {
        Todo findTodo = findById(todoId).orElseThrow();
        findTodo.setContent(updateDto.getContent());
        findTodo.setStatus(updateDto.getStatus());
    }

    @Override
    public void deleteById(Long todoId) {
        todoStore.remove(todoId);
    }

    public void clearStore() {
        todoStore.clear();
    }


}
