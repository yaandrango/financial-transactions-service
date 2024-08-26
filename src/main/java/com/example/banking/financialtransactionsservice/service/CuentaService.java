package com.example.banking.financialtransactionsservice.service;

import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Optional<Cuenta> findById(Integer numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta);
    }

    public void delete(Cuenta cuenta) {
        cuentaRepository.delete(cuenta);
    }
}
