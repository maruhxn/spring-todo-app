package toy.todoapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import toy.todoapp.config.SessionConst;

@Slf4j
public class LogoutCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        if (session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
            // 로그인 시도를 한 URL로 리다이렉트
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
