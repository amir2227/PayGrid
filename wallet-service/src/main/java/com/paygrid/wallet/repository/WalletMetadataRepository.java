package com.paygrid.wallet.repository;

import com.paygrid.wallet.entity.WalletMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletMetadataRepository extends JpaRepository<WalletMetadata, UUID> {
}
