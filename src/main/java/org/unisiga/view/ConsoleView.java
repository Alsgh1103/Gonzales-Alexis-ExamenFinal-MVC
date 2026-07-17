package org.unisiga.view;

/**
 * Representa la interfaz de usuario por consola.
 */
public class ConsoleView {
    public void desplegarMenu() {
        System.out.println("=================================================");
        System.out.println("        SISTEMA DE GESTIÓN UNISIGA v2            ");
        System.out.println("=================================================");
    }

    public void mostrarMensajeProcesamiento(String mensaje) {
        System.out.println(">> [SISTEMA]: " + mensaje);
    }

    public void imprimirComprobante(String nombre, String asignatura, char grupo) {
        System.out.println("\n----------- COMPROBANTE DE MATRÍCULA -----------");
        System.out.println("Estudiante: " + nombre);
        System.out.println("Asignatura Inscrita: " + asignatura + " - Sección " + grupo);
        System.out.println("Estado de Solicitud: CURSANDO");
        System.out.println("-------------------------------------------------\n");
    }
}
