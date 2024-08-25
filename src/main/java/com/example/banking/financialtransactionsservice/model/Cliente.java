package com.example.banking.financialtransactionsservice.model;

import jakarta.persistence.*;

@Entity
public class Cliente extends Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteid;

    private String contrasena;
    private Boolean estado;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

}
