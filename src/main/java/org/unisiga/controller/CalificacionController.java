package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Asignatura;
import org.unisiga.model.Calificacion;
import org.unisiga.model.Evaluacion;
import org.unisiga.model.Inscripcion;
import org.unisiga.model.Estudiante;
import org.unisiga.view.CalificacionView;

public class CalificacionController {

    private CalificacionView view;
    private List<Inscripcion> inscripciones;
    private List<Asignatura> asignaturas;
    private List<Calificacion> calificaciones;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public CalificacionController(CalificacionView view, List<Inscripcion> inscripciones, List<Asignatura> asignaturas, List<Calificacion> calificaciones) {
        this.view = view;
        this.inscripciones = inscripciones;
        this.asignaturas = asignaturas;
        this.calificaciones = calificaciones;
        this.modelo = (DefaultTableModel) view.tblEvaluacion.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarCombos();
        initListeners();
        refrescarTabla();
    }

    private void cargarCombos() {
        view.cmbEstudiante.removeAllItems();
        for (Inscripcion ins : inscripciones) {
            String rut = ins.getEstudiante().getRut();
            boolean existe = false;
            for (int i = 0; i < view.cmbEstudiante.getItemCount(); i++) {
                if (view.cmbEstudiante.getItemAt(i).equals(rut)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) view.cmbEstudiante.addItem(rut);
        }

        view.cmbEvaluacion.removeAllItems();
        for (Asignatura a : asignaturas) {
            for (Evaluacion ev : a.getEvaluaciones()) {
                view.cmbEvaluacion.addItem(a.getCodigo() + " - " + ev.getTitulo());
            }
        }
    }

    private void initListeners() {
        view.bntAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar();
            }
        });

        view.btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });

        view.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        view.btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        view.btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        view.tblEvaluacion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String rutEst = (String) view.cmbEstudiante.getSelectedItem();
        String evalKey = (String) view.cmbEvaluacion.getSelectedItem();
        String notaStr = view.txtNota.getText().trim();

        if (rutEst == null || evalKey == null || notaStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        float nota;
        try {
            nota = Float.parseFloat(notaStr);
            if (nota < 1.0f || nota > 7.0f) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "La nota debe ser un número entre 1.0 y 7.0.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Inscripcion inscripcion = null;
        Evaluacion evaluacion = buscarEvaluacion(evalKey);

        for (Inscripcion ins : inscripciones) {
            if (ins.getEstudiante().getRut().equals(rutEst)) {
                if (evaluacion != null && ins.getSeccion().getAsignatura() == evaluacion.getAsignatura()) {
                    inscripcion = ins;
                    break;
                }
            }
        }

        if (inscripcion == null || evaluacion == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "El estudiante no tiene inscripción en la asignatura de esa evaluación.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Calificacion c : calificaciones) {
            if (c.getInscripcion() == inscripcion && c.getEvaluacion() == evaluacion) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe una calificación para ese estudiante en esa evaluación.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Calificacion nueva = new Calificacion(nota, inscripcion, evaluacion);
        calificaciones.add(nueva);
        inscripcion.getCalificaciones().add(nueva);
        evaluacion.getCalificaciones().add(nueva);
        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una calificación de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String notaStr = view.txtNota.getText().trim();
        if (notaStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "La nota es obligatoria.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        float nota;
        try {
            nota = Float.parseFloat(notaStr);
            if (nota < 1.0f || nota > 7.0f) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "La nota debe ser un número entre 1.0 y 7.0.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rutEst = modelo.getValueAt(filaSeleccionada, 1).toString();
        String evalKey = modelo.getValueAt(filaSeleccionada, 2).toString();
        Evaluacion evaluacion = buscarEvaluacion(evalKey);

        for (Calificacion c : calificaciones) {
            if (c.getInscripcion().getEstudiante().getRut().equals(rutEst) && c.getEvaluacion() == evaluacion) {
                c.setNota(nota);
                break;
            }
        }
        refrescarTabla();
        limpiar();
    }

    private void eliminar() {
        boolean eliminado = false;
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            Boolean sel = (Boolean) modelo.getValueAt(i, 0);
            if (sel != null && sel) {
                String rutEst = modelo.getValueAt(i, 1).toString();
                String evalKey = modelo.getValueAt(i, 2).toString();
                Evaluacion evaluacion = buscarEvaluacion(evalKey);
                calificaciones.removeIf(c -> c.getInscripcion().getEstudiante().getRut().equals(rutEst) && c.getEvaluacion() == evaluacion);
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos una calificación para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblEvaluacion.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.cmbEstudiante.setSelectedItem(modelo.getValueAt(fila, 1).toString());
        view.cmbEvaluacion.setSelectedItem(modelo.getValueAt(fila, 2).toString());
        view.txtNota.setText(modelo.getValueAt(fila, 3).toString());
    }

    private void limpiar() {
        if (view.cmbEstudiante.getItemCount() > 0) view.cmbEstudiante.setSelectedIndex(0);
        if (view.cmbEvaluacion.getItemCount() > 0) view.cmbEvaluacion.setSelectedIndex(0);
        view.txtNota.setText("");
        view.tblEvaluacion.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Calificacion c : calificaciones) {
            String evalKey = c.getEvaluacion().getAsignatura().getCodigo() + " - " + c.getEvaluacion().getTitulo();
            modelo.addRow(new Object[]{false, c.getInscripcion().getEstudiante().getRut(), evalKey, c.getNota()});
        }
    }

    private Evaluacion buscarEvaluacion(String key) {
        if (key == null) return null;
        for (Asignatura a : asignaturas) {
            for (Evaluacion ev : a.getEvaluaciones()) {
                String evKey = a.getCodigo() + " - " + ev.getTitulo();
                if (evKey.equals(key)) return ev;
            }
        }
        return null;
    }
}
