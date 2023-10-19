package toy.todoapp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
//@Controller
public class CommonErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String statusMsg = status.toString();
        log.info("error! error msg={}", statusMsg);

        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(statusMsg));

        model.addAttribute("msg", statusMsg + " " + httpStatus.getReasonPhrase());
        return "error/error";
    }

}
