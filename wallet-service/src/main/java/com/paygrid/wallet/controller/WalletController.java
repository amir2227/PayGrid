package com.paygrid.wallet.controller;

import com.paygrid.wallet.dto.WalletAmountRequest;
import com.paygrid.wallet.dto.WalletCreateRequest;
import com.paygrid.wallet.dto.WalletDto;
import com.paygrid.wallet.entity.Wallet;
import com.paygrid.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.paygrid.wallet.controller.WalletController.BASE_URL;

@Tag(name = "Wallets", description = "Operations related to wallet lifecycle and balance management")
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class WalletController {
    public static final String BASE_URL = "/api/v1/wallets";

    private final WalletService walletService;
    @PostMapping
    @Operation(summary = "Create Wallet", description = "Creates a wallet for authenticated user")
    public ResponseEntity<Void> createWallet(
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletCreateRequest request) {

        Wallet wallet = walletService.createWallet(userId, request);
        return ResponseEntity.created(URI.create(BASE_URL + "/" + wallet.getId() )).build();
    }
    @GetMapping("/my")
    @Operation(summary = "Get My Wallets", description = "Fetch all wallets for authenticated user")
    public ResponseEntity<List<WalletDto>> getMyWallets(
            @RequestHeader(value = "X-User-Id") UUID userId) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }
    @Operation(summary = "Get Wallet", description = "Get a wallet by id")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable("walletId") UUID walletId,
                                               @RequestHeader(value = "X-User-Id") UUID userId) {
        return ResponseEntity.ok(walletService.getWallet(userId,walletId));
    }
    @Operation(summary = "Delete Wallet", description = "Delete a wallet by id")
    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId") UUID walletId,
                                             @RequestHeader(value = "X-User-Id") UUID userId) {
        walletService.deleteWallet(userId,walletId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{walletId}/actions/freeze")
    @Operation(summary = "Freeze amount", description = "Moves amount from available to frozen")
    public ResponseEntity<Void> freeze(
            @PathVariable("walletId") UUID walletId,
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletAmountRequest request) {
        walletService.freeze(userId,walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{walletId}/actions/unfreeze")
    @Operation(summary = "Unfreeze amount", description = "Moves amount from frozen back to available")
    public ResponseEntity<Void> unfreeze(
            @PathVariable("walletId") UUID walletId,
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletAmountRequest request) {
        walletService.unfreeze(userId,walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{walletId}/actions/credit")
    @Operation(summary = "Credit amount", description = "Adds amount to available balance")
    public ResponseEntity<Void> credit(
            @PathVariable("walletId") UUID walletId,
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletAmountRequest request) {
        walletService.credit(userId,walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{walletId}/actions/debit")
    @Operation(summary = "Debit amount", description = "Subtracts from available balance")
    public ResponseEntity<Void> debit(
            @PathVariable("walletId") UUID walletId,
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletAmountRequest request) {
        walletService.debit(userId,walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{walletId}/actions/confirm-frozen-debit")
    @Operation(summary = "Confirm frozen debit", description = "Finalizes frozen funds usage")
    public ResponseEntity<Void> confirmFrozenDebit(
            @PathVariable("walletId") UUID walletId,
            @RequestHeader(value = "X-User-Id") UUID userId,
            @RequestBody @Valid WalletAmountRequest request) {
        walletService.confirmFrozenDebit(userId,walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }
}
