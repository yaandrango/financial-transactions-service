package com.example.banking.financialtransactionsservice.repository;

import com.example.banking.financialtransactionsservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
