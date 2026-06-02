package com.narvasoft.apirest.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registros_medico")
public class RegistrosMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_paciente", nullable = false)
    private String nombre_paciente;

    @Column(name = "apellido_paciente", nullable = false)
    private String apellido_paciente;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fecha_nacimiento;

    @Column(name = "genero")
    private String genero;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "tratamiento")
    private String tratamiento;

    @Column(name = "fecha_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_consulta;

    @Column(name = "nombre_medico")
    private String nombre_medico;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Constructor vacío (necesario para JPA)
    public RegistrosMedico() {
    }

    // Constructor con todos los campos
    public RegistrosMedico(Long id, String nombre_paciente, String apellido_paciente,
                           Date fecha_nacimiento, String genero, String diagnostico,
                           String tratamiento, Date fecha_consulta, String nombre_medico,
                           String especialidad, String observaciones) {
        this.id = id;
        this.nombre_paciente = nombre_paciente;
        this.apellido_paciente = apellido_paciente;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero = genero;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.fecha_consulta = fecha_consulta;
        this.nombre_medico = nombre_medico;
        this.especialidad = especialidad;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_paciente() {
        return nombre_paciente;
    }

    public void setNombre_paciente(String nombre_paciente) {
        this.nombre_paciente = nombre_paciente;
    }

    public String getApellido_paciente() {
        return apellido_paciente;
    }

    public void setApellido_paciente(String apellido_paciente) {
        this.apellido_paciente = apellido_paciente;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Date getFecha_consulta() {
        return fecha_consulta;
    }

    public void setFecha_consulta(Date fecha_consulta) {
        this.fecha_consulta = fecha_consulta;
    }

    public String getNombre_medico() {
        return nombre_medico;
    }

    public void setNombre_medico(String nombre_medico) {
        this.nombre_medico = nombre_medico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Método toString
    @Override
    public String toString() {
        return "RegistrosMedico{" +
                "id=" + id +
                ", nombre_paciente='" + nombre_paciente + '\'' +
                ", apellido_paciente='" + apellido_paciente + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                ", genero='" + genero + '\'' +
                ", diagnostico='" + diagnostico + '\'' +
                ", tratamiento='" + tratamiento + '\'' +
                ", fecha_consulta=" + fecha_consulta +
                ", nombre_medico='" + nombre_medico + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}