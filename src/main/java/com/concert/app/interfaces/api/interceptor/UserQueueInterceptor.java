package com.concert.app.interfaces.api.interceptor;

import com.concert.app.domain.queue.Queue;
import com.concert.app.domain.queue.QueueRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@RequiredArgsConstructor
public class UserQueueInterceptor implements HandlerInterceptor {

    private static final String QUEUE_TOKEN = "Authorization";

    private final QueueRepository queueRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(QUEUE_TOKEN);
        Queue.tokenNullCheck(token);
        Queue queue = queueRepository.findByToken(token);
        queue.checkToken();
        return true;
    }
}
