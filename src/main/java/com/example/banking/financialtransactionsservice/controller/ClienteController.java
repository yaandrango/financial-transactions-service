package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.dto.ClienteRequest;
import com.example.banking.financialtransactionsservice.exception.ResourceNotFoundException;
import com.example.banking.financialtransactionsservice.model.Cliente;
import com.example.banking.financialtransactionsservice.model.Persona;
import com.example.banking.financialtransactionsservice.repository.ClienteRepository;
import com.example.banking.financialtransactionsservice.service.ClienteService;
import com.example.banking.financialtransactionsservice.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/listar")
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<Cliente> crearCliente(@RequestBody ClienteRequest clienteRequest) {
        Persona persona = personaService.findById(clienteRequest.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setContrasena(clienteRequest.getContrasena());
        cliente.setEstado(clienteRequest.getEstado());
        Cliente nuevoCliente = clienteService.save(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setContrasena(clienteDetails.getContrasena());
        cliente.setEstado(clienteDetails.getEstado());
        return clienteRepository.save(cliente);
    }

    @PatchMapping("/actualizar-parcial/{id}")
    public ResponseEntity<Cliente> partialUpdateCliente(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "contrasena":
                    cliente.setContrasena((String) value);
                    break;
                case "estado":
                    cliente.setEstado((Boolean) value);
                    break;
                case "personaId":
                    Long personaId = Long.valueOf(value.toString());
                    Persona persona = personaService.findById(personaId)
                            .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + personaId));
                    cliente.setPersona(persona);
                    break;
                default:
                    throw new RuntimeException("Campo no reconocido: " + key);
            }
        });

        Cliente updatedCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        clienteRepository.delete(cliente);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }
}

