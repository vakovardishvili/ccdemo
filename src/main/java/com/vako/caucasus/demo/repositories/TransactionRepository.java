package com.vako.caucasus.demo.repositories;

import com.vako.caucasus.demo.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
}
