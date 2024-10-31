package com.example.springboot_azuresql.service;

import com.example.springboot_azuresql.entity.TransactionDetails;
import com.example.springboot_azuresql.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class FraudDetectionService {

    private static final Logger log = LoggerFactory.getLogger(FraudDetectionService.class);
    @Autowired
    private TransactionRepository transactionRepository;

    public boolean analyzeTransaction(TransactionDetails transactionDetails) throws IllegalArgumentException {
        log.info("Analysing transaction");
        if(!StringUtils.hasText(transactionDetails.getSourceAccount()) || !StringUtils.hasText(transactionDetails.getDestinationAccount())) {
            log.error("Source/Destination account is empty");
            throw new IllegalArgumentException("Source/Destination account is empty");
        }
        // Simple rule: Any transaction over $10,000 is considered suspicious
        if(transactionDetails.getAmount().compareTo(new BigDecimal("10000")) <= 0) {
            log.info("Transaction is genuine");
            transactionDetails.setSuspicious(false);
            transactionRepository.save(transactionDetails);
            return false;
        }
        log.info("Transaction is suspicious");
        return true;
    }

}

