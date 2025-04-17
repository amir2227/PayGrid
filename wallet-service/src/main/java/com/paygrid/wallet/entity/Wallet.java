package com.paygrid.wallet.entity;

import com.paygrid.wallet.entity.vo.CurrencyCode;
import com.paygrid.wallet.entity.vo.CurrencyType;
import com.paygrid.wallet.entity.vo.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "currency"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;
    @Enumerated(EnumType.STRING)
    private CurrencyType type;
    @Enumerated(EnumType.STRING)
    private WalletStatus status;
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal availableBalance = BigDecimal.ZERO;
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal frozenBalance = BigDecimal.ZERO;
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal pendingBalance = BigDecimal.ZERO;

    @OneToOne(mappedBy = "wallet",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private WalletMetadata metadata;

    @Version
    private Long version; // optimistic locking for safe concurrent updates

    @CreationTimestamp
    private OffsetDateTime createdAt;
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    public void freeze(BigDecimal amount) {
        validateAvailable(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        this.frozenBalance = this.frozenBalance.add(amount);
    }
    public void unfreeze(BigDecimal amount) {
        validateFrozen(amount);
        this.frozenBalance = this.frozenBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);

    }
    public void credit(BigDecimal amount) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.availableBalance = this.availableBalance.add(amount);
    }

    public void debit(BigDecimal amount) {
        validateAvailable(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
    }
    public void confirmFrozenDebit(BigDecimal amount) {
        validateFrozen(amount);
        this.frozenBalance = this.frozenBalance.subtract(amount);
    }

    private void validateAvailable(BigDecimal amount) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Not enough available balance");
        }
    }
    private void validateFrozen(BigDecimal amount) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (frozenBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Not enough frozen funds to release");
        }
    }
}
