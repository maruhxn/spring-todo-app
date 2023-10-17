package toy.todoapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import toy.todoapp.config.SessionConst;
import toy.todoapp.domain.Member;
import toy.todoapp.domain.Todo;
import toy.todoapp.domain.TodoStatus;
import toy.todoapp.repository.TodoUpdateDto;
import toy.todoapp.service.CreateTodoDto;
import toy.todoapp.service.TodoService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {


    private final TodoService todoService;

    @ModelAttribute("statusList")
    public TodoStatus[] status() {
        return TodoStatus.values();
    }

    @GetMapping("")
    public String todoListView(Model model) {
        List<Todo> todos = todoService.findTodos();
        model.addAttribute("todos", todos);
        return "to-do/to-doList";
    }

    @GetMapping("/create")
    public String createTodoView(@ModelAttribute("dto") CreateTodoDto dto) {
        return "to-do/to-doCreateForm";
    }

    @PostMapping("")
    public String createTodo(@Valid @ModelAttribute CreateTodoDto dto,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
    ) {
        if (bindingResult.hasErrors()) {
            return "to-do/to-doList";
        }

        dto.setMemberId(loginMember.getMemberId());

        Todo todo = todoService.createTodo(dto);

        log.info("Add TODO={}", todo);

        return "redirect:/todos";
    }

    @GetMapping("/{todoId}")
    public String todoDetailView(@PathVariable Long todoId, Model model) {
        Todo todo = todoService.findOne(todoId).get();
        model.addAttribute("todo", todo);
        return "to-do/to-doDetail";
    }

    @PutMapping("/{todoId}")
    public String updateTodo(@PathVariable Long todoId,
                             @Valid @ModelAttribute TodoUpdateDto dto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "to-do/to-doList";
        }

        todoService.updateTodo(todoId, dto);

        return "redirect:/todos/{todoId}";
    }

    @DeleteMapping("/{todoId}")
    public String deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);

        log.info("Delete TODO, id={}", todoId);

        return "redirect:/todos";
    }

    @GetMapping("/{todoId}/edit")
    public String updateTodoView(@PathVariable Long todoId, Model model) {
        Todo todo = todoService.findOne(todoId).get();
        model.addAttribute("todo", todo);
        return "to-do/to-doUpdateForm";
    }
}
