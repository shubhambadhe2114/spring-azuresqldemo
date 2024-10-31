package com.example.springboot_azuresql.repository;

import com.example.springboot_azuresql.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, Long> {
    List<TransactionDetails> findByIdIn(List<Long> ids);
}
