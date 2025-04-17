package com.paygrid.wallet.service.impl;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.WalletStatus;
import com.paygrid.wallet.exception.BadRequestException;
import com.paygrid.wallet.exception.DuplicatedException;
import com.paygrid.wallet.exception.NotFoundException;
import com.paygrid.wallet.mapper.WalletMapper;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;
import com.paygrid.wallet.repository.WalletRepository;
import com.paygrid.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    @Override
    public void createWallet(UserRegisteredEvent event) {
        validateDuplicated(event.getUserId(), CurrencyCode.USD);
        Wallet wallet = walletMapper.toEntity(event);
        walletRepository.save(wallet);
    }
    public void createWallet(WalletCreateRequest request){
        validateDuplicated(request.getUserId(), request.getCurrencyCode());
        Wallet wallet = walletMapper.toEntity(request);
        walletRepository.save(wallet);
    }

    @Override
    public WalletDto getWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new NotFoundException("Wallet not found with id: " + walletId));
        return walletMapper.toDTO(wallet);
    }

    @Override
    public List<WalletDto> getWalletByUserId(UUID userId) {
        return walletMapper.toDTOList(walletRepository.findAllByUserIdAndStatusDeletedNot(userId));
    }

    @Override
    public void deleteWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new NotFoundException("Wallet not found with id: " + walletId));
        if (wallet.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0
                || wallet.getPendingBalance().compareTo(BigDecimal.ZERO) > 0
                || wallet.getFrozenBalance().compareTo(BigDecimal.ZERO) > 0)
            throw new BadRequestException("Cannot delete wallet with balance");
        wallet.setStatus(WalletStatus.DELETED);
        walletRepository.save(wallet);
    }

    private void validateDuplicated(UUID userId, CurrencyCode currencyCode) {
        if (walletRepository.existsByUserIdAndCurrencyAndStatusDeletedNot(userId, currencyCode)) {
            throw new DuplicatedException("Wallet already exists for user: " + userId);
        }
    }
}
