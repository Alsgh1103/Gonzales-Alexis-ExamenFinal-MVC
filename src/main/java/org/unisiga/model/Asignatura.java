package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo general de asignaturas. Controla las secciones y evaluaciones unificadas.
 * [EVALUACIÓN]: Control absoluto de ciclos de vida mediante Composición Fuerte y Auto-asociación.
 */
public class Asignatura {
    private String codigo;
    private String nombre;
    private int creditosSct;
    
    // Auto-asociación recursiva para prerrequisitos
    private List<Asignatura> prerrequisitos;
    
    // Composiciones fuertes (Sólo Asignatura puede instanciar estos objetos)
    private List<Seccion> secciones;
    private List<Evaluacion> evaluaciones;

    public Asignatura(String codigo, String nombre, int creditosSct) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditosSct = creditosSct;
        this.prerrequisitos = new ArrayList<>();
        this.secciones = new ArrayList<>();
        this.evaluaciones = new ArrayList<>();
    }

    public void agregarPrerrequisito(Asignatura asig) {
        if (asig == null || prerrequisitos.contains(asig)) return;
        prerrequisitos.add(asig);
    }

    /**
     * LÓGICA DE COMPOSICIÓN: Instancia una sección semestral y la asocia.
     */
    public Seccion crearSeccion(char idGrupo, int cupoMaximo, String horario) {
        Seccion nueva = new Seccion(idGrupo, cupoMaximo, horario, this);
        secciones.add(nueva);
        return nueva;
    }

    /**
     * LÓGICA DE COMPOSICIÓN: Instancia una evaluación unificada para la asignatura.
     */
    public Evaluacion crearEvaluacion(int id, String titulo, float ponderacion) {
        Evaluacion nueva = new Evaluacion(id, titulo, ponderacion, this);
        evaluaciones.add(nueva);
        return nueva;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCreditosSct() { return creditosSct; }
    public void setCreditosSct(int creditosSct) { this.creditosSct = creditosSct; }
    public List<Asignatura> getPrerrequisitos() { return prerrequisitos; }
    public List<Seccion> getSecciones() { return secciones; }
    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }
}
