package com.example.banking.financialtransactionsservice.service;

import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Movimiento;
import com.example.banking.financialtransactionsservice.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {
    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<Movimiento> findAll() {
        return movimientoRepository.findAll();
    }

    public Optional<Movimiento> findById(Long id) {
        return movimientoRepository.findById(id);
    }

    public Movimiento save(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    public void deleteById(Long id) {
        movimientoRepository.deleteById(id);
    }

//    // Método para obtener movimientos de una cuenta ordenados por fecha descendente
//    public List<Movimiento> findByCuentaOrderByFechaDesc(Cuenta cuenta) {
//        return movimientoRepository.findByCuentaOrderByFechaDesc(cuenta);
//    }
}
