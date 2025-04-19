package com.paygrid.wallet.dto;

import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import com.paygrid.wallet.entity.vo.WalletStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class WalletDto {
    private UUID id;
    private UUID userId;
    private CurrencyCode currency;
    private CurrencyType currencyType;
    private BigDecimal available;
    private BigDecimal frozen;
    private WalletStatus status;
    private WalletMetadataDto metadata;
}
