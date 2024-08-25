package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.exception.ResourceNotFoundException;
import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Movimiento;
import com.example.banking.financialtransactionsservice.repository.CuentaRepository;
import com.example.banking.financialtransactionsservice.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @GetMapping("/listar")
    public List<Movimiento> getAllMovimientos() {
        return movimientoService.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<Movimiento> crearMovimiento(@RequestBody Movimiento movimientoRequest) {
        Cuenta cuenta = cuentaRepository.findById(movimientoRequest.getCuenta().getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(movimientoRequest.getTipoMovimiento());
        movimiento.setValor(movimientoRequest.getValor());
        movimiento.setSaldo(movimientoRequest.getSaldo());
        movimiento.setCuenta(cuenta);
        Movimiento nuevoMovimiento = movimientoService.save(movimiento);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Movimiento> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento movimientoDetails) {
        Movimiento movimiento = movimientoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        movimiento.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
        movimiento.setValor(movimientoDetails.getValor());
        movimiento.setSaldo(movimientoDetails.getSaldo());
        Movimiento updatedMovimiento = movimientoService.save(movimiento);
        return ResponseEntity.ok(updatedMovimiento);
    }

    @PatchMapping("/actualizar-parcial/{id}")
    public ResponseEntity<Movimiento> partialUpdateMovimiento(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Movimiento movimiento = movimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "tipoMovimiento":
                    movimiento.setTipoMovimiento((String) value);
                    break;
                case "valor":
                    movimiento.setValor(new BigDecimal(value.toString()));
                    break;
                case "saldo":
                    movimiento.setSaldo(new BigDecimal(value.toString()));
                    break;
                case "cuenta_id":
                    Integer cuentaId = Integer.valueOf(value.toString());
                    Cuenta cuenta = cuentaRepository.findById(cuentaId)
                            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + cuentaId));
                    movimiento.setCuenta(cuenta);
                    break;
                default:
                    throw new RuntimeException("Campo no reconocido: " + key);
            }
        });

        Movimiento movimientoActualizado = movimientoService.save(movimiento);
        return ResponseEntity.ok(movimientoActualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> deleteMovimiento(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        movimientoService.deleteById(id);
        return ResponseEntity.ok("Movimiento eliminada exitosamente");
    }
}
