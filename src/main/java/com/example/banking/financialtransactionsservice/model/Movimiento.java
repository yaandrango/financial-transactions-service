package com.example.banking.financialtransactionsservice.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;

@Entity
@Table(name = "movimiento", schema = "public")
public class Movimiento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

}

