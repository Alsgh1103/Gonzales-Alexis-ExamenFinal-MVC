package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Academico;
import org.unisiga.model.Asignatura;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.model.Seccion;
import org.unisiga.view.SeccionView;

public class SeccionController {

    private SeccionView view;
    private List<Asignatura> asignaturas;
    private List<MiembroUniversitario> usuarios;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public SeccionController(SeccionView view, List<Asignatura> asignaturas, List<MiembroUniversitario> usuarios) {
        this.view = view;
        this.asignaturas = asignaturas;
        this.usuarios = usuarios;
        this.modelo = (DefaultTableModel) view.tblSeccion.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        refrescarCombos();
        initListeners();
        refrescarTabla();
    }

    private void refrescarCombos() {
        view.cmbAsignatura.removeAllItems();
        for (Asignatura a : asignaturas) {
            view.cmbAsignatura.addItem(a.getCodigo());
        }

        view.cmbAcademico.removeAllItems();
        for (MiembroUniversitario u : usuarios) {
            if (u instanceof Academico) {
                view.cmbAcademico.addItem(u.getRut());
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

        view.tblSeccion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String idStr = view.txtIdSeccion.getText().trim();
        String cupoStr = view.txtCupoMaximo.getText().trim();
        String horario = view.txtHorario.getText().trim();
        String codigoAsig = (String) view.cmbAsignatura.getSelectedItem();
        String rutAcad = (String) view.cmbAcademico.getSelectedItem();

        if (idStr.isEmpty() || cupoStr.isEmpty() || horario.isEmpty() || codigoAsig == null || rutAcad == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idStr.length() != 1) {
            javax.swing.JOptionPane.showMessageDialog(view, "El ID de sección debe ser un solo carácter.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        char idGrupo = idStr.charAt(0);
        int cupoMaximo;
        try {
            cupoMaximo = Integer.parseInt(cupoStr);
            if (cupoMaximo <= 0) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "El cupo debe ser un número entero positivo.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Asignatura asig = null;
        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equals(codigoAsig)) {
                asig = a;
                break;
            }
        }

        if (asig == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Asignatura no encontrada.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Seccion s : asig.getSecciones()) {
            if (s.getIdGrupo() == idGrupo) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe una sección con ese ID para esta asignatura.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Seccion nueva = asig.crearSeccion(idGrupo, cupoMaximo, horario);

        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equals(rutAcad) && u instanceof Academico) {
                nueva.asignarDocente((Academico) u);
                break;
            }
        }

        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una sección de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cupoStr = view.txtCupoMaximo.getText().trim();
        String horario = view.txtHorario.getText().trim();
        String rutAcad = (String) view.cmbAcademico.getSelectedItem();

        if (cupoStr.isEmpty() || horario.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Cupo y horario son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cupo;
        try {
            cupo = Integer.parseInt(cupoStr);
            if (cupo <= 0) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "El cupo debe ser un número entero positivo.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigoAsig = modelo.getValueAt(filaSeleccionada, 3).toString();
        String idStr = modelo.getValueAt(filaSeleccionada, 1).toString();

        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equals(codigoAsig)) {
                for (Seccion s : a.getSecciones()) {
                    if (String.valueOf(s.getIdGrupo()).equals(idStr)) {
                        s.setCupoMaximo(cupo);
                        s.setHorario(horario);
                        if (rutAcad != null) {
                            for (MiembroUniversitario u : usuarios) {
                                if (u.getRut().equals(rutAcad) && u instanceof Academico) {
                                    s.asignarDocente((Academico) u);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
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
                String codigoAsig = modelo.getValueAt(i, 3).toString();
                String idStr = modelo.getValueAt(i, 1).toString();
                for (Asignatura a : asignaturas) {
                    if (a.getCodigo().equals(codigoAsig)) {
                        a.getSecciones().removeIf(s -> String.valueOf(s.getIdGrupo()).equals(idStr));
                        break;
                    }
                }
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos una sección para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblSeccion.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtIdSeccion.setText(modelo.getValueAt(fila, 1).toString());
        view.txtCupoMaximo.setText(modelo.getValueAt(fila, 2).toString());
        view.cmbAsignatura.setSelectedItem(modelo.getValueAt(fila, 3).toString());
        view.txtHorario.setText(modelo.getValueAt(fila, 4).toString());
        String docente = modelo.getValueAt(fila, 5) != null ? modelo.getValueAt(fila, 5).toString() : "";
        view.cmbAcademico.setSelectedItem(docente);
    }

    private void limpiar() {
        view.txtIdSeccion.setText("");
        view.txtCupoMaximo.setText("");
        view.txtHorario.setText("");
        if (view.cmbAsignatura.getItemCount() > 0) view.cmbAsignatura.setSelectedIndex(0);
        if (view.cmbAcademico.getItemCount() > 0) view.cmbAcademico.setSelectedIndex(0);
        view.tblSeccion.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Asignatura a : asignaturas) {
            for (Seccion s : a.getSecciones()) {
                String docente = s.getDocenteDicta() != null ? s.getDocenteDicta().getRut() : "Sin asignar";
                modelo.addRow(new Object[]{false, String.valueOf(s.getIdGrupo()), s.getCupoMaximo(), a.getCodigo(), s.getHorario(), docente});
            }
        }
    }
}
