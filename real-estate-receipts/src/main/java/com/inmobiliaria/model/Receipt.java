package com.inmobiliaria.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String tenantName;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String concept;
    
    @Column(nullable = false)
    private String period;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false, unique = true)
    private String receiptNumber;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
}
