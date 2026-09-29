package com.narvasoft.apirest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
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

    // Constructor vacío (necesario para JPA)
    public Usuarios() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    // toString sin la contraseña
    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", cargo='" + cargo + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}