package com.paygrid.wallet.service;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;

import java.util.List;
import java.util.UUID;

public interface WalletService {
    void createWallet(UserRegisteredEvent event);
    void createWallet(WalletCreateRequest request);
    public WalletDto getWallet(UUID walletId);
    public List<WalletDto> getWalletByUserId(UUID userId);
    void deleteWallet(UUID walletId);

}
