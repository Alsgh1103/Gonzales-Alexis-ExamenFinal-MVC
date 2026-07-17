package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al profesor encargado de dictar cátedras.
 */
public class Academico extends MiembroUniversitario {
    private String idEmpleado;
    private String tipoContrato;
    private Departamento departamento; // Relación de agregación
    private List<Seccion> seccionesDictadas;

    public Academico(String rut, String nombre, String correo, String idEmpleado, String tipoContrato) {
        super(rut, nombre, correo);
        this.idEmpleado = idEmpleado;
        this.tipoContrato = tipoContrato;
        this.seccionesDictadas = new ArrayList<>();
    }

    @Override
    public boolean login(String password) {
        // TODO: Implementar validación simulada de MFA docente (requiere que el password contenga '@')
        throw new UnsupportedOperationException("Método login() no implementado aún.");
    }

    /**
     * Registra la nota de un estudiante para una evaluación de la asignatura.
     * [REGLAS]: Validar parámetros, rango de notas [1.0, 7.0] y que la evaluación pertenezca a la asignatura.
     */
    public void registrarNota(Inscripcion inscripcion, Evaluacion evaluacion, float valorNota) {
        // TODO: Implementar la validación e inserción/actualización de la nota (Tres Vías)
        throw new UnsupportedOperationException("Método registrarNota() no implementado aún.");
    }

    // Getters y Setters
    public String getIdEmpleado() { return idEmpleado; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento depto) { this.departamento = depto; }
    public List<Seccion> getSeccionesDictadas() { return seccionesDictadas; }
}
