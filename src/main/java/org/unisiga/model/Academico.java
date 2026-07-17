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
        if (password == null || !password.contains("@")) return false;
        return password.length() >= 6;
    }

    /**
     * Registra la nota de un estudiante para una evaluación de la asignatura.
     * [REGLAS]: Validar parámetros, rango de notas [1.0, 7.0] y que la evaluación pertenezca a la asignatura.
     */
    public void registrarNota(Inscripcion inscripcion, Evaluacion evaluacion, float valorNota) {
        if (inscripcion == null || evaluacion == null) throw new IllegalArgumentException("Parámetros no pueden ser nulos.");
        if (valorNota < 1.0f || valorNota > 7.0f) throw new IllegalArgumentException("Nota fuera de rango [1.0, 7.0].");
        for (Calificacion c : inscripcion.getCalificaciones()) {
            if (c.getEvaluacion() == evaluacion) {
                c.setNota(valorNota);
                return;
            }
        }
        Calificacion nueva = new Calificacion(valorNota, inscripcion, evaluacion);
        inscripcion.getCalificaciones().add(nueva);
        evaluacion.getCalificaciones().add(nueva);
    }

    // Getters y Setters
    public String getIdEmpleado() { return idEmpleado; }
    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento depto) { this.departamento = depto; }
    public List<Seccion> getSeccionesDictadas() { return seccionesDictadas; }
}
