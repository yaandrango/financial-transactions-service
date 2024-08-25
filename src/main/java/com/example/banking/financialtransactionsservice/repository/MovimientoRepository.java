package com.example.banking.financialtransactionsservice.repository;

import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Movimiento findFirstByCuentaNumeroCuentaOrderByFechaDesc(Integer numeroCuenta);
}

