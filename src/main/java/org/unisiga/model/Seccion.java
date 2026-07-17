package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Materialización física de una asignatura en el semestre.
 */
public class Seccion {
    private char idGrupo;
    private int cupoMaximo;
    private String horario;
    private Asignatura asignatura; // Contenedor (Composición)
    private Academico docenteDicta;
    private List<Inscripcion> inscripciones;

    // [DISEÑO TÉCNICO]: Constructor package-private para forzar que solo 'Asignatura' (mismo package) lo invoque.
    Seccion(char idGrupo, int cupoMaximo, String horario, Asignatura asignatura) {
        this.idGrupo = idGrupo;
        this.cupoMaximo = cupoMaximo;
        this.horario = horario;
        this.asignatura = asignatura;
        this.inscripciones = new ArrayList<>();
    }

    public void asignarDocente(Academico docente) {
        // TODO: Asignar al docente de forma segura controlando la asociación bidireccional
        throw new UnsupportedOperationException("Método asignarDocente() no implementado aún.");
    }

    // Getters y Setters
    public char getIdGrupo() { return idGrupo; }
    public int getCupoMaximo() { return cupoMaximo; }
    public String getHorario() { return horario; }
    public Asignatura getAsignatura() { return asignatura; }
    public Academico getDocenteDicta() { return docenteDicta; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }
}
