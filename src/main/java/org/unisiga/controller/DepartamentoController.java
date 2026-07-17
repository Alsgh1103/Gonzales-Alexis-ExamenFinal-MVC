package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Departamento;
import org.unisiga.view.DepartamentoView;

public class DepartamentoController {

    private DepartamentoView view;
    private List<Departamento> departamentos;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public DepartamentoController(DepartamentoView view, List<Departamento> departamentos) {
        this.view = view;
        this.departamentos = departamentos;
        this.modelo = (DefaultTableModel) view.tblDepartamento.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initListeners();
        refrescarTabla();
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

        view.tblDepartamento.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String codigo = view.txtCodigo.getText().trim();
        String nombre = view.txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Código y nombre son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Departamento d : departamentos) {
            if (d.getCodigoDepto().equalsIgnoreCase(codigo)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe un departamento con ese código.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Departamento nuevo = new Departamento(codigo, nombre);
        departamentos.add(nuevo);
        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione un departamento de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = view.txtCodigo.getText().trim();
        String nombre = view.txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Código y nombre son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigoOriginal = modelo.getValueAt(filaSeleccionada, 1).toString();
        for (Departamento d : departamentos) {
            if (d.getCodigoDepto().equals(codigoOriginal)) {
                d.setNombre(nombre);
                break;
            }
        }
        refrescarTabla();
        limpiar();
    }

    private void eliminar() {
        boolean eliminado = false;
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                String codigo = modelo.getValueAt(i, 1).toString();
                departamentos.removeIf(d -> d.getCodigoDepto().equals(codigo));
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos un departamento para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblDepartamento.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
        view.txtNombre.setText(modelo.getValueAt(fila, 2).toString());
    }

    private void limpiar() {
        view.txtCodigo.setText("");
        view.txtNombre.setText("");
        view.tblDepartamento.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Departamento d : departamentos) {
            modelo.addRow(new Object[]{false, d.getCodigoDepto(), d.getNombre()});
        }
    }
}
