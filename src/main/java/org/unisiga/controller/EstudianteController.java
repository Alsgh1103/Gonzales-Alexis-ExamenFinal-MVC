package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Departamento;
import org.unisiga.model.Estudiante;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.view.EstudianteView;

public class EstudianteController {

    private EstudianteView view;
    private List<MiembroUniversitario> usuarios;
    private DefaultTableModel modelo;
    private int filaSeleccionada = -1;

    public EstudianteController(EstudianteView view, List<MiembroUniversitario> usuarios) {
        this.view = view;
        this.usuarios = usuarios;
        this.modelo = (DefaultTableModel) view.tblEstudiante.getModel();
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

        view.tblEstudiante.addMouseListener(new java.awt.event.MouseAdapter() {
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
        String matricula = view.txtInscripcion.getText().trim();
        String anioStr = view.txtAnio.getText().trim();
        String promedioStr = view.txtPromedio.getText().trim();

        if (rut.isEmpty() || nombre.isEmpty() || correo.isEmpty() || matricula.isEmpty() || anioStr.isEmpty() || promedioStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equalsIgnoreCase(rut)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Ya existe un usuario con ese RUT.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int anio;
        float promedio;
        try {
            anio = Integer.parseInt(anioStr);
            promedio = Float.parseFloat(promedioStr);
            if (promedio < 1.0f || promedio > 7.0f) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Año debe ser entero. Promedio entre 1.0 y 7.0.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Estudiante nuevo = new Estudiante(rut, nombre, correo, matricula, anio, promedio);
        usuarios.add(nuevo);
        refrescarTabla();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione un estudiante de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rutOriginal = modelo.getValueAt(filaSeleccionada, 1).toString();
        String nombre = view.txtNombre.getText().trim();
        String correo = view.txtCorreo.getText().trim();
        String anioStr = view.txtAnio.getText().trim();
        String promedioStr = view.txtPromedio.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || anioStr.isEmpty() || promedioStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equals(rutOriginal) && u instanceof Estudiante) {
                u.setNombre(nombre);
                u.setCorreoInstitucional(correo);
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
                usuarios.removeIf(u -> u.getRut().equals(rut) && u instanceof Estudiante);
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos un estudiante para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblEstudiante.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtRut.setText(modelo.getValueAt(fila, 1).toString());
        view.txtNombre.setText(modelo.getValueAt(fila, 2).toString());
        view.txtCorreo.setText(modelo.getValueAt(fila, 3).toString());
        view.txtInscripcion.setText(modelo.getValueAt(fila, 4).toString());
        view.txtAnio.setText(modelo.getValueAt(fila, 5).toString());
        view.txtPromedio.setText(modelo.getValueAt(fila, 6).toString());
    }

    private void limpiar() {
        view.txtRut.setText("");
        view.txtNombre.setText("");
        view.txtCorreo.setText("");
        view.txtInscripcion.setText("");
        view.txtAnio.setText("");
        view.txtPromedio.setText("");
        view.tblEstudiante.clearSelection();
        filaSeleccionada = -1;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (MiembroUniversitario u : usuarios) {
            if (u instanceof Estudiante) {
                Estudiante e = (Estudiante) u;
                modelo.addRow(new Object[]{false, e.getRut(), e.getNombre(), e.getCorreoInstitucional(), e.getMatricula(), e.getAnioIngreso(), e.getPromedioPpa()});
            }
        }
    }
}
