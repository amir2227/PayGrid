package com.paygrid.wallet.messaging.produser;

import com.paygrid.wallet.messaging.config.RabbitConfig;
import com.paygrid.wallet.messaging.event.WalletBalanceChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishWalletBalanceChange(WalletBalanceChangedEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event,msg ->{
            msg.getMessageProperties().getHeaders().remove("__TypeId__");
            return msg;
        });
    }
}
