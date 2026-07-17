package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Hito evaluativo del plan unificado de la asignatura.
 */
public class Evaluacion {
    private int id;
    private String titulo;
    private float ponderacion;
    private Asignatura asignatura; // Contenedor (Composición)
    private List<Calificacion> calificaciones;

    // [DISEÑO TÉCNICO]: Constructor package-private para forzar que solo 'Asignatura' lo invoque.
    Evaluacion(int id, String titulo, float ponderacion, Asignatura asignatura) {
        this.id = id;
        this.titulo = titulo;
        this.ponderacion = ponderacion;
        this.asignatura = asignatura;
        this.calificaciones = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public float getPonderacion() { return ponderacion; }
    public Asignatura getAsignatura() { return asignatura; }
    public List<Calificacion> getCalificaciones() { return calificaciones; }
}
