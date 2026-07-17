package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Academico;
import org.unisiga.model.Departamento;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.view.AcademicoView;

public class AcademicoController {

    private AcademicoView view;
    private List<MiembroUniversitario> usuarios;
    private List<Departamento> departamentos;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public AcademicoController(AcademicoView view, List<MiembroUniversitario> usuarios, List<Departamento> departamentos) {
        this.view = view;
        this.usuarios = usuarios;
        this.departamentos = departamentos;
        this.modelo = (DefaultTableModel) view.tblAcademico.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarComboContrato();
        initListeners();
        refrescarTabla();
    }

    private void cargarComboContrato() {
        view.cmbTipoContrato.removeAllItems();
        view.cmbTipoContrato.addItem("Planta");
        view.cmbTipoContrato.addItem("Contrata");
        view.cmbTipoContrato.addItem("Honorarios");
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

        view.tblAcademico.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String rut = view.txtRut.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String correo = view.txtCorreo.getText().trim();
        String idEmpleado = view.txtIdEmpleado.getText().trim();
        String contrato = (String) view.cmbTipoContrato.getSelectedItem();

        if (rut.isEmpty() || nombre.isEmpty() || correo.isEmpty() || idEmpleado.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equalsIgnoreCase(rut)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe un usuario con ese RUT.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Academico nuevo = new Academico(rut, nombre, correo, idEmpleado, contrato);
        usuarios.add(nuevo);
        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione un académico de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rutOriginal = modelo.getValueAt(filaSeleccionada, 1).toString();
        String nombre = view.txtNombre.getText().trim();
        String correo = view.txtCorreo.getText().trim();
        String contrato = (String) view.cmbTipoContrato.getSelectedItem();

        if (nombre.isEmpty() || correo.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Nombre y correo son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equals(rutOriginal) && u instanceof Academico) {
                Academico a = (Academico) u;
                a.setNombre(nombre);
                a.setCorreoInstitucional(correo);
                a.setTipoContrato(contrato);
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
                String rut = modelo.getValueAt(i, 1).toString();
                usuarios.removeIf(u -> u.getRut().equals(rut) && u instanceof Academico);
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos un académico para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblAcademico.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtRut.setText(modelo.getValueAt(fila, 1).toString());
        view.txtNombre.setText(modelo.getValueAt(fila, 2).toString());
        view.txtCorreo.setText(modelo.getValueAt(fila, 3).toString());
        view.txtIdEmpleado.setText(modelo.getValueAt(fila, 4).toString());
        view.cmbTipoContrato.setSelectedItem(modelo.getValueAt(fila, 5).toString());
    }

    private void limpiar() {
        view.txtRut.setText("");
        view.txtNombre.setText("");
        view.txtCorreo.setText("");
        view.txtIdEmpleado.setText("");
        if (view.cmbTipoContrato.getItemCount() > 0) view.cmbTipoContrato.setSelectedIndex(0);
        view.tblAcademico.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (MiembroUniversitario u : usuarios) {
            if (u instanceof Academico) {
                Academico a = (Academico) u;
                modelo.addRow(new Object[]{false, a.getRut(), a.getNombre(), a.getCorreoInstitucional(), a.getIdEmpleado(), a.getTipoContrato()});
            }
        }
    }
}
