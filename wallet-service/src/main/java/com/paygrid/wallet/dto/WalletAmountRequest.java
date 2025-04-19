package com.paygrid.wallet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletAmountRequest {
    @NotNull
    private BigDecimal amount;
}
