package org.unisiga.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase de Asociación que resuelve la relación N:M entre Estudiante y Sección.
 */
public class Inscripcion {
    private Estudiante estudiante;
    private Seccion seccion;
    private String estadoInscripcion; // "Inscrito", "Aprobado", "Reprobado"
    private Date fechaInscripcion;
    private List<Calificacion> calificaciones;

    public Inscripcion(Estudiante estudiante, Seccion seccion) {
        this.estudiante = estudiante;
        this.seccion = seccion;
        this.estadoInscripcion = "Inscrito";
        this.fechaInscripcion = new Date();
        this.calificaciones = new ArrayList<>();
    }

    // Getters y Setters
    public Estudiante getEstudiante() { return estudiante; }
    public Seccion getSeccion() { return seccion; }
    public String getEstadoInscripcion() { return estadoInscripcion; }
    public void setEstadoInscripcion(String estado) { this.estadoInscripcion = estado; }
    public Date getFechaInscripcion() { return fechaInscripcion; }
    public List<Calificacion> getCalificaciones() { return calificaciones; }
}
