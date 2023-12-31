package toy.todoapp.repository;

import toy.todoapp.domain.Todo;

import java.util.*;
import java.util.stream.Collectors;

//@Repository
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
    public Optional<Todo> findById(Long todoId) {
        return Optional.ofNullable(todoStore.get(todoId));
    }

    @Override
    public List<Todo> findAll(Long memberId) {
        return todoStore.values().stream().
                filter(todo -> Objects.equals(todo.getMemberId(), memberId))
                .collect(Collectors.toList());
    }

    @Override
    public int update(Long todoId, TodoUpdateDto updateDto) {
        Todo findTodo = findById(todoId).orElseThrow();
        findTodo.setContent(updateDto.getContent());
        findTodo.setStatus(updateDto.getStatus());
        return 1;
    }

    @Override
    public int deleteById(Long todoId) {
        todoStore.remove(todoId);
        return 1;
    }

    public void clearStore() {
        todoStore.clear();
    }


}
