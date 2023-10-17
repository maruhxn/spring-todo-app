package toy.todoapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import toy.todoapp.interceptor.LoginCheckInterceptor;
import toy.todoapp.interceptor.LogoutCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/", "/auth/**");
        registry.addInterceptor(new LogoutCheckInterceptor())
                .order(2)
                .addPathPatterns("/auth/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/auth/logout");
    }
}
