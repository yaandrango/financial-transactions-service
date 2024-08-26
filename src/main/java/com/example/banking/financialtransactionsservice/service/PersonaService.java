package com.example.banking.financialtransactionsservice.service;

import com.example.banking.financialtransactionsservice.model.Persona;
import com.example.banking.financialtransactionsservice.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }

    public List<Persona> findAll() {
        return personaRepository.findAll();
    }
}
