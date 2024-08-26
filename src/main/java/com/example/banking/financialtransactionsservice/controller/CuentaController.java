package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.dto.CuentaRequest;
import com.example.banking.financialtransactionsservice.exception.ResourceNotFoundException;
import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Cliente;
import com.example.banking.financialtransactionsservice.repository.CuentaRepository;
import com.example.banking.financialtransactionsservice.service.CuentaService;
import com.example.banking.financialtransactionsservice.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listar")
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaRequest cuentaRequest) {
        Cliente cliente = clienteService.findById(cuentaRequest.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaRequest.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaRequest.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaRequest.getSaldoInicial());
        cuenta.setEstado(cuentaRequest.getEstado());
        cuenta.setCliente(cliente);
        Cuenta nuevaCuenta = cuentaService.save(cuenta);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{numeroCuenta}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Integer numeroCuenta, @RequestBody Cuenta cuentaDetails) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + numeroCuenta));

        cuenta.setTipoCuenta(cuentaDetails.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDetails.getSaldoInicial());
        cuenta.setEstado(cuentaDetails.getEstado());
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @PatchMapping("/actualizar-parcial/{numeroCuenta}")
    public ResponseEntity<Cuenta> partialUpdateCuenta(@PathVariable Integer numeroCuenta, @RequestBody Map<String, Object> updates) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + numeroCuenta));

        updates.forEach((key, value) -> {
            switch (key) {
                case "tipoCuenta":
                    cuenta.setTipoCuenta((String) value);
                    break;
                case "saldoInicial":
                    cuenta.setSaldoInicial(new BigDecimal(value.toString()));
                    break;
                case "estado":
                    cuenta.setEstado((Boolean) value);
                    break;
                case "clienteId":
                    Long clienteId = Long.valueOf(value.toString());
                    Cliente cliente = clienteService.findById(clienteId)
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));
                    cuenta.setCliente(cliente);
                    break;
                default:
                    throw new RuntimeException("Campo no reconocido: " + key);
            }
        });

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return ResponseEntity.ok(cuentaActualizada);
    }


    @DeleteMapping("/eliminar/{numeroCuenta}")
    public ResponseEntity<String> deleteCuenta(@PathVariable Integer numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + numeroCuenta));

        cuentaRepository.delete(cuenta);
        return ResponseEntity.ok("Cuenta eliminada exitosamente");
    }
}
