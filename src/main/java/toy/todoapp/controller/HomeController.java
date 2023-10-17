package toy.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import toy.todoapp.domain.Member;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeView(@SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {
        model.addAttribute("member", loginMember);
        return "index";
    }
}
