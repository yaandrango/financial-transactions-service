package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.exception.ErrorResponse;
import com.example.banking.financialtransactionsservice.exception.ResourceNotFoundException;
import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Movimiento;
import com.example.banking.financialtransactionsservice.repository.CuentaRepository;
import com.example.banking.financialtransactionsservice.repository.MovimientoRepository;
import com.example.banking.financialtransactionsservice.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping("/listar")
    public List<Movimiento> getAllMovimientos() {
        return movimientoService.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearMovimiento(@RequestBody Movimiento movimientoRequest) {
        Cuenta cuenta = cuentaRepository.findById(movimientoRequest.getCuenta().getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Obtener el saldo disponible actual desde el último movimiento registrado o el saldo inicial si no hay movimientos
        BigDecimal saldoDisponible = obtenerSaldoDisponible(cuenta.getNumeroCuenta());

        // Verificar si hay saldo suficiente
        if (saldoDisponible.add(movimientoRequest.getValor()).compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Saldo no disponible", saldoDisponible));
        }

        // Calcular el nuevo saldo disponible después del movimiento
        BigDecimal nuevoSaldo = saldoDisponible.add(movimientoRequest.getValor());

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(movimientoRequest.getTipoMovimiento());
        movimiento.setValor(movimientoRequest.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        Movimiento nuevoMovimiento = movimientoService.save(movimiento);

        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento movimientoDetails) {
        Movimiento movimiento = movimientoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        // Obtener la cuenta asociada al movimiento
        Cuenta cuenta = movimiento.getCuenta();

        // Obtener el saldo disponible actual desde el último movimiento registrado antes de la actualización
        BigDecimal saldoDisponibleAntesDeActualizacion = obtenerSaldoDisponible(cuenta.getNumeroCuenta()).subtract(movimiento.getValor());

        // Verificar si hay saldo suficiente
        if (saldoDisponibleAntesDeActualizacion.add(movimientoDetails.getValor()).compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Saldo no disponible", saldoDisponibleAntesDeActualizacion));
        }

        // Recalcular el saldo basado en el nuevo valor del movimiento
        BigDecimal nuevoSaldo = saldoDisponibleAntesDeActualizacion.add(movimientoDetails.getValor());
        movimiento.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
        movimiento.setValor(movimientoDetails.getValor());
        movimiento.setSaldo(nuevoSaldo); // Guardar el nuevo saldo en el movimiento
        movimiento.setFecha(LocalDateTime.now());
        Movimiento updatedMovimiento = movimientoService.save(movimiento);

        return ResponseEntity.ok(updatedMovimiento);
    }

    @PatchMapping("/actualizar-parcial/{id}")
    public ResponseEntity<?> partialUpdateMovimiento(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Movimiento movimiento = movimientoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));

        // Obtener la cuenta asociada al movimiento
        Cuenta cuenta = movimiento.getCuenta();

        // Obtener el saldo disponible antes de la actualización restando el valor anterior del movimiento
        BigDecimal saldoDisponibleAntesDeActualizacion = obtenerSaldoDisponible(cuenta.getNumeroCuenta()).subtract(movimiento.getValor());


        updates.forEach((key, value) -> {
            switch (key) {
                case "tipoMovimiento":
                    movimiento.setTipoMovimiento((String) value);
                    break;
                case "valor":
                    BigDecimal nuevoValor = new BigDecimal(value.toString());

                    // Verificar si hay saldo suficiente
                    if (saldoDisponibleAntesDeActualizacion.add(nuevoValor).compareTo(BigDecimal.ZERO) < 0) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                String.format("Saldo no disponible. Saldo disponible actual: %s", saldoDisponibleAntesDeActualizacion)
                        );
                    }

                    movimiento.setValor(nuevoValor);

                    // Recalcular el saldo disponible
                    BigDecimal nuevoSaldo = saldoDisponibleAntesDeActualizacion.add(nuevoValor);
                    movimiento.setSaldo(nuevoSaldo);
                    break;
                default:
                    throw new RuntimeException("Campo no reconocido: " + key);
            }
        });

        movimiento.setFecha(LocalDateTime.now());

        Movimiento movimientoActualizado = movimientoService.save(movimiento);

        return ResponseEntity.ok(movimientoActualizado);
    }

//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<String> deleteMovimiento(@PathVariable Long id) {
//        Movimiento movimiento = movimientoService.findById(id)
//                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));
//
//        // Eliminar el movimiento
//        movimientoService.deleteById(id);
//
//        return ResponseEntity.ok("Movimiento eliminada exitosamente");
//    }

    // Método auxiliar para obtener el saldo disponible actual de una cuenta
    private BigDecimal obtenerSaldoDisponible(Integer numeroCuenta) {
        Movimiento ultimoMovimiento = movimientoRepository.findFirstByCuentaNumeroCuentaOrderByFechaDesc(numeroCuenta);

        if (ultimoMovimiento != null) {
            return ultimoMovimiento.getSaldo();
        } else {
            return cuentaRepository.findById(numeroCuenta)
                    .map(Cuenta::getSaldoInicial)
                    .orElse(BigDecimal.ZERO);
        }
    }
}
