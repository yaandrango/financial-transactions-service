package com.example.banking.financialtransactionsservice.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cuenta", schema = "public")
public class Cuenta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroCuenta;

    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
