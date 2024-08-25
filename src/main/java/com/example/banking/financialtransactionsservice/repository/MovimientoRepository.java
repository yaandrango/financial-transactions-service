package com.example.banking.financialtransactionsservice.repository;

import com.example.banking.financialtransactionsservice.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}

