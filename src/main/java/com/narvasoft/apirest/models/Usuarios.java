package com.narvasoft.apirest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    // WRITE_ONLY: se puede recibir en el JSON de entrada, pero nunca se envía al navegador
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "cargo")
    private String cargo;   // Estudiante / Profesor / Enfermera

    @Column(name = "foto")
    private String foto;
}
