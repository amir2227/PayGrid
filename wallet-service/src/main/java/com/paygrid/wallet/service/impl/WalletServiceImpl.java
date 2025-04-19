package com.paygrid.wallet.service.impl;

import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.WalletStatus;
import com.paygrid.wallet.exception.BadRequestException;
import com.paygrid.wallet.exception.DuplicatedException;
import com.paygrid.wallet.exception.ForbiddenException;
import com.paygrid.wallet.exception.NotFoundException;
import com.paygrid.wallet.mapper.WalletMapper;
import com.paygrid.wallet.messaging.event.UserRegisteredEvent;
import com.paygrid.wallet.messaging.event.WalletBalanceChangedEvent;
import com.paygrid.wallet.messaging.produser.RabbitProducer;
import com.paygrid.wallet.repository.WalletRepository;
import com.paygrid.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final RabbitProducer rabbitProducer;
    private final WalletMapper walletMapper;
    @Override
    @Transactional
    public void createWallet(UserRegisteredEvent event) {
        validateDuplicated(event.getUserId(), CurrencyCode.USD);
        Wallet wallet = walletMapper.toEntity(event);
        walletRepository.save(wallet);
    }
    @Override
    @Transactional
    public Wallet createWallet(UUID userId,WalletCreateRequest request){
        validateDuplicated(userId, request.getCurrencyCode());
        Wallet wallet = walletMapper.toEntity(userId,request);
       return walletRepository.save(wallet);
    }

    @Override
    public WalletDto getWallet(UUID userId,UUID walletId) {
        Wallet wallet = getOrThrow(walletId);
        validateAccess(userId, wallet);
        return walletMapper.toDTO(wallet);
    }

    @Override
    public List<WalletDto> getWalletByUserId(UUID userId) {
        return walletMapper.toDTOList(walletRepository.findAllByUserIdAndStatusNot(userId, WalletStatus.DELETED));
    }

    @Override
    @Transactional
    public void deleteWallet(UUID userId,UUID walletId) {
        Wallet wallet = getOrThrow(walletId);
        validateAccess(userId, wallet);
        if (wallet.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0
                || wallet.getPendingBalance().compareTo(BigDecimal.ZERO) > 0
                || wallet.getFrozenBalance().compareTo(BigDecimal.ZERO) > 0)
            throw new BadRequestException("Cannot delete wallet with balance");
        wallet.setStatus(WalletStatus.DELETED);
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void freeze(UUID userId,UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdAndStatusNot(walletId, WalletStatus.DELETED)
                        .orElseThrow(() -> new NotFoundException("Wallet not found with id: " + walletId));
        validateAccess(userId, wallet);
        wallet.freeze(amount);
        walletRepository.save(wallet);
        WalletBalanceChangedEvent event = walletMapper.toEvent(wallet,"FREEZE",amount);
        rabbitProducer.publishWalletBalanceChange(event);
    }

    @Override
    @Transactional
    public void unfreeze(UUID userId,UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdAndStatusNot(walletId, WalletStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Wallet not found with id: " + walletId));
        validateAccess(userId, wallet);
        wallet.unfreeze(amount);
        walletRepository.save(wallet);
        WalletBalanceChangedEvent event = walletMapper.toEvent(wallet,"UNFREEZE",amount);
        rabbitProducer.publishWalletBalanceChange(event);
    }

    @Override
    @Transactional
    public void credit(UUID userId,UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdAndStatusNot(walletId, WalletStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Wallet not found with id: " + walletId));
        validateAccess(userId, wallet);
        wallet.credit(amount);
        walletRepository.save(wallet);
        WalletBalanceChangedEvent event = walletMapper.toEvent(wallet,"CREDIT",amount);
        rabbitProducer.publishWalletBalanceChange(event);
    }

    @Override
    @Transactional
    public void debit(UUID userId,UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdAndStatusNot(walletId, WalletStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Wallet not found with id: " + walletId));
        validateAccess(userId, wallet);
        wallet.debit(amount);
        walletRepository.save(wallet);
        WalletBalanceChangedEvent event = walletMapper.toEvent(wallet,"DEBIT",amount);
        rabbitProducer.publishWalletBalanceChange(event);
    }

    @Override
    @Transactional
    public void confirmFrozenDebit(UUID userId,UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdAndStatusNot(walletId, WalletStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Wallet not found with id: " + walletId));
        validateAccess(userId, wallet);
        wallet.confirmFrozenDebit(amount);
        walletRepository.save(wallet);
        WalletBalanceChangedEvent event = walletMapper.toEvent(wallet,"CONFIRM_FROZEN_DEBIT",amount);
        rabbitProducer.publishWalletBalanceChange(event);
    }

    private Wallet getOrThrow(UUID walletId){
        return walletRepository.findById(walletId)
               .orElseThrow(()-> new NotFoundException("Wallet not found with id: " + walletId));
    }

    private void validateDuplicated(UUID userId, CurrencyCode currencyCode) {
        if (walletRepository.existsByUserIdAndCurrencyAndStatusNot(userId, currencyCode, WalletStatus.DELETED)) {
            throw new DuplicatedException("Wallet already exists for user: " + userId);
        }
    }
    private void validateAccess(UUID userId, Wallet wallet){
        if (!wallet.getUserId().equals(userId))
            throw new ForbiddenException("Forbidden access  to this wallet");
    }
}
