package com.paygrid.wallet.messaging.event;

import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceChangedEvent implements Serializable {
    private UUID walletId;
    private UUID userId;
    private CurrencyCode currencyCode;
    private CurrencyType currencyType;
    private BigDecimal availableBalance;
    private BigDecimal frozenBalance;
    private BigDecimal pendingBalance;
    private String operationType;
    private BigDecimal amount;
    private Instant timestamp;
}
