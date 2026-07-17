package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Asignatura;
import org.unisiga.view.AsignaturaView;

public class AsignaturaController {

    private AsignaturaView view;
    private List<Asignatura> asignaturas;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public AsignaturaController(AsignaturaView view, List<Asignatura> asignaturas) {
        this.view = view;
        this.asignaturas = asignaturas;
        this.modelo = (DefaultTableModel) view.tblAsignatura.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initListeners();
        refrescarTabla();
        refrescarCombo();
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

        view.tblAsignatura.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String codigo = view.txtCodigo.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String creditosStr = view.txtCreditos.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Código, nombre y créditos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int creditos;
        try {
            creditos = Integer.parseInt(creditosStr);
            if (creditos <= 0) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Los créditos deben ser un número entero positivo.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe una asignatura con ese código.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Asignatura nueva = new Asignatura(codigo, nombre, creditos);

        String prereqSel = (String) view.cmbPrerrequisito.getSelectedItem();
        if (prereqSel != null && !prereqSel.equals("Ninguno")) {
            for (Asignatura a : asignaturas) {
                if (a.getCodigo().equals(prereqSel)) {
                    nueva.agregarPrerrequisito(a);
                    break;
                }
            }
        }

        asignaturas.add(nueva);
        refrescarTabla();
        refrescarCombo();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una asignatura de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = view.txtNombre.getText().trim();
        String creditosStr = view.txtCreditos.getText().trim();

        if (nombre.isEmpty() || creditosStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Nombre y créditos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int creditos;
        try {
            creditos = Integer.parseInt(creditosStr);
            if (creditos <= 0) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Los créditos deben ser un número entero positivo.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigoOriginal = modelo.getValueAt(filaSeleccionada, 1).toString();
        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equals(codigoOriginal)) {
                a.setNombre(nombre);
                a.setCreditosSct(creditos);
                break;
            }
        }
        refrescarTabla();
        refrescarCombo();
        limpiar();
    }

    private void eliminar() {
        boolean eliminado = false;
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            Boolean sel = (Boolean) modelo.getValueAt(i, 0);
            if (sel != null && sel) {
                String codigo = modelo.getValueAt(i, 1).toString();
                asignaturas.removeIf(a -> a.getCodigo().equals(codigo));
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos una asignatura para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        refrescarCombo();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblAsignatura.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
        view.txtNombre.setText(modelo.getValueAt(fila, 2).toString());
        view.txtCreditos.setText(modelo.getValueAt(fila, 3).toString());
    }

    private void limpiar() {
        view.txtCodigo.setText("");
        view.txtNombre.setText("");
        view.txtCreditos.setText("");
        if (view.cmbPrerrequisito.getItemCount() > 0) view.cmbPrerrequisito.setSelectedIndex(0);
        view.tblAsignatura.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Asignatura a : asignaturas) {
            String prereq = a.getPrerrequisitos().isEmpty() ? "Ninguno" : a.getPrerrequisitos().get(0).getCodigo();
            modelo.addRow(new Object[]{false, a.getCodigo(), a.getNombre(), a.getCreditosSct(), prereq});
        }
    }

    private void refrescarCombo() {
        view.cmbPrerrequisito.removeAllItems();
        view.cmbPrerrequisito.addItem("Ninguno");
        for (Asignatura a : asignaturas) {
            view.cmbPrerrequisito.addItem(a.getCodigo());
        }
    }
}
