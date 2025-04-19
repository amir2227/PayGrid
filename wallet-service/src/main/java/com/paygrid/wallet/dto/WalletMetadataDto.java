package com.paygrid.wallet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletMetadataDto {
    private String address;
    private String network;
    private String memoOrTag;
    private Boolean custodial;
    private String externalReference;
}
