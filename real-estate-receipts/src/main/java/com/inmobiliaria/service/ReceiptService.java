package com.inmobiliaria.service;

import com.inmobiliaria.model.Receipt;
import com.inmobiliaria.model.User;
import com.inmobiliaria.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;
    
    public Receipt createReceipt(String tenantName, BigDecimal amount, String concept, 
                                String period, User createdBy) {
        Receipt receipt = new Receipt();
        receipt.setTenantName(tenantName);
        receipt.setAmount(amount);
        receipt.setConcept(concept);
        receipt.setPeriod(period);
        receipt.setDate(LocalDate.now());
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setCreatedBy(createdBy);
        
        return receiptRepository.save(receipt);
    }
    
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findByOrderByDateDesc();
    }
    
    public List<Receipt> searchReceipts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllReceipts();
        }
        
        List<Receipt> byTenant = receiptRepository.findByTenantNameContainingIgnoreCase(query);
        List<Receipt> byNumber = receiptRepository.findByReceiptNumberContainingIgnoreCase(query);
        
        byTenant.addAll(byNumber);
        return byTenant.stream()
                .distinct()
                .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                .toList();
    }
    
    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }
    
    private String generateReceiptNumber() {
        String prefix = "REC";
        String date = LocalDate.now().toString().replace("-", "");
        long count = receiptRepository.count() + 1;
        return prefix + date + String.format("%04d", count);
    }
}
