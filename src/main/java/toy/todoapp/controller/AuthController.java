package toy.todoapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import toy.todoapp.domain.Member;
import toy.todoapp.service.AuthService;
import toy.todoapp.service.LoginDto;
import toy.todoapp.service.RegisterDto;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String registerView(@ModelAttribute RegisterDto dto) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDto dto, BindingResult bindingResult,
                           @RequestParam(defaultValue = "/") String redirectURL,
                           HttpServletRequest req
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        Member member = authService.register(dto);

        log.info("member={}", member);

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginView(@ModelAttribute LoginDto dto) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDto dto, BindingResult bindingResult,
                         @RequestParam(defaultValue = "/") String redirectURL,
                         HttpServletRequest req
    ) {
        log.info("loginDto={}", dto);
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        Member loginMember = authService.login(dto);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "auth/login";
        }

        HttpSession session = req.getSession();
        session.setAttribute("loginMember", loginMember); // 비밀번호는 빼는게 좋지 않나..?

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        log.info("로그아웃 시도");
        HttpSession session = req.getSession(false);
        if (session != null) {
            log.info("logout!");
            session.invalidate();
        }
        return "redirect:/login";
    }
}
