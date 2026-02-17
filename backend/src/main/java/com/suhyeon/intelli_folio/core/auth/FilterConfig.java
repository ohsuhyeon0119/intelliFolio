package com.suhyeon.intelli_folio.core.auth;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthService authService;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration() {
        FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtAuthFilter(authService));
        reg.addUrlPatterns("/api/*");
        reg.setOrder(2);
        return reg;
    }

    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> loggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiLoggingFilter());
        registration.setOrder(1); // 숫자 작을수록 먼저 실행
        registration.addUrlPatterns("/*");
        return registration;
    }
}