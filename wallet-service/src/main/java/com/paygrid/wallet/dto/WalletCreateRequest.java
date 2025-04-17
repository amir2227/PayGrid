package com.paygrid.wallet.dto;

import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WalletCreateRequest {
    private UUID userId;
    private CurrencyType currencyType;
    private CurrencyCode currencyCode;

    // crypto specific fields
    private String address;
    private String network;
    private String memoOrTag;
    private String externalReference;
    private Boolean custodial;
}
