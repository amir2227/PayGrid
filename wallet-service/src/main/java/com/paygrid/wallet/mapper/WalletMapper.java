package com.paygrid.wallet.mapper;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.WalletMetadata;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import com.paygrid.wallet.entity.vo.WalletStatus;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletMapper {

    public WalletDto toDTO(Wallet wallet) {
        return WalletDto.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .currency(wallet.getCurrency())
                .currencyType(wallet.getType())
                .available(wallet.getAvailableBalance())
                .frozen(wallet.getFrozenBalance())
                .status(wallet.getStatus())
                .build();
    }
    public List<WalletDto> toDTOList(List<Wallet> wallets) {
        return wallets.stream().map(this::toDTO).toList();
    }
    public Wallet toEntity(WalletCreateRequest walletDto) {
        Wallet wallet = new Wallet();
        wallet.setUserId(walletDto.getUserId());
        wallet.setCurrency(walletDto.getCurrencyCode());
        wallet.setType(walletDto.getCurrencyType());
        wallet.setStatus(WalletStatus.ACTIVE);
        if (walletDto.getCurrencyType().equals(CurrencyType.CRYPTO)){
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
}
