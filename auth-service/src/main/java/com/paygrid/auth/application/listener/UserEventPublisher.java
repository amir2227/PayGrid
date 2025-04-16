package com.paygrid.auth.application.listener;

import com.paygrid.auth.domain.event.UserRegisteredEvent;
import com.paygrid.auth.infrastructure.messaging.RabbitUserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {
    private final RabbitUserEventPublisher rabbitPublisher;

    @EventListener
    @Async
    public void onUserRegistered(UserRegisteredEvent event) {
        rabbitPublisher.publish(event);
    }
}
