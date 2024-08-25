package com.example.banking.financialtransactionsservice.service;

import com.example.banking.financialtransactionsservice.model.Cliente;
import com.example.banking.financialtransactionsservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
