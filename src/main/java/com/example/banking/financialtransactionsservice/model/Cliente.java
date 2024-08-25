package com.example.banking.financialtransactionsservice.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "cliente", schema = "public")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteid;

    private String contrasena;
    private Boolean estado;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    public Cliente() {}

    // Getters y Setters
    public Long getClienteid() {
        return clienteid;
    }

    public void setClienteid(Long clienteid) {
        this.clienteid = clienteid;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
