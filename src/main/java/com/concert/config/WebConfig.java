package com.concert.config;

import com.concert.app.interfaces.api.interceptor.UserQueueInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserQueueInterceptor userQueueScheduler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userQueueScheduler)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/v1/api/queue/**");
    }
}
