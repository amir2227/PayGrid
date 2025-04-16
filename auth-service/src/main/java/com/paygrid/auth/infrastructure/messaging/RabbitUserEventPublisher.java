package com.paygrid.auth.infrastructure.messaging;

import com.paygrid.auth.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitUserEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(UserRegisteredEvent event){
        rabbitTemplate.convertAndSend(RabbitConfig.USER_REGISTERED_EXCHANGE,"user.registered.created", event,msg->{
            msg.getMessageProperties().getHeaders().remove("__TypeId__");
            return msg;
        });
    }
}
