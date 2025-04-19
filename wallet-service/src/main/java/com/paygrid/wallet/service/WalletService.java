package com.paygrid.wallet.service;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface WalletService {
    void createWallet(UserRegisteredEvent event);
    Wallet createWallet(UUID userId, WalletCreateRequest request);
    public WalletDto getWallet(UUID userId,UUID walletId);
    public List<WalletDto> getWalletByUserId(UUID userId);
    void deleteWallet(UUID userId, UUID walletId);

    void freeze(UUID userId, UUID walletId, BigDecimal amount);
    void unfreeze(UUID userId,UUID walletId, BigDecimal amount);
    void credit(UUID userId,UUID walletId, BigDecimal amount);
    void debit(UUID userId,UUID walletId, BigDecimal amount);
    void confirmFrozenDebit(UUID userId,UUID walletId, BigDecimal amount);

}
