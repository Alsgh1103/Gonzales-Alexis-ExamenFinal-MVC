package org.unisiga.main;

import org.unisiga.controller.InscripcionController;
import org.unisiga.model.*;
import org.unisiga.view.ConsoleView;

/**
 * Orquestador principal de pruebas. Configura el escenario inicial de la pauta.
 */
public class Main {
    public static void main(String[] args) {
        ConsoleView vista = new ConsoleView();
        InscripcionController controller = new InscripcionController();

        vista.desplegarMenu();

        // 1. TODO: Sembrar catálogo de asignaturas en el sistema (ej. Álgebra, Programación, POO)
        // 2. TODO: Definir reglas recursivas (POO requiere Programación Básica)
        // 3. TODO: Instanciar secciones y evaluaciones a través de Composición Fuerte en Asignatura
        // 4. TODO: Crear estudiantes de prueba (Juan con prerrequisito aprobado, María sin el prerrequisito)
        // 5. TODO: Ejecutar casos de uso en el controlador y validar salidas con la vista
        
        System.out.println("[SISTEMA] El esqueleto está compilado. Comienza a implementar la lógica de negocio.");
    }
}
