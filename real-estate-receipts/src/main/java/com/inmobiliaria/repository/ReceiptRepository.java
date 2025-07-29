package com.inmobiliaria.repository;

import com.inmobiliaria.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByTenantNameContainingIgnoreCase(String tenantName);
    List<Receipt> findByReceiptNumberContainingIgnoreCase(String receiptNumber);
    List<Receipt> findByOrderByDateDesc();
}
