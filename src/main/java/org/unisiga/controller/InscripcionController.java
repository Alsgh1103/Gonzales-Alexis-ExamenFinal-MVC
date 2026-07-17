package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Asignatura;
import org.unisiga.model.Estudiante;
import org.unisiga.model.Inscripcion;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.model.Seccion;
import org.unisiga.view.InscripcionView;

public class InscripcionController {

    private InscripcionView view;
    private List<MiembroUniversitario> usuarios;
    private List<Asignatura> asignaturas;
    private List<Inscripcion> inscripciones;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public InscripcionController(InscripcionView view, List<MiembroUniversitario> usuarios, List<Asignatura> asignaturas, List<Inscripcion> inscripciones) {
        this.view = view;
        this.usuarios = usuarios;
        this.asignaturas = asignaturas;
        this.inscripciones = inscripciones;
        this.modelo = (DefaultTableModel) view.tblSeccion.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarCombos();
        initListeners();
        refrescarTabla();
    }

    private void cargarCombos() {
        view.cmbEstudiante.removeAllItems();
        for (MiembroUniversitario u : usuarios) {
            if (u instanceof Estudiante) {
                view.cmbEstudiante.addItem(u.getRut());
            }
        }

        view.cmbSeccion.removeAllItems();
        for (Asignatura a : asignaturas) {
            for (Seccion s : a.getSecciones()) {
                view.cmbSeccion.addItem(a.getCodigo() + "-" + s.getIdGrupo());
            }
        }

        view.cmbEstado.removeAllItems();
        view.cmbEstado.addItem("Inscrito");
        view.cmbEstado.addItem("Aprobado");
        view.cmbEstado.addItem("Reprobado");
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

        view.tblSeccion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String rutEst = (String) view.cmbEstudiante.getSelectedItem();
        String seccionKey = (String) view.cmbSeccion.getSelectedItem();
        String estado = (String) view.cmbEstado.getSelectedItem();

        if (rutEst == null || seccionKey == null || estado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione estudiante, sección y estado.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fecha = view.dateFecha.getDate();
        if (fecha == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una fecha de inscripción.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Estudiante estudiante = null;
        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equals(rutEst) && u instanceof Estudiante) {
                estudiante = (Estudiante) u;
                break;
            }
        }

        Seccion seccion = buscarSeccionPorKey(seccionKey);

        if (estudiante == null || seccion == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Estudiante o sección no válidos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Inscripcion ins : inscripciones) {
            if (ins.getEstudiante().getRut().equals(rutEst) && ins.getSeccion() == seccion) {
                javax.swing.JOptionPane.showMessageDialog(view, "El estudiante ya está inscrito en esa sección.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (seccion.getInscripciones().size() >= seccion.getCupoMaximo()) {
            javax.swing.JOptionPane.showMessageDialog(view, "La sección no tiene cupos disponibles.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Inscripcion nueva = new Inscripcion(estudiante, seccion);
        nueva.setEstadoInscripcion(estado);
        inscripciones.add(nueva);
        seccion.getInscripciones().add(nueva);
        estudiante.getInscripciones().add(nueva);
        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una inscripción de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String estado = (String) view.cmbEstado.getSelectedItem();
        String rutEst = modelo.getValueAt(filaSeleccionada, 1).toString();
        String seccionKey = modelo.getValueAt(filaSeleccionada, 2).toString();

        for (Inscripcion ins : inscripciones) {
            if (ins.getEstudiante().getRut().equals(rutEst)) {
                String key = ins.getSeccion().getAsignatura().getCodigo() + "-" + ins.getSeccion().getIdGrupo();
                if (key.equals(seccionKey)) {
                    ins.setEstadoInscripcion(estado);
                    break;
                }
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
                String seccionKey = modelo.getValueAt(i, 2).toString();
                inscripciones.removeIf(ins -> {
                    String key = ins.getSeccion().getAsignatura().getCodigo() + "-" + ins.getSeccion().getIdGrupo();
                    return ins.getEstudiante().getRut().equals(rutEst) && key.equals(seccionKey);
                });
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos una inscripción para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblSeccion.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.cmbEstudiante.setSelectedItem(modelo.getValueAt(fila, 1).toString());
        view.cmbSeccion.setSelectedItem(modelo.getValueAt(fila, 2).toString());
        view.cmbEstado.setSelectedItem(modelo.getValueAt(fila, 3).toString());
    }

    private void limpiar() {
        if (view.cmbEstudiante.getItemCount() > 0) view.cmbEstudiante.setSelectedIndex(0);
        if (view.cmbSeccion.getItemCount() > 0) view.cmbSeccion.setSelectedIndex(0);
        if (view.cmbEstado.getItemCount() > 0) view.cmbEstado.setSelectedIndex(0);
        view.dateFecha.setDate(null);
        view.tblSeccion.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Inscripcion ins : inscripciones) {
            String key = ins.getSeccion().getAsignatura().getCodigo() + "-" + ins.getSeccion().getIdGrupo();
            modelo.addRow(new Object[]{false, ins.getEstudiante().getRut(), key, ins.getEstadoInscripcion(), ins.getFechaInscripcion()});
        }
    }

    private Seccion buscarSeccionPorKey(String key) {
        String[] partes = key.split("-");
        if (partes.length < 2) return null;
        String codigoAsig = partes[0];
        char idGrupo = partes[1].charAt(0);
        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equals(codigoAsig)) {
                for (Seccion s : a.getSecciones()) {
                    if (s.getIdGrupo() == idGrupo) return s;
                }
            }
        }
        return null;
    }
}
