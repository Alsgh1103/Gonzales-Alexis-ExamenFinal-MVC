package org.unisiga.controller;

import java.util.ArrayList;
import java.util.List;
import org.unisiga.model.*;

/**
 * Controlador de lógica de negocio transaccional. Simula llamadas e interacciones de base de datos.
 */
public class InscripcionController {
    private List<Estudiante> estudiantesDb;
    private List<Asignatura> asignaturasDb;

    public InscripcionController() {
        this.estudiantesDb = new ArrayList<>();
        this.asignaturasDb = new ArrayList<>();
    }

    // Métodos de sembrado (seeding) de bases de datos
    public void registrarEstudianteEnDb(Estudiante e) { estudiantesDb.add(e); }
    public void registrarAsignaturaEnDb(Asignatura a) { asignaturasDb.add(a); }

    /**
     * Procesa la solicitud de inscripción de asignaturas.
     * [LÓGICA]: 
     * 1. Buscar estudiante y asignatura.
     * 2. Obtener el grupo solicitado por composición.
     * 3. VALIDAR PRERREQUISITOS (Auto-asociación): El alumno debe tener aprobado el prerrequisito en su historial.
     * 4. Delegar la transacción al dominio del modelo.
     */
    public String inscribirSeccionEstudiante(String matricula, String codigoAsignatura, char idGrupo) {
        // TODO: Implementar la orquestación completa de la inscripción según la regla de negocio
        throw new UnsupportedOperationException("El controlador de inscripción no está implementado.");
    }
}
