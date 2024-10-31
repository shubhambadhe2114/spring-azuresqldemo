package com.example.springboot_azuresql.controller;

import com.example.springboot_azuresql.entity.TransactionDetails;
import com.example.springboot_azuresql.repository.TransactionRepository;
import com.example.springboot_azuresql.service.FraudDetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private FraudDetectionService fraudService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<String> processTransaction(@RequestBody TransactionDetails transactionDetails) {
        log.info("Processing transaction {}", transactionDetails);
        try {
            boolean isFraudulent = fraudService.analyzeTransaction(transactionDetails);
            log.info("Transaction id {} is Fraudulent {}", transactionDetails.getId(), isFraudulent);
            if (isFraudulent) {
                log.error("Fraudulent detected for transaction {}", transactionDetails.getId());
                return ResponseEntity.status(403).body("Transaction flagged as fraudulent");
            }
            return ResponseEntity.ok("Transaction processed successfully");
        } catch (Exception e) {
            log.error("Error processing transaction {}", transactionDetails, e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<String> getTransactions(@RequestParam Long id) {
        log.info("Getting transactions with id {}", id);
        try {
            List<TransactionDetails> transactionDetails = transactionRepository.findByIdIn(Collections.singletonList(id));

            return ResponseEntity.ok(transactionDetails.get(0).toString());
        } catch (Exception e) {
            log.error("Error getting transaction. ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
