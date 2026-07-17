package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al alumno de la universidad.
 * [EVALUACIÓN]: El estudiante debe implementar el encapsulamiento y el método de inscripción.
 */
public class Estudiante extends MiembroUniversitario {
    private String matricula;
    private int anioIngreso;
    private float promedioPpa;
    private List<Inscripcion> inscripciones;

    public Estudiante(String rut, String nombre, String correo, String matricula, int anioIngreso, float promedioPpa) {
        super(rut, nombre, correo);
        this.matricula = matricula;
        this.anioIngreso = anioIngreso;
        this.promedioPpa = promedioPpa;
        this.inscripciones = new ArrayList<>();
    }

    @Override
    public boolean login(String password) {
        // TODO: Implementar validación simulada de clave del estudiante (largo mínimo de 8 caracteres)
        throw new UnsupportedOperationException("Método login() no implementado aún.");
    }

    /**
     * Realiza el proceso de inscripción en una sección.
     * [REGLAS]: Validar que la sección no sea nula y que cuente con cupos disponibles.
     */
    public void inscribirSeccion(Seccion seccion) {
        // TODO: Implementar la lógica del control de cupos y la creación de la clase de asociación 'Inscripcion'
        // No olvides agregar la nueva inscripción tanto a la lista de este estudiante como a la de la sección.
        throw new UnsupportedOperationException("Método inscribirSeccion() no implementado aún.");
    }

    // Getters y Setters
    public String getMatricula() { return matricula; }
    public float getPromedioPpa() { return promedioPpa; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }
}
