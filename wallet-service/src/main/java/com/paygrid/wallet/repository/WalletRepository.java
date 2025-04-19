package com.paygrid.wallet.repository;

import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.WalletStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
// existsByUserIdAndCurrency
public interface WalletRepository extends JpaRepository<Wallet,UUID> {
    Boolean existsByUserIdAndCurrencyAndStatusNot(UUID userId, CurrencyCode currency, WalletStatus status);
    List<Wallet> findAllByUserIdAndStatusNot(UUID userId,WalletStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByIdAndStatusNot(UUID id, WalletStatus status);
}
