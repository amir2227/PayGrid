package com.paygrid.wallet.mapper;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.dto.WalletMetadataDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.WalletMetadata;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import com.paygrid.wallet.entity.vo.WalletStatus;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;
import com.paygrid.wallet.messaging.event.WalletBalanceChangedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class WalletMapper {

    public WalletDto toDTO(Wallet wallet) {
        if (wallet.getMetadata() != null) {

        }
        return WalletDto.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .currency(wallet.getCurrency())
                .currencyType(wallet.getType())
                .available(wallet.getAvailableBalance())
                .frozen(wallet.getFrozenBalance())
                .status(wallet.getStatus())
                .metadata(wallet.getMetadata() != null ?
                        WalletMetadataDto.builder()
                                .address(wallet.getMetadata().getAddress())
                                .network(wallet.getMetadata().getNetwork())
                                .externalReference(wallet.getMetadata().getExternalReference())
                                .memoOrTag(wallet.getMetadata().getMemoOrTag())
                                .custodial(wallet.getMetadata().getCustodial())
                                .build() : null)
                .build();
    }

    public List<WalletDto> toDTOList(List<Wallet> wallets) {
        return wallets.stream().map(this::toDTO).toList();
    }

    public Wallet toEntity(UUID userId,WalletCreateRequest walletDto) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setCurrency(walletDto.getCurrencyCode());
        wallet.setType(walletDto.getCurrencyType());
        wallet.setStatus(WalletStatus.ACTIVE);
        if (walletDto.getCurrencyType().equals(CurrencyType.CRYPTO)) {
            WalletMetadata metadata = new WalletMetadata();
            metadata.setAddress(walletDto.getAddress());
            metadata.setNetwork(walletDto.getNetwork());
            metadata.setExternalReference(walletDto.getExternalReference());
            metadata.setMemoOrTag(walletDto.getMemoOrTag());
            metadata.setCustodial(walletDto.getCustodial());
            metadata.setWallet(wallet);
            wallet.setMetadata(metadata);
        }
        return wallet;
    }

    public Wallet toEntity(UserRegisteredEvent event) {
        Wallet wallet = new Wallet();
        wallet.setUserId(event.getUserId());
        wallet.setCurrency(CurrencyCode.USD);
        wallet.setType(CurrencyType.FIAT);
        wallet.setStatus(WalletStatus.ACTIVE);
        return wallet;
    }

    public WalletBalanceChangedEvent toEvent(Wallet wallet, String operationType, BigDecimal amount) {
        return WalletBalanceChangedEvent.builder()
                .walletId(wallet.getId())
                .userId(wallet.getUserId())
                .currencyCode(wallet.getCurrency())
                .currencyType(wallet.getType())
                .availableBalance(wallet.getAvailableBalance())
                .frozenBalance(wallet.getFrozenBalance())
                .pendingBalance(wallet.getPendingBalance())
                .amount(amount)
                .operationType(operationType)
                .timestamp(Instant.now())
                .build();
    }
}
