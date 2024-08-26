package com.example.banking.financialtransactionsservice.exception;

import java.math.BigDecimal;

public class ErrorResponse {
    private String mensaje;
    private BigDecimal saldoDisponible;

    public ErrorResponse(String mensaje, BigDecimal saldoDisponible) {
        this.mensaje = mensaje;
        this.saldoDisponible = saldoDisponible;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}
