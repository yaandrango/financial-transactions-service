package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.controller.MovimientoController;
import com.example.banking.financialtransactionsservice.model.Cuenta;
import com.example.banking.financialtransactionsservice.model.Movimiento;
import com.example.banking.financialtransactionsservice.exception.ErrorResponse;
import com.example.banking.financialtransactionsservice.repository.CuentaRepository;
import com.example.banking.financialtransactionsservice.repository.MovimientoRepository;
import com.example.banking.financialtransactionsservice.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MovimientoControllerTest {

    @Mock
    private MovimientoService movimientoService;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private MovimientoController movimientoController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movimientoController).build();
    }

    @Test
    public void testCrearMovimiento_SaldoDisponible() throws Exception {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento("Deposito");
        movimiento.setValor(BigDecimal.valueOf(500));
        movimiento.setSaldo(BigDecimal.valueOf(1500));
        movimiento.setCuenta(cuenta);

        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        when(movimientoService.save(any(Movimiento.class))).thenReturn(movimiento);

        mockMvc.perform(post("/api/movimientos/crear")
                        .contentType("application/json")
                        .content("{\"cuenta\":{\"numeroCuenta\":1},\"tipoMovimiento\":\"Deposito\",\"valor\":500}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(500))
                .andExpect(jsonPath("$.saldo").value(1500));
    }

    @Test
    public void testCrearMovimiento_SaldoNoDisponible() throws Exception {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));

        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));

        mockMvc.perform(post("/api/movimientos/crear")
                        .contentType("application/json")
                        .content("{\"cuenta\":{\"numeroCuenta\":1},\"tipoMovimiento\":\"Retiro\",\"valor\":-1500}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Saldo no disponible"))
                .andExpect(jsonPath("$.saldoDisponible").value(1000));
    }



}
