package com.example.banking.financialtransactionsservice.controller;

import com.example.banking.financialtransactionsservice.dto.ClienteRequest;
import com.example.banking.financialtransactionsservice.exception.ResourceNotFoundException;
import com.example.banking.financialtransactionsservice.model.Cliente;
import com.example.banking.financialtransactionsservice.model.Persona;
import com.example.banking.financialtransactionsservice.repository.ClienteRepository;
import com.example.banking.financialtransactionsservice.service.ClienteService;
import com.example.banking.financialtransactionsservice.service.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClienteControllerTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    public void testGetAllClientes() throws Exception {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        mockMvc.perform(get("/api/clientes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testCrearCliente() throws Exception {
        Persona persona = new Persona();
        persona.setId(1L);

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setContrasena("contrasena123");
        cliente.setEstado(true);

        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setPersonaId(1L);
        clienteRequest.setContrasena("contrasena123");
        clienteRequest.setEstado(true);

        when(personaService.findById(1L)).thenReturn(Optional.of(persona));
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personaId\":1,\"contrasena\":\"contrasena123\",\"estado\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contrasena").value("contrasena123"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    public void testActualizarCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteid(1L);
        cliente.setContrasena("oldPassword");
        cliente.setEstado(true);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/clientes/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contrasena\":\"newPassword\",\"estado\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contrasena").value("newPassword"))
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    public void testPartialUpdateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteid(1L);
        cliente.setContrasena("oldPassword");
        cliente.setEstado(true);

        Persona persona = new Persona();
        persona.setId(2L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(personaService.findById(2L)).thenReturn(Optional.of(persona));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(patch("/api/clientes/actualizar-parcial/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contrasena\":\"newPassword\",\"estado\":false,\"personaId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contrasena").value("newPassword"))
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteid(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(delete("/api/clientes/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente eliminado exitosamente"));

        verify(clienteRepository, times(1)).delete(cliente);
    }
}
