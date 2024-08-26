package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.model.Persona;
import com.example.banking.financialtransactionsservice.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @PostMapping("/crear")
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        Persona savedPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPersona);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaService.findAll();
        return ResponseEntity.ok(personas);
    }
}
