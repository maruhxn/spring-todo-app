package toy.todoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public String PostNotFoundException(IllegalArgumentException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("msg", e.getMessage());
        return "error/error";
    }
}
