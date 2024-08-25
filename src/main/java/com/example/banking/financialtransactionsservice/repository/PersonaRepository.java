package com.example.banking.financialtransactionsservice.repository;

import com.example.banking.financialtransactionsservice.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
