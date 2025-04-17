package com.paygrid.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // Each metadata row is tied to a single wallet
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, unique = true)
    private Wallet wallet;
    @Column(name = "address")
    private String address; // crypto wallet address
    @Column(name = "network")
    private String network; // e.g., Ethereum, BSC, Tron
    @Column(name = "memo_or_tag")
    private String memoOrTag; // for networks requiring tags (like XRP, XLM)
    @Column(name = "external_reference")
    private String externalReference; // for linking with external wallet providers

    @Column(name = "is_custodial")
    private Boolean custodial; // true = held by us, false = user's own wallet

    @CreationTimestamp
    private OffsetDateTime createdAt;
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
