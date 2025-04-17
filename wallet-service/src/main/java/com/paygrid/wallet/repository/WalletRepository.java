package com.paygrid.wallet.repository;

import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
// existsByUserIdAndCurrency
public interface WalletRepository extends JpaRepository<Wallet,UUID> {
    Boolean existsByUserIdAndCurrencyAndStatusDeletedNot(UUID userId, CurrencyCode currency);
    List<Wallet> findAllByUserIdAndStatusDeletedNot(UUID userId);
}
