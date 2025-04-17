package com.paygrid.wallet.messaging.listener;

import com.paygrid.wallet.messaging.event.UserRegisteredEvent;
import com.paygrid.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final WalletService walletService;

    @RabbitListener(queues = "user.registered.wallet")
    public void handle(UserRegisteredEvent event) {
        log.info("Received user created event for user with id: " + event.getUserId());
        walletService.createWallet(event);
    }

}
